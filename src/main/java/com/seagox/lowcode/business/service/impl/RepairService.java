package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.Repair;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.RepairMapper;
import com.seagox.lowcode.business.service.IRepairService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.mapper.MessageMapper;
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

    private static final String BUSINESS_TYPE = "repair";
    private static final int MESSAGE_TYPE_REPAIR_ASSIGN = 2;

    @Autowired
    private RepairMapper repairMapper;

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
        repair.setRepairedAt(original.getRepairedAt());
        repair.setAcceptedAt(original.getAcceptedAt());
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
        Date now = new Date();
        repair.setStatus(STATUS_PROCESSING);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(now);
        repairMapper.updateById(repair);
        refreshRepairAssignMessage(repair, projectMember, userId, now);
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
        original.setRepairedAt(now);
        original.setAcceptedAt(null);
        original.setStatus(STATUS_CONFIRMING);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        repairMapper.updateById(original);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        insertRepairMessage(original, userId, original.getCreatedBy(), "您有一条维修工单待验收", now);
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
        Date now = new Date();
        repair.setStatus(STATUS_COMPLETED);
        repair.setAcceptedAt(now);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(now);
        repairMapper.updateById(repair);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
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
        repair.setRepairedAt(null);
        repair.setAcceptedAt(null);
        repair.setUpdatedBy(userId);
        repair.setUpdatedAt(new Date());
        repairMapper.updateById(repair);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
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
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
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
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        return ResultData.success(null);
    }

    /**
     * 刷新维修指派待办消息
     */
    private void refreshRepairAssignMessage(Repair repair, ProjectMember projectMember, Long userId, Date now) {
        if (repair.getId() == null) {
            return;
        }
        messageMapper.deleteMessage(BUSINESS_TYPE, repair.getId());
        if (projectMember.getUserId() == null || projectMember.getUserId().equals(userId)) {
            return;
        }
        Project project = projectMapper.selectById(repair.getProjectId());
        if (project == null || project.getCompanyId() == null) {
            return;
        }
        SysMessage message = new SysMessage();
        message.setCompanyId(project.getCompanyId());
        message.setType(MESSAGE_TYPE_REPAIR_ASSIGN);
        message.setFromUserId(userId);
        message.setToUserId(projectMember.getUserId());
        message.setTitle("您有一条新的维修工单");
        message.setBusinessType(BUSINESS_TYPE);
        message.setBusinessKey(repair.getId());
        message.setStatus(0);
        message.setCreatedBy(userId);
        message.setCreatedAt(now);
        message.setUpdatedBy(userId);
        message.setUpdatedAt(now);
        messageMapper.insert(message);
    }

    /**
     * 插入维修待办消息
     */
    private void insertRepairMessage(Repair repair, Long fromUserId, Long toUserId, String title, Date now) {
        if (repair.getId() == null || toUserId == null || toUserId.equals(fromUserId)) {
            return;
        }
        Project project = projectMapper.selectById(repair.getProjectId());
        if (project == null || project.getCompanyId() == null) {
            return;
        }
        SysMessage message = new SysMessage();
        message.setCompanyId(project.getCompanyId());
        message.setType(MESSAGE_TYPE_REPAIR_ASSIGN);
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setTitle(title);
        message.setBusinessType(BUSINESS_TYPE);
        message.setBusinessKey(repair.getId());
        message.setStatus(0);
        message.setCreatedBy(fromUserId);
        message.setCreatedAt(now);
        message.setUpdatedBy(fromUserId);
        message.setUpdatedAt(now);
        messageMapper.insert(message);
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
