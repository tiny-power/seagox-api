package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.LeaveRequest;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.entity.SeaInstance;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.entity.SeaNodeDetail;
import com.seagox.lowcode.mapper.LeaveRequestMapper;
import com.seagox.lowcode.mapper.SeaDefinitionMapper;
import com.seagox.lowcode.mapper.SeaInstanceMapper;
import com.seagox.lowcode.mapper.SeaNodeDetailMapper;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.ILeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LeaveRequestService implements ILeaveRequestService {

    public static final String BUSINESS_TYPE = "leave_request";
    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_APPROVING = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_APPROVED = 3;
    public static final int STATUS_REJECTED = 4;

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Autowired
    private SeaDefinitionMapper seaDefinitionMapper;

    @Autowired
    private SeaInstanceMapper seaInstanceMapper;

    @Autowired
    private SeaNodeMapper seaNodeMapper;

    @Autowired
    private SeaNodeDetailMapper seaNodeDetailMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = leaveRequestMapper.queryByPage(companyId, applicantId, applicantName,
                leaveType, status, startTime, endTime);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData insert(LeaveRequest leaveRequest) {
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(STATUS_DRAFT);
        leaveRequestMapper.insert(leaveRequest);
        return ResultData.success(null);
    }

    @Override
    public ResultData update(LeaveRequest leaveRequest) {
        LeaveRequest original = leaveRequestMapper.selectById(leaveRequest.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请假单不可以修改");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请假单不可以修改");
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(original.getStatus());
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData delete(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请假单不可以删除");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请假单不可以删除");
        }
        clearProcess(BUSINESS_TYPE, String.valueOf(id));
        leaveRequestMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData submit(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单正在审批中");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单已经审批通过");
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        SeaDefinition seaDefinition = queryFlowDefinition();
        if (seaDefinition == null || StringUtils.isEmpty(seaDefinition.getResources())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请先维护请假单流程定义");
        }
        clearProcess(BUSINESS_TYPE, String.valueOf(id));
        int processStatus = runtimeService.startProcessInstance(buildFlowVariables(leaveRequest, seaDefinition));
        leaveRequest.setStatus(processStatus == 1 ? STATUS_APPROVED : STATUS_APPROVING);
        leaveRequest.setSubmitTime(new Date());
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData cancel(Long id) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (!Integer.valueOf(STATUS_APPROVING).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有审批中的请假单可以撤销");
        }
        terminateProcess(BUSINESS_TYPE, String.valueOf(id));
        leaveRequest.setStatus(STATUS_CANCELED);
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    private ResultData verify(LeaveRequest leaveRequest) {
        if (leaveRequest.getCompanyId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择公司");
        }
        if (leaveRequest.getApplicantId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择申请人");
        }
        if (leaveRequest.getLeaveType() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择请假类型");
        }
        if (leaveRequest.getStartTime() == null || leaveRequest.getEndTime() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择请假时间");
        }
        if (leaveRequest.getEndTime().before(leaveRequest.getStartTime())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "结束时间不能早于开始时间");
        }
        if (leaveRequest.getDuration() == null || leaveRequest.getDuration().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请假时长");
        }
        if (StringUtils.isEmpty(leaveRequest.getReason())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请假事由");
        }
        return null;
    }

    private SeaDefinition queryFlowDefinition() {
        LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
        qw.eq(SeaDefinition::getBusinessType, BUSINESS_TYPE)
        .orderByDesc(SeaDefinition::getUpdateTime);
        List<SeaDefinition> list = seaDefinitionMapper.selectList(qw);
        return list.isEmpty() ? null : list.get(0);
    }

    private Map<String, Object> buildFlowVariables(LeaveRequest leaveRequest, SeaDefinition seaDefinition) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("companyId", leaveRequest.getCompanyId());
        variables.put("creator", leaveRequest.getApplicantId());
        variables.put("title", "请假单-" + leaveRequest.getId());
        variables.put("businessType", BUSINESS_TYPE);
        variables.put("businessKey", leaveRequest.getId());
        variables.put("resources", seaDefinition.getResources());
        variables.put("leaveType", leaveRequest.getLeaveType());
        variables.put("startTime", leaveRequest.getStartTime());
        variables.put("endTime", leaveRequest.getEndTime());
        variables.put("duration", leaveRequest.getDuration());
        variables.put("reason", leaveRequest.getReason());
        return variables;
    }

    private void clearProcess(String businessType, String businessKey) {
        LambdaQueryWrapper<SeaInstance> instanceQw = new LambdaQueryWrapper<>();
        instanceQw.eq(SeaInstance::getBusinessType, businessType)
        .eq(SeaInstance::getBusinessKey, businessKey);
        List<SeaInstance> seaInstanceList = seaInstanceMapper.selectList(instanceQw);
        for (SeaInstance seaInstance : seaInstanceList) {
            LambdaQueryWrapper<SeaNode> nodeQw = new LambdaQueryWrapper<>();
            nodeQw.eq(SeaNode::getDefId, seaInstance.getId());
            List<SeaNode> seaNodeList = seaNodeMapper.selectList(nodeQw);
            List<Long> nodeIds = new ArrayList<>();
            for (SeaNode seaNode : seaNodeList) {
                nodeIds.add(seaNode.getId());
            }
            if (!nodeIds.isEmpty()) {
                LambdaQueryWrapper<SeaNodeDetail> detailQw = new LambdaQueryWrapper<>();
                detailQw.in(SeaNodeDetail::getNodeId, nodeIds);
                seaNodeDetailMapper.delete(detailQw);
            }
            seaNodeMapper.delete(nodeQw);
            seaInstanceMapper.deleteById(seaInstance.getId());
        }
    }

    private void terminateProcess(String businessType, String businessKey) {
        LambdaQueryWrapper<SeaInstance> instanceQw = new LambdaQueryWrapper<>();
        instanceQw.eq(SeaInstance::getBusinessType, businessType)
        .eq(SeaInstance::getBusinessKey, businessKey);
        List<SeaInstance> seaInstanceList = seaInstanceMapper.selectList(instanceQw);
        for (SeaInstance seaInstance : seaInstanceList) {
            terminateProcess(seaInstance);
        }
    }

    private void terminateProcess(SeaInstance seaInstance) {
        SeaInstance updateInstance = new SeaInstance();
        updateInstance.setId(seaInstance.getId());
        updateInstance.setStatus(3);
        updateInstance.setEndTime(new Date());
        seaInstanceMapper.updateById(updateInstance);

        LambdaQueryWrapper<SeaNode> activeNodeQw = new LambdaQueryWrapper<>();
        activeNodeQw.eq(SeaNode::getDefId, seaInstance.getId())
        .eq(SeaNode::getStatus, 0);
        List<SeaNode> activeNodeList = seaNodeMapper.selectList(activeNodeQw);
        List<Long> nodeIds = new ArrayList<>();
        for (SeaNode seaNode : activeNodeList) {
            nodeIds.add(seaNode.getId());
        }
        if (!nodeIds.isEmpty()) {
            SeaNodeDetail detail = new SeaNodeDetail();
            detail.setStatus(3);
            detail.setEndTime(new Date());
            LambdaQueryWrapper<SeaNodeDetail> detailQw = new LambdaQueryWrapper<>();
            detailQw.in(SeaNodeDetail::getNodeId, nodeIds)
            .eq(SeaNodeDetail::getStatus, 0);
            seaNodeDetailMapper.update(detail, detailQw);
        }
        SeaNode node = new SeaNode();
        node.setStatus(4);
        node.setEndTime(new Date());
        seaNodeMapper.update(node, activeNodeQw);
    }

}
