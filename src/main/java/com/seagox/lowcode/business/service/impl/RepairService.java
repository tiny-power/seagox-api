package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Repair;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.RepairMapper;
import com.seagox.lowcode.business.service.IRepairService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 报修单
 */
@Service
public class RepairService implements IRepairService {

    public static final int STATUS_SUBMITTED = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_CONFIRMING = 3;
    public static final int STATUS_COMPLETED = 4;
    public static final int STATUS_CANCELED = 5;

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = repairMapper.queryRepairs(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = repairMapper.queryRepairById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData insert(Repair repair, Long userId) {
        ResultData verifyResult = verify(repair);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        if (projectMapper.selectById(repair.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        if (StringUtils.isEmpty(repair.getRepairNo())) {
            repair.setRepairNo(generateRepairNo(now));
        }
        repair.setStatus(STATUS_SUBMITTED);
        repair.setRepairAt(repair.getRepairAt() == null ? now : repair.getRepairAt());
        repair.setCreatedBy(userId);
        repair.setUpdatedBy(userId);
        repair.setCreatedAt(now);
        repair.setUpdatedAt(now);
        repairMapper.insert(repair);
        return ResultData.success(repair.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData update(Repair repair, Long userId) {
        if (repair == null || repair.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单ID不能为空");
        }
        Repair original = repairMapper.selectById(repair.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的报修单不可以修改");
        }
        if (Integer.valueOf(STATUS_CANCELED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已取消的报修单不可以修改");
        }
        ResultData verifyResult = verify(repair);
        if (verifyResult != null) {
            return verifyResult;
        }

        repair.setStatus(original.getStatus());
        repair.setRepairNo(original.getRepairNo());
        repair.setRepairAt(original.getRepairAt());
        repair.setAfterAttachments(original.getAfterAttachments());
        repair.setRepairMemberId(original.getRepairMemberId());
        repair.setExpectedVisitAt(original.getExpectedVisitAt());
        repair.setCompleteAt(original.getCompleteAt());
        repair.setCreatedBy(original.getCreatedBy());
        repair.setCreatedAt(original.getCreatedAt());
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData assign(Long id, Long repairMemberId, Date expectedVisitAt, Long userId) {
        if (repairMemberId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "维修人员不能为空");
        }
        if (expectedVisitAt == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "预计上门时间不能为空");
        }
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的报修单不可以指派");
        }
        if (Integer.valueOf(STATUS_CANCELED).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已取消的报修单不可以指派");
        }
        ProjectMember projectMember = projectMemberMapper.selectById(repairMemberId);
        if (projectMember == null || !repair.getProjectId().equals(projectMember.getProjectId())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "维修人员不属于当前项目");
        }
        repair.setRepairMemberId(repairMemberId);
        repair.setExpectedVisitAt(expectedVisitAt);
        repair.setStatus(STATUS_PROCESSING);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData complete(Long id, Repair repair, Long userId) {
        Repair original = repairMapper.selectById(id);
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (!Integer.valueOf(STATUS_PROCESSING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有处理中的报修单可以提交完成");
        }
        Date now = new Date();
        original.setAfterAttachments(repair == null ? original.getAfterAttachments() : repair.getAfterAttachments());
        original.setRepairResult(repair == null ? original.getRepairResult() : repair.getRepairResult());
        original.setCompleteAt(now);
        original.setStatus(STATUS_CONFIRMING);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        repairMapper.updateById(original);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData confirm(Long id, Long userId) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (!Integer.valueOf(STATUS_CONFIRMING).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有待验收的报修单可以确认完成");
        }
        repair.setStatus(STATUS_COMPLETED);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData rework(Long id, Long userId) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (!Integer.valueOf(STATUS_CONFIRMING).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有待验收的报修单可以重新维修");
        }
        repair.setStatus(STATUS_PROCESSING);
        repair.setAfterAttachments(null);
        repair.setRepairResult(null);
        repair.setCompleteAt(null);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData cancel(Long id, Long userId) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的报修单不可以取消");
        }
        if (Integer.valueOf(STATUS_CANCELED).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单已取消");
        }
        repair.setStatus(STATUS_CANCELED);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        return ResultData.success(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultData delete(Long id) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(repair.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的报修单不可以删除");
        }
        repairMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 校验报修单保存参数
     */
    private ResultData verify(Repair repair) {
        if (repair == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "报修单不能为空");
        }
        if (repair.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (StringUtils.isEmpty(repair.getType())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择报修类型");
        }
        if (StringUtils.isEmpty(repair.getLocation())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择报修位置");
        }
        if (StringUtils.isEmpty(repair.getDescription())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写问题描述");
        }
        if (StringUtils.isEmpty(repair.getContact())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写联系人");
        }
        if (StringUtils.isEmpty(repair.getContactNumber())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写联系电话");
        }
        return null;
    }

    private String generateRepairNo(Date now) {
        return "BX" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now)
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }
}
