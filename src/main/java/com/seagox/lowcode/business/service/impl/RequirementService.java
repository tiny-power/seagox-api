package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.entity.Requirement;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.RequirementMapper;
import com.seagox.lowcode.business.service.IRequirementService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.mapper.MessageMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 需求沟通表
 */
@Service
public class RequirementService implements IRequirementService {

    /**
     * 待提交
     */
    public static final int STATUS_DRAFT = 1;
    /**
     * 待审核
     */
    public static final int STATUS_PENDING = 2;
    /**
     * 已完成
     */
    public static final int STATUS_COMPLETED = 3;

    /**
     * 需求沟通消息业务类型
     */
    private static final String BUSINESS_TYPE = "requirement";

    /**
     * 需求表消息类型
     */
    private static final int MESSAGE_TYPE_REQUIREMENT = 4;

    /**
     * 业主项目角色编码
     */
    private static final int ROLE_OWNER = 10;

    /**
     * 需求沟通数据访问对象
     */
    @Autowired
    private RequirementMapper requirementMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 项目成员数据访问对象
     */
    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    /**
     * 消息数据访问对象
     */
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 分页查询需求沟通
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = requirementMapper.queryRequirements(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询需求沟通详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = requirementMapper.queryRequirementById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 查询项目需求沟通
     */
    @Override
    public ResultData queryByProjectId(Long projectId) {
        if (projectId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        LambdaQueryWrapper<Requirement> qw = new LambdaQueryWrapper<>();
        qw.eq(Requirement::getProjectId, projectId).orderByDesc(Requirement::getUpdatedAt);
        List<Requirement> list = requirementMapper.selectList(qw);
        Requirement requirement = list.isEmpty() ? null : list.get(0);
        if (requirement == null) {
            return ResultData.success(null);
        }
        return queryById(requirement.getId());
    }

    /**
     * 保存需求沟通
     */
    @Transactional
    @Override
    public ResultData save(Requirement requirement, Long userId) {
        ResultData verifyResult = verify(requirement);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Project project = projectMapper.selectById(requirement.getProjectId());
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        Requirement original = null;
        if (requirement.getId() != null) {
            original = requirementMapper.selectById(requirement.getId());
        }
        if (original == null) {
            original = findByProjectId(requirement.getProjectId());
        }

        if (original == null) {
            requirement.setStatus(requirement.getStatus() == null ? STATUS_DRAFT : requirement.getStatus());
            requirement.setSignatureUrl(requirement.getSignatureUrl() == null ? "" : requirement.getSignatureUrl());
            requirement.setSignedAt(Integer.valueOf(STATUS_COMPLETED).equals(requirement.getStatus()) ? requirement.getSignedAt() : null);
            requirement.setCreatedBy(userId);
            requirement.setUpdatedBy(userId);
            requirement.setCreatedAt(now);
            requirement.setUpdatedAt(now);
            requirementMapper.insert(requirement);
            return ResultData.success(requirement.getId());
        }

        if (Integer.valueOf(STATUS_COMPLETED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的需求沟通不可以修改");
        }
        original.setStyle(requirement.getStyle());
        original.setBudget(requirement.getBudget());
        original.setMember(requirement.getMember());
        original.setMark(requirement.getMark());
        original.setStatus(requirement.getStatus() == null ? original.getStatus() : requirement.getStatus());
        if (!Integer.valueOf(STATUS_COMPLETED).equals(original.getStatus())) {
            original.setSignedAt(null);
            original.setSignatureUrl("");
        }
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        requirementMapper.updateById(original);
        return ResultData.success(original.getId());
    }

    /**
     * 提交业主审核
     */
    @Transactional
    @Override
    public ResultData submit(Long id, Long userId, Long companyId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        if (companyId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "公司不能为空");
        }
        Requirement requirement = requirementMapper.selectById(id);
        if (requirement == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(requirement.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的需求沟通不可以提交");
        }
        ProjectMember owner = findOwner(requirement.getProjectId());
        if (owner == null || owner.getUserId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目业主不存在");
        }
        requirement.setStatus(STATUS_PENDING);
        requirement.setUpdatedBy(userId);
        Date now = new Date();
        requirement.setUpdatedAt(now);
        requirementMapper.updateById(requirement);
        refreshOwnerMessage(requirement, owner.getUserId(), userId, companyId, now);
        return ResultData.success(null);
    }

    /**
     * 签字确认
     */
    @Override
    public ResultData sign(Long id, String signatureUrl, Long userId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Requirement requirement = requirementMapper.selectById(id);
        if (requirement == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不存在");
        }
        if (!Integer.valueOf(STATUS_PENDING).equals(requirement.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有待审核的需求沟通可以签字");
        }
        if (StringUtils.isEmpty(signatureUrl)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签字文件不能为空");
        }
        Date now = new Date();
        requirement.setSignatureUrl(signatureUrl);
        requirement.setSignedAt(now);
        requirement.setStatus(STATUS_COMPLETED);
        requirement.setUpdatedBy(userId);
        requirement.setUpdatedAt(now);
        requirementMapper.updateById(requirement);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        return ResultData.success(null);
    }

    /**
     * 删除需求沟通
     */
    @Override
    public ResultData delete(Long id) {
        Requirement requirement = requirementMapper.selectById(id);
        if (requirement == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(requirement.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的需求沟通不可以删除");
        }
        requirementMapper.deleteById(id);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        return ResultData.success(null);
    }

    /**
     * 查询项目业主
     */
    private ProjectMember findOwner(Long projectId) {
        LambdaQueryWrapper<ProjectMember> qw = new LambdaQueryWrapper<>();
        qw.eq(ProjectMember::getProjectId, projectId)
                .eq(ProjectMember::getRoleCode, ROLE_OWNER)
                .eq(ProjectMember::getStatus, 1)
                .orderByAsc(ProjectMember::getId);
        List<ProjectMember> list = projectMemberMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 重建业主审批消息
     */
    private void refreshOwnerMessage(Requirement requirement, Long ownerUserId, Long userId, Long companyId, Date now) {
        messageMapper.deleteMessage(BUSINESS_TYPE, requirement.getId());

        SysMessage message = new SysMessage();
        message.setCompanyId(companyId);
        message.setType(MESSAGE_TYPE_REQUIREMENT);
        message.setFromUserId(userId);
        message.setToUserId(ownerUserId);
        message.setTitle("您有一条需求沟通待审核");
        message.setBusinessType(BUSINESS_TYPE);
        message.setBusinessKey(requirement.getId());
        message.setStatus(0);
        message.setCreatedBy(userId);
        message.setCreatedAt(now);
        message.setUpdatedBy(userId);
        message.setUpdatedAt(now);
        messageMapper.insert(message);
    }

    /**
     * 按项目查询需求沟通
     */
    private Requirement findByProjectId(Long projectId) {
        LambdaQueryWrapper<Requirement> qw = new LambdaQueryWrapper<>();
        qw.eq(Requirement::getProjectId, projectId).orderByDesc(Requirement::getUpdatedAt);
        List<Requirement> list = requirementMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 校验需求沟通
     */
    private ResultData verify(Requirement requirement) {
        if (requirement == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不能为空");
        }
        if (requirement.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (StringUtils.isEmpty(requirement.getStyle())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写风格偏好");
        }
        if (StringUtils.isEmpty(requirement.getBudget())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写预算范围");
        }
        if (StringUtils.isEmpty(requirement.getMember())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写家庭成员");
        }
        return null;
    }
}
