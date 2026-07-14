package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.entity.SolutionDesign;
import com.seagox.lowcode.business.entity.SolutionDesignDetail;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.SolutionDesignMapper;
import com.seagox.lowcode.business.service.ISolutionDesignService;
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
 * 方案设计
 */
@Service
public class SolutionDesignService implements ISolutionDesignService {

    public static final int STATUS_DRAFT = 1;
    public static final int STATUS_PENDING = 2;
    public static final int STATUS_CONFIRMED = 3;
    public static final int STATUS_FROZEN = 4;
    public static final int STATUS_DEFROSTING = 5;
    public static final int STATUS_COMPLETED = 6;
    private static final String BUSINESS_TYPE = "solution_design";
    private static final int MESSAGE_TYPE_SOLUTION_DESIGN = 4;
    private static final int ROLE_OWNER = 10;

    @Autowired
    private SolutionDesignMapper solutionDesignMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private MessageMapper messageMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = solutionDesignMapper.querySolutionDesigns(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = solutionDesignMapper.querySolutionDesignById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        List<Map<String, Object>> details = solutionDesignMapper.querySolutionDesignDetails(id);
        MapDateFormatUtils.formatDateValues(details);
        data.put("details", details);
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByProjectId(Long projectId) {
        if (projectId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        SolutionDesign solutionDesign = findByProjectId(projectId);
        if (solutionDesign == null) {
            return ResultData.success(null);
        }
        return queryById(solutionDesign.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ResultData save(SolutionDesign solutionDesign, Long userId) {
        ResultData verifyResult = verify(solutionDesign);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        Project project = projectMapper.selectById(solutionDesign.getProjectId());
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        SolutionDesign original = null;
        if (solutionDesign.getId() != null) {
            original = solutionDesignMapper.selectById(solutionDesign.getId());
        }
        if (original == null) {
            original = findByProjectId(solutionDesign.getProjectId());
        }

        if (original == null) {
            solutionDesign.setStatus(solutionDesign.getStatus() == null ? STATUS_DRAFT : solutionDesign.getStatus());
            solutionDesign.setCreatedBy(userId);
            solutionDesign.setUpdatedBy(userId);
            solutionDesign.setCreatedAt(now);
            solutionDesign.setUpdatedAt(now);
            solutionDesignMapper.insert(solutionDesign);
            SolutionDesignDetail detail = buildDetail(solutionDesign, solutionDesign.getId(), userId, now);
            solutionDesignMapper.insertDetail(detail);
            return ResultData.success(solutionDesign.getId());
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的方案设计不可以修改");
        }
        SolutionDesignDetail latestDetail = solutionDesignMapper.queryLatestDetail(original.getId());
        SolutionDesignDetail detail = buildDetail(solutionDesign, original.getId(), userId, now);
        if (latestDetail != null && shouldUpdateLatestDetail(latestDetail, detail)) {
            detail.setId(latestDetail.getId());
            detail.setCreatedBy(latestDetail.getCreatedBy());
            detail.setCreatedAt(latestDetail.getCreatedAt());
            detail.setSignatureUrl(defaultString(solutionDesign.getSignatureUrl(), latestDetail.getSignatureUrl()));
            detail.setSignedAt(solutionDesign.getSignedAt() == null ? latestDetail.getSignedAt() : solutionDesign.getSignedAt());
            detail.setDefrostExplanation(defaultString(solutionDesign.getDefrostExplanation(), latestDetail.getDefrostExplanation()));
            detail.setApplyDefrostAt(solutionDesign.getApplyDefrostAt() == null ? latestDetail.getApplyDefrostAt() : solutionDesign.getApplyDefrostAt());
            solutionDesignMapper.updateDetailById(detail);
        } else {
            solutionDesignMapper.insertDetail(detail);
        }
        original.setStatus(solutionDesign.getStatus() == null ? original.getStatus() : solutionDesign.getStatus());
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        solutionDesignMapper.updateById(original);
        return ResultData.success(original.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ResultData submit(Long id, Long userId, Long companyId) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        if (companyId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "公司不能为空");
        }
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的方案设计不可以操作");
        }
        ProjectMember owner = findOwner(solutionDesign.getProjectId());
        if (owner == null || owner.getUserId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目业主不存在");
        }
        Date now = new Date();
        solutionDesign.setStatus(STATUS_PENDING);
        solutionDesign.setUpdatedBy(userId);
        solutionDesign.setUpdatedAt(now);
        solutionDesignMapper.updateById(solutionDesign);
        refreshOwnerMessage(solutionDesign, owner.getUserId(), userId, companyId, now);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData confirm(Long id, Long userId) {
        ResultData result = updateStatus(id, userId, STATUS_CONFIRMED, "方案设计不存在", STATUS_PENDING);
        if (ResultCode.SUCCESS.getCode() == result.getCode()) {
            messageMapper.deleteMessage(BUSINESS_TYPE, id);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData freeze(Long id, String signatureUrl, Long userId) {
        if (StringUtils.isEmpty(signatureUrl)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签字文件不能为空");
        }
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        if (!Integer.valueOf(STATUS_CONFIRMED).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已确认的方案可以冻结");
        }
        Date now = new Date();
        SolutionDesignDetail detail = solutionDesignMapper.queryLatestDetail(id);
        if (detail == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计详情不存在");
        }
        detail.setSignatureUrl(signatureUrl);
        detail.setSignedAt(now);
        detail.setUpdatedBy(userId);
        detail.setUpdatedAt(now);
        solutionDesignMapper.updateDetailById(detail);
        solutionDesign.setStatus(STATUS_FROZEN);
        solutionDesign.setUpdatedBy(userId);
        solutionDesign.setUpdatedAt(now);
        solutionDesignMapper.updateById(solutionDesign);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData applyDefrost(Long id, String defrostExplanation, Long userId) {
        if (StringUtils.isEmpty(defrostExplanation)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "解冻说明不能为空");
        }
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        if (!Integer.valueOf(STATUS_FROZEN).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已冻结的方案可以申请解冻");
        }
        Date now = new Date();
        SolutionDesignDetail detail = solutionDesignMapper.queryLatestDetail(id);
        if (detail == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计详情不存在");
        }
        detail.setDefrostExplanation(defrostExplanation);
        detail.setApplyDefrostAt(now);
        detail.setUpdatedBy(userId);
        detail.setUpdatedAt(now);
        solutionDesignMapper.updateDetailById(detail);
        solutionDesign.setStatus(STATUS_DEFROSTING);
        solutionDesign.setUpdatedBy(userId);
        solutionDesign.setUpdatedAt(now);
        solutionDesignMapper.updateById(solutionDesign);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData approveDefrost(Long id, Long userId) {
        return updateStatus(id, userId, STATUS_DRAFT, "方案设计不存在", STATUS_DEFROSTING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData rejectDefrost(Long id, Long userId) {
        return updateStatus(id, userId, STATUS_FROZEN, "方案设计不存在", STATUS_DEFROSTING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData complete(Long id, Long userId) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        if (!Integer.valueOf(STATUS_FROZEN).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已冻结的方案可以完成");
        }
        solutionDesign.setStatus(STATUS_COMPLETED);
        solutionDesign.setUpdatedBy(userId);
        solutionDesign.setUpdatedAt(new Date());
        solutionDesignMapper.updateById(solutionDesign);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData delete(Long id) {
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的方案设计不可以删除");
        }
        solutionDesignMapper.deleteDetailsBySolutionDesignId(id);
        solutionDesignMapper.deleteById(id);
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
     * 重建业主确认消息
     */
    private void refreshOwnerMessage(SolutionDesign solutionDesign, Long ownerUserId, Long userId, Long companyId, Date now) {
        messageMapper.deleteMessage(BUSINESS_TYPE, solutionDesign.getId());

        SysMessage message = new SysMessage();
        message.setCompanyId(companyId);
        message.setType(MESSAGE_TYPE_SOLUTION_DESIGN);
        message.setFromUserId(userId);
        message.setToUserId(ownerUserId);
        message.setTitle("您有一条方案设计待确认");
        message.setBusinessType(BUSINESS_TYPE);
        message.setBusinessKey(solutionDesign.getId());
        message.setStatus(0);
        message.setCreatedBy(userId);
        message.setCreatedAt(now);
        message.setUpdatedBy(userId);
        message.setUpdatedAt(now);
        messageMapper.insert(message);
    }

    /**
     * 更新方案设计状态并校验当前状态
     */
    private ResultData updateStatus(Long id, Long userId, Integer status, String notFoundMessage, Integer requiredStatus) {
        ResultData userResult = checkUserId(userId);
        if (userResult != null) {
            return userResult;
        }
        SolutionDesign solutionDesign = solutionDesignMapper.selectById(id);
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, notFoundMessage);
        }
        if (requiredStatus != null && !requiredStatus.equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前状态不允许操作");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(solutionDesign.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的方案设计不可以操作");
        }
        solutionDesign.setStatus(status);
        solutionDesign.setUpdatedBy(userId);
        solutionDesign.setUpdatedAt(new Date());
        solutionDesignMapper.updateById(solutionDesign);
        return ResultData.success(null);
    }

    /**
     * 通过项目ID查询最新方案设计记录
     */
    private SolutionDesign findByProjectId(Long projectId) {
        LambdaQueryWrapper<SolutionDesign> qw = new LambdaQueryWrapper<>();
        qw.eq(SolutionDesign::getProjectId, projectId).orderByDesc(SolutionDesign::getUpdatedAt);
        List<SolutionDesign> list = solutionDesignMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 校验当前用户ID
     */
    private ResultData checkUserId(Long userId) {
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        return null;
    }

    /**
     * 校验方案设计保存参数
     */
    private ResultData verify(SolutionDesign solutionDesign) {
        if (solutionDesign == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "方案设计不能为空");
        }
        if (solutionDesign.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (StringUtils.isEmpty(solutionDesign.getVersion())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写版本");
        }
        if (StringUtils.isEmpty(solutionDesign.getAttachments())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请上传效果图");
        }
        if (StringUtils.isEmpty(solutionDesign.getSolutionExplanation())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写方案说明");
        }
        return null;
    }

    /**
     * 构建方案设计版本明细
     */
    private SolutionDesignDetail buildDetail(SolutionDesign solutionDesign, Long solutionDesignId, Long userId, Date now) {
        SolutionDesignDetail detail = new SolutionDesignDetail();
        detail.setSolutionDesignId(solutionDesignId);
        detail.setVersion(parseVersion(solutionDesign.getVersion()));
        detail.setAttachments(solutionDesign.getAttachments());
        detail.setSolutionExplanation(solutionDesign.getSolutionExplanation());
        detail.setAnnotation(solutionDesign.getAnnotation());
        detail.setDefrostExplanation(solutionDesign.getDefrostExplanation());
        detail.setApplyDefrostAt(solutionDesign.getApplyDefrostAt());
        detail.setSignatureUrl(defaultString(solutionDesign.getSignatureUrl(), ""));
        detail.setSignedAt(solutionDesign.getSignedAt());
        detail.setCreatedBy(userId);
        detail.setUpdatedBy(userId);
        detail.setCreatedAt(now);
        detail.setUpdatedAt(now);
        return detail;
    }

    /**
     * 判断是否更新最新版本明细
     */
    private boolean shouldUpdateLatestDetail(SolutionDesignDetail latestDetail, SolutionDesignDetail detail) {
        return latestDetail.getVersion() != null
                && latestDetail.getVersion().equals(detail.getVersion());
    }

    /**
     * 解析版本号
     */
    private Integer parseVersion(String version) {
        if (StringUtils.isEmpty(version)) {
            return 1;
        }
        String value = version.trim().replaceAll("^[vV]", "");
        int dotIndex = value.indexOf(".");
        if (dotIndex > -1) {
            value = value.substring(0, dotIndex);
        }
        value = value.replaceAll("[^0-9]", "");
        if (StringUtils.isEmpty(value)) {
            return 1;
        }
        return Integer.valueOf(value);
    }

    /**
     * 返回默认字符串值
     */
    private String defaultString(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }
}
