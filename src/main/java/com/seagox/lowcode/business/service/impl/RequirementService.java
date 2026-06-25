package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.Requirement;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.RequirementMapper;
import com.seagox.lowcode.business.service.IRequirementService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
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
    @Override
    public ResultData submit(Long id, Long userId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Requirement requirement = requirementMapper.selectById(id);
        if (requirement == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "需求沟通不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(requirement.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的需求沟通不可以提交");
        }
        requirement.setStatus(STATUS_PENDING);
        requirement.setUpdatedBy(userId);
        requirement.setUpdatedAt(new Date());
        requirementMapper.updateById(requirement);
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
        return ResultData.success(null);
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
