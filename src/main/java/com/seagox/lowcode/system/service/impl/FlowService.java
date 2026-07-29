package com.seagox.lowcode.system.service.impl;

import app.tinybrief.weave.api.RepositoryService;
import app.tinybrief.weave.api.RuntimeService;
import app.tinybrief.weave.api.TaskService;
import app.tinybrief.weave.api.command.DeployDefinitionCommand;
import app.tinybrief.weave.api.dto.WeaveDefinition;
import app.tinybrief.weave.api.dto.WeaveInstance;
import app.tinybrief.weave.api.dto.WeaveProcessRecord;
import app.tinybrief.weave.api.dto.WeaveTask;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.PaymentRequest;
import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.business.entity.ProjectChangeOrder;
import com.seagox.lowcode.business.mapper.LeaveRequestMapper;
import com.seagox.lowcode.business.mapper.PaymentRequestMapper;
import com.seagox.lowcode.business.mapper.ProjectChangeOrderMapper;
import com.seagox.lowcode.business.service.ILeaveRequestService;
import com.seagox.lowcode.business.service.IPaymentRequestService;
import com.seagox.lowcode.business.service.IProjectChangeOrderService;
import com.seagox.lowcode.business.service.impl.LeaveRequestService;
import com.seagox.lowcode.business.service.impl.PaymentRequestService;
import com.seagox.lowcode.business.service.impl.ProjectChangeOrderService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.mapper.FlowMapper;
import com.seagox.lowcode.system.mapper.MessageMapper;
import com.seagox.lowcode.system.mapper.ProcessDraftMapper;
import com.seagox.lowcode.system.service.IFlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FlowService implements IFlowService {

    private static final int INSTANCE_COMPLETED = 1;
    private static final int INSTANCE_TERMINATED = 3;
    private static final DateTimeFormatter FLOW_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FlowMapper flowMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    @Autowired
    private ProjectChangeOrderMapper projectChangeOrderMapper;

    @Autowired
    private ILeaveRequestService leaveRequestService;

    @Autowired
    private IPaymentRequestService paymentRequestService;

    @Autowired
    private IProjectChangeOrderService projectChangeOrderService;

    @Autowired
    private ProcessDraftMapper processDraftMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public ResultData queryTodoItem(Integer pageNo, Integer pageSize, Long companyId, String userId, Long formId,
                                    String name, String statusStr, String businessTypeStr) {
        startPage(pageNo, pageSize);
        List<Map<String, Object>> list = flowMapper.queryTodoItem(companyId, userId, formId, name, statusStr, businessTypeStr);
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData queryDoneItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
                                    String startTime, String endTime, String todoPerson, String businessTypeStr) {
        startPage(pageNo, pageSize);
        List<Map<String, Object>> list = flowMapper.queryDoneItem(
                companyId,
                String.valueOf(userId),
                name,
                startTime,
                endTime,
                todoPerson,
                businessTypeStr
        );
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData queryCopyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
                                    String businessTypeStr) {
        startPage(pageNo, pageSize);
        List<Map<String, Object>> list = flowMapper.queryCopyItem(companyId, String.valueOf(userId), name, businessTypeStr);
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData querySelfItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
                                    String businessTypeStr) {
        startPage(pageNo, pageSize);
        List<Map<String, Object>> list = flowMapper.querySelfItem(companyId, userId, name, businessTypeStr);
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData queryReadyItem(Integer pageNo, Integer pageSize, Long companyId, Long userId, String name,
                                     String businessTypeStr) {
        startPage(pageNo, pageSize);
        List<Map<String, Object>> list = flowMapper.queryReadyItem(companyId, userId, name, businessTypeStr);
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData flowInfo(String businessType, String businessKey) {
        WeaveInstance instance = runtimeService.getProcessInstanceByBusinessKey(businessType, businessKey);
        if (instance == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "无流程信息");
        }
        return ResultData.success(toFlowInfo(instance));
    }

    @Override
    public ResultData queryProcessInfo(String businessType, String businessKey) {
        List<WeaveProcessRecord> records = runtimeService.listProcessRecords(businessType, businessKey);
        List<Map<String, Object>> result = new ArrayList<>(records.size());
        for (WeaveProcessRecord record : records) {
            result.add(toProcessInfo(record));
        }
        return ResultData.success(result);
    }

    @Transactional
    @Override
    public ResultData batchSubmit(String batchData) {
        JSONObject result = new JSONObject();
        int successNum = 0;
        int errorNum = 0;
        List<String> failList = new ArrayList<>();
        for (Object raw : parseBatch(batchData)) {
            JSONObject item = (JSONObject) raw;
            String businessType = item.getString("businessType");
            String businessKey = item.getString("businessKey");
            try {
                if (!LeaveRequestService.BUSINESS_TYPE.equals(businessType)
                        && !PaymentRequestService.BUSINESS_TYPE.equals(businessType)
                        && !ProjectChangeOrderService.BUSINESS_TYPE.equals(businessType)) {
                    throw new IllegalArgumentException("暂不支持该类别提交");
                }
                ResultData submitResult;
                if (LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
                    submitResult = leaveRequestService.submit(Long.valueOf(businessKey));
                } else if (PaymentRequestService.BUSINESS_TYPE.equals(businessType)) {
                    submitResult = paymentRequestService.submit(Long.valueOf(businessKey));
                } else {
                    submitResult = projectChangeOrderService.submit(Long.valueOf(businessKey));
                }
                if (submitResult.getCode() != ResultCode.SUCCESS.getCode()) {
                    throw new IllegalArgumentException(submitResult.getMessage());
                }
                if (item.getLong("id") != null) {
                    processDraftMapper.deleteById(item.getLong("id"));
                }
                successNum++;
            } catch (Exception e) {
                errorNum++;
                failList.add(e.getMessage());
            }
        }
        result.put("successNum", successNum);
        result.put("errorNum", errorNum);
        result.put("failList", failList);
        return ResultData.success(result);
    }

    @Transactional
    @Override
    public ResultData batchApprove(Long companyId, Long userId, Boolean approved, String comment, String rejectNode,
                                   String batchData) {
        JSONObject result = new JSONObject();
        int successNum = 0;
        int errorNum = 0;
        List<String> failList = new ArrayList<>();
        for (Object raw : parseBatch(batchData)) {
            JSONObject item = (JSONObject) raw;
            String businessType = item.getString("businessType");
            String businessKey = item.getString("businessKey");
            try {
                WeaveTask task = findTodoTask(companyId, userId, businessType, businessKey);
                Map<String, Object> variables = buildFlowVariables(businessType, businessKey, Boolean.TRUE.equals(approved), comment);
                if (Boolean.TRUE.equals(approved)) {
                    taskService.complete(task.getId(), String.valueOf(userId), comment, variables);
                } else {
                    taskService.reject(task.getId(), String.valueOf(userId), comment, variables);
                }
                WeaveInstance instance = runtimeService.getProcessInstance(task.getInstanceId());
                int processStatus = instance == null || instance.getStatus() == null ? 0 : instance.getStatus();
                syncBusinessStatus(businessType, businessKey, Boolean.TRUE.equals(approved), processStatus);
                refreshApprovalMessages(companyId, businessType, businessKey, userId);
                successNum++;
            } catch (Exception e) {
                errorNum++;
                failList.add(e.getMessage());
            }
        }
        result.put("successNum", successNum);
        result.put("errorNum", errorNum);
        result.put("failList", failList);
        return ResultData.success(result);
    }

    @Transactional
    @Override
    public ResultData revokeTask(Long processInstanceId) {
        WeaveInstance instance = runtimeService.getProcessInstance(processInstanceId);
        if (instance == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "无流程信息");
        }
        if (Integer.valueOf(INSTANCE_COMPLETED).equals(instance.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前流程已经结束,不可以撤回");
        }
        if (Integer.valueOf(INSTANCE_TERMINATED).equals(instance.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前流程已终止,不可以撤回");
        }
        runtimeService.terminateProcessInstance(processInstanceId, "流程撤回");
        return ResultData.success(null);
    }

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String businessType, String name) {
        startPage(pageNo, pageSize);
        List<WeaveDefinition> list = repositoryService.listDefinitions(businessType, name);
        return ResultData.success(new PageInfo<>(list));
    }

    @Override
    public ResultData insert(DeployDefinitionCommand command) {
        repositoryService.deployDefinition(command);
        return ResultData.success(null);
    }

    @Override
    public ResultData update(DeployDefinitionCommand command) {
        repositoryService.updateDefinition(command);
        return ResultData.success(null);
    }

    @Override
    public ResultData delete(Long id) {
        repositoryService.deleteDefinition(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryById(Long id, String dataSource) {
        WeaveDefinition definition = repositoryService.getDefinitionById(id);
        if (definition == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "无流程定义");
        }
        return ResultData.success(definition);
    }

    @Override
    public ResultData queryByBusinessType(String businessType) {
        WeaveDefinition definition = repositoryService.getDefinition(businessType);
        if (definition == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "无流程定义");
        }
        return ResultData.success(definition);
    }

    private void startPage(Integer pageNo, Integer pageSize) {
        int current = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        PageHelper.startPage(current, size);
    }

    private JSONArray parseBatch(String batchData) {
        if (StringUtils.isEmpty(batchData)) {
            return new JSONArray();
        }
        return JSONArray.parseArray(batchData);
    }

    private WeaveTask findTodoTask(Long companyId, Long userId, String businessType, String businessKey) {
        List<WeaveTask> tasks = taskService.createTaskQuery()
                .companyId(companyId)
                .taskAssignee(String.valueOf(userId))
                .list();
        for (WeaveTask task : tasks) {
            if (businessType.equals(task.getBusinessType()) && businessKey.equals(task.getBusinessKey())) {
                return task;
            }
        }
        throw new IllegalArgumentException("当前没有审批节点,请检查数据");
    }

    private Map<String, Object> buildFlowVariables(String businessType, String businessKey, boolean approved, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        variables.put("comment", comment);
        if (LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
            LeaveRequest leaveRequest = leaveRequestMapper.selectById(Long.valueOf(businessKey));
            if (leaveRequest != null) {
                variables.put("id", leaveRequest.getId());
                variables.put("companyId", leaveRequest.getCompanyId());
                variables.put("applicantId", leaveRequest.getApplicantId());
                variables.put("leaveType", leaveRequest.getLeaveType());
                variables.put("duration", leaveRequest.getDuration());
                variables.put("reason", leaveRequest.getReason());
            }
        } else if (PaymentRequestService.BUSINESS_TYPE.equals(businessType)) {
            PaymentRequest paymentRequest = paymentRequestMapper.selectById(Long.valueOf(businessKey));
            if (paymentRequest != null) {
                variables.put("id", paymentRequest.getId());
                variables.put("projectId", paymentRequest.getProjectId());
                variables.put("companyId", paymentRequest.getCompanyId());
                variables.put("applicantId", paymentRequest.getApplicantId());
                variables.put("amount", paymentRequest.getAmount());
                variables.put("reason", paymentRequest.getReason());
                variables.put("attachments", paymentRequest.getAttachments());
            }
        } else if (ProjectChangeOrderService.BUSINESS_TYPE.equals(businessType)) {
            ProjectChangeOrder projectChangeOrder = projectChangeOrderMapper.selectById(Long.valueOf(businessKey));
            if (projectChangeOrder != null) {
                variables.put("id", projectChangeOrder.getId());
                variables.put("projectId", projectChangeOrder.getProjectId());
                variables.put("companyId", projectChangeOrder.getCompanyId());
                variables.put("applicantId", projectChangeOrder.getApplicantId());
                variables.put("orderNo", projectChangeOrder.getOrderNo());
                variables.put("orderType", projectChangeOrder.getOrderType());
                variables.put("orderDate", formatDateOnly(projectChangeOrder.getOrderDate()));
                variables.put("amount", projectChangeOrder.getAmount());
                variables.put("reason", projectChangeOrder.getReason());
                variables.put("content", projectChangeOrder.getContent());
                variables.put("remark", projectChangeOrder.getRemark());
                variables.put("attachments", projectChangeOrder.getAttachments());
            }
        }
        return variables;
    }

    private Map<String, Object> toFlowInfo(WeaveInstance instance) {
        Map<String, Object> flowInfo = new LinkedHashMap<>();
        flowInfo.put("id", instance.getId());
        flowInfo.put("companyId", instance.getCompanyId());
        flowInfo.put("userId", instance.getUserId());
        flowInfo.put("name", instance.getName());
        flowInfo.put("businessType", instance.getBusinessType());
        flowInfo.put("businessKey", instance.getBusinessKey());
        flowInfo.put("status", instance.getStatus());
        flowInfo.put("resources", instance.getResources());
        flowInfo.put("variables", instance.getVariables());
        flowInfo.put("startTime", formatTime(instance.getStartTime()));
        flowInfo.put("endTime", formatTime(instance.getEndTime()));
        return flowInfo;
    }

    private Map<String, Object> toProcessInfo(WeaveProcessRecord record) {
        Map<String, Object> processInfo = new LinkedHashMap<>();
        processInfo.put("label", record.getLabel());
        processInfo.put("name", record.getName());
        processInfo.put("nodeStatus", record.getNodeStatus());
        processInfo.put("status", record.getStatus());
        processInfo.put("remark", record.getRemark());
        processInfo.put("startTime", formatTime(record.getStartTime()));
        processInfo.put("endTime", formatTime(record.getEndTime()));
        processInfo.put("consuming", record.getConsuming());
        return processInfo;
    }

    private String formatTime(LocalDateTime time) {
        return time == null ? null : FLOW_TIME_FORMATTER.format(time);
    }

    private String formatDateOnly(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void syncBusinessStatus(String businessType, String businessKey, Boolean approved, int processStatus) {
        if (LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setId(Long.valueOf(businessKey));
            if (processStatus == INSTANCE_COMPLETED) {
                leaveRequest.setStatus(LeaveRequestService.STATUS_APPROVED);
            } else if (!approved && processStatus == INSTANCE_TERMINATED) {
                leaveRequest.setStatus(LeaveRequestService.STATUS_REJECTED);
            } else {
                return;
            }
            leaveRequestMapper.updateById(leaveRequest);
        } else if (PaymentRequestService.BUSINESS_TYPE.equals(businessType)) {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setId(Long.valueOf(businessKey));
            if (processStatus == INSTANCE_COMPLETED) {
                paymentRequest.setStatus(PaymentRequestService.STATUS_APPROVED);
            } else if (!approved && processStatus == INSTANCE_TERMINATED) {
                paymentRequest.setStatus(PaymentRequestService.STATUS_REJECTED);
            } else {
                return;
            }
            paymentRequestMapper.updateById(paymentRequest);
        } else if (ProjectChangeOrderService.BUSINESS_TYPE.equals(businessType)) {
            ProjectChangeOrder projectChangeOrder = new ProjectChangeOrder();
            projectChangeOrder.setId(Long.valueOf(businessKey));
            if (processStatus == INSTANCE_COMPLETED) {
                projectChangeOrder.setStatus(ProjectChangeOrderService.STATUS_APPROVED);
            } else if (!approved && processStatus == INSTANCE_TERMINATED) {
                projectChangeOrder.setStatus(ProjectChangeOrderService.STATUS_REJECTED);
            } else {
                return;
            }
            projectChangeOrderMapper.updateById(projectChangeOrder);
        }
    }

    private void refreshApprovalMessages(Long companyId, String businessType, String businessKey, Long userId) {
        Long id = Long.valueOf(businessKey);
        messageMapper.deleteMessage(businessType, id);
        MessageMeta meta = buildMessageMeta(businessType);
        if (meta == null) {
            return;
        }
        Date now = new Date();
        Set<Long> toUserIds = queryCurrentTaskUserIds(companyId, businessType, businessKey);
        for (Long toUserId : toUserIds) {
            SysMessage message = new SysMessage();
            message.setCompanyId(companyId);
            message.setType(meta.type);
            message.setFromUserId(userId);
            message.setToUserId(toUserId);
            message.setTitle(meta.title);
            message.setBusinessType(businessType);
            message.setBusinessKey(id);
            message.setStatus(0);
            message.setCreatedBy(userId);
            message.setCreatedAt(now);
            message.setUpdatedBy(userId);
            message.setUpdatedAt(now);
            messageMapper.insert(message);
        }
    }

    private Set<Long> queryCurrentTaskUserIds(Long companyId, String businessType, String businessKey) {
        Set<Long> result = new HashSet<>();
        List<String> assignees = messageMapper.queryCurrentTaskAssignees(companyId, businessType, businessKey);
        for (String assignee : assignees) {
            addAssigneeUserIds(result, assignee);
        }
        return result;
    }

    private void addAssigneeUserIds(Set<Long> result, String assignee) {
        if (StringUtils.isEmpty(assignee)) {
            return;
        }
        String[] userIds = assignee.split(",");
        for (String userId : userIds) {
            String text = userId == null ? "" : userId.trim();
            if (StringUtils.isEmpty(text)) {
                continue;
            }
            result.add(Long.valueOf(text));
        }
    }

    private MessageMeta buildMessageMeta(String businessType) {
        if (LeaveRequestService.BUSINESS_TYPE.equals(businessType)) {
            return new MessageMeta(7, "您有一条请假单待审批");
        }
        if (PaymentRequestService.BUSINESS_TYPE.equals(businessType)) {
            return new MessageMeta(8, "您有一条请款单待审批");
        }
        if (ProjectChangeOrderService.BUSINESS_TYPE.equals(businessType)) {
            return new MessageMeta(12, "您有一条签证单待审批");
        }
        return null;
    }

    private static class MessageMeta {
        private final Integer type;
        private final String title;

        private MessageMeta(Integer type, String title) {
            this.type = type;
            this.title = title;
        }
    }

}
