package com.seagox.lowcode.business.service.impl;

import app.tinybrief.weave.api.RepositoryService;
import app.tinybrief.weave.api.RuntimeService;
import app.tinybrief.weave.api.TaskService;
import app.tinybrief.weave.api.dto.WeaveDefinition;
import app.tinybrief.weave.api.dto.WeaveInstance;
import app.tinybrief.weave.api.dto.WeaveTask;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.business.mapper.LeaveRequestMapper;
import com.seagox.lowcode.business.service.ILeaveRequestService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.entity.SysProcessDraft;
import com.seagox.lowcode.system.mapper.AccountMapper;
import com.seagox.lowcode.system.mapper.ProcessDraftMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假单服务实现
 */
@Service
public class LeaveRequestService implements ILeaveRequestService {

    /**
     * 请假单流程业务类型
     */
    public static final String BUSINESS_TYPE = "leave_request";
    /**
     * 草稿状态
     */
    public static final int STATUS_DRAFT = 0;
    /**
     * 审批中状态
     */
    public static final int STATUS_APPROVING = 1;
    /**
     * 已撤销状态
     */
    public static final int STATUS_CANCELED = 2;
    /**
     * 已通过状态
     */
    public static final int STATUS_APPROVED = 3;
    /**
     * 已驳回状态
     */
    public static final int STATUS_REJECTED = 4;

    /**
     * 请假单数据访问对象
     */
    @Autowired
    private LeaveRequestMapper leaveRequestMapper;

    /**
     * 用户数据访问对象
     */
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 流程待发事项数据访问对象
     */
    @Autowired
    private ProcessDraftMapper processDraftMapper;

    /**
     * 流程定义服务
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程运行服务
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程任务服务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 分页查询请假单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime, Long userId) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = leaveRequestMapper.queryByPage(companyId, applicantId, applicantName,
                leaveType, status, startTime, endTime);
        if (userId != null) {
            Map<String, String> todoNodeMap = queryTodoNodeMap(companyId, userId);
            for (Map<String, Object> item : list) {
                boolean pendingReviewer = todoNodeMap.containsKey(String.valueOf(item.get("id")));
                item.put("role", pendingReviewer ? "reviewer" : "applicant");
                item.put("pendingReviewer", pendingReviewer);
            }
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    /**
     * 查询请假单详情
     */
    @Override
    public ResultData queryById(Long id, Long userId) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        SysAccount applicant = accountMapper.selectById(leaveRequest.getApplicantId());
        Map<String, Object> data = new HashMap<>();
        data.put("id", leaveRequest.getId());
        data.put("companyId", leaveRequest.getCompanyId());
        data.put("applicantId", leaveRequest.getApplicantId());
        data.put("applicantName", applicant == null ? "" : applicant.getName());
        data.put("leaveType", leaveRequest.getLeaveType());
        data.put("startTime", formatDate(leaveRequest.getStartTime()));
        data.put("endTime", formatDate(leaveRequest.getEndTime()));
        data.put("duration", leaveRequest.getDuration());
        data.put("reason", leaveRequest.getReason());
        data.put("attachments", leaveRequest.getAttachments());
        data.put("status", leaveRequest.getStatus());
        data.put("submitTime", formatDate(leaveRequest.getSubmitTime()));
        data.put("createdAt", formatDate(leaveRequest.getCreatedAt()));
        if (userId != null) {
            Map<String, String> todoNodeMap = queryTodoNodeMap(leaveRequest.getCompanyId(), userId);
            boolean applicantRole = userId.equals(leaveRequest.getApplicantId());
            boolean reviewer = todoNodeMap.containsKey(String.valueOf(id));
            if (!applicantRole && !reviewer) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "无权查看请假单");
            }
            data.put("role", applicantRole ? "applicant" : "reviewer");
            data.put("pendingReviewer", reviewer);
            data.put("currentNode", todoNodeMap.get(String.valueOf(id)));
        }
        return ResultData.success(data);
    }

    /**
     * 新增请假单
     */
    @Transactional
    @Override
    public ResultData insert(LeaveRequest leaveRequest, Long userId) {
        if (leaveRequest.getApplicantId() == null) {
            leaveRequest.setApplicantId(userId);
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(STATUS_DRAFT);
        leaveRequestMapper.insert(leaveRequest);
        saveProcessDraft(leaveRequest);
        return ResultData.success(leaveRequest.getId());
    }

    /**
     * 修改请假单
     */
    @Transactional
    @Override
    public ResultData update(LeaveRequest leaveRequest, Long userId) {
        LeaveRequest original = leaveRequestMapper.selectById(leaveRequest.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (userId != null) {
            ResultData ownerResult = verifyOwner(original, userId);
            if (ownerResult != null) {
                return ownerResult;
            }
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请假单不可以修改");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请假单不可以修改");
        }
        if (userId != null) {
            leaveRequest.setCompanyId(original.getCompanyId());
            leaveRequest.setApplicantId(userId);
        }
        ResultData verifyResult = verify(leaveRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        leaveRequest.setStatus(original.getStatus());
        leaveRequestMapper.updateById(leaveRequest);
        if (Integer.valueOf(STATUS_DRAFT).equals(leaveRequest.getStatus())) {
            saveProcessDraft(leaveRequest);
        } else {
            deleteProcessDraft(leaveRequest);
        }
        return ResultData.success(null);
    }

    /**
     * 提交请假单审批
     */
    @Transactional
    @Override
    public ResultData submit(LeaveRequest leaveRequest, Long userId) {
        if (leaveRequest.getId() == null) {
            if (leaveRequest.getApplicantId() == null) {
                leaveRequest.setApplicantId(userId);
            }
            ResultData insertResult = insert(leaveRequest, userId);
            if (insertResult.getCode() != ResultCode.SUCCESS.getCode()) {
                return insertResult;
            }
            leaveRequest.setId((Long) insertResult.getData());
        } else {
            LeaveRequest original = leaveRequestMapper.selectById(leaveRequest.getId());
            if (userId != null) {
                ResultData ownerResult = verifyOwner(original, userId);
                if (ownerResult != null) {
                    return ownerResult;
                }
            }
            if (original == null) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
            }
            if (Integer.valueOf(STATUS_APPROVING).equals(original.getStatus())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请假单正在审批中");
            }
            if (Integer.valueOf(STATUS_APPROVED).equals(original.getStatus())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请假单已经审批通过");
            }
            leaveRequest.setCompanyId(original.getCompanyId());
            leaveRequest.setApplicantId(userId == null ? original.getApplicantId() : userId);
            leaveRequest.setStatus(original.getStatus());
            ResultData updateResult = update(leaveRequest, userId);
            if (updateResult.getCode() != ResultCode.SUCCESS.getCode()) {
                return updateResult;
            }
        }
        return submit(leaveRequest.getId());
    }

    /**
     * 删除请假单
     */
    @Transactional
    @Override
    public ResultData delete(Long id, Long userId) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (userId != null) {
            ResultData ownerResult = verifyOwner(leaveRequest, userId);
            if (ownerResult != null) {
                return ownerResult;
            }
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请假单不可以删除");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请假单不可以删除");
        }
        clearProcess(leaveRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        deleteProcessDraft(leaveRequest);
        leaveRequestMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 提交请假单审批
     */
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
        WeaveDefinition definition = repositoryService.getDefinition(BUSINESS_TYPE);
        if (definition == null || StringUtils.isEmpty(definition.getResources())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请先维护请假单流程定义");
        }
        clearProcess(leaveRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        Long processInstanceId = runtimeService.startProcessInstanceByKey(BUSINESS_TYPE, String.valueOf(id),
                leaveRequest.getCompanyId(), leaveRequest.getApplicantId(), buildFlowTitle(leaveRequest), buildFlowVariables(leaveRequest));
        WeaveInstance processInstance = runtimeService.getProcessInstance(processInstanceId);
        int processStatus = processInstance == null || processInstance.getStatus() == null ? 0 : processInstance.getStatus();
        leaveRequest.setStatus(processStatus == 1 ? STATUS_APPROVED : STATUS_APPROVING);
        leaveRequest.setSubmitTime(new Date());
        leaveRequestMapper.updateById(leaveRequest);
        deleteProcessDraft(leaveRequest);
        return ResultData.success(null);
    }

    /**
     * 撤销请假单审批
     */
    @Transactional
    @Override
    public ResultData cancel(Long id, Long userId) {
        LeaveRequest leaveRequest = leaveRequestMapper.selectById(id);
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (userId != null) {
            ResultData ownerResult = verifyOwner(leaveRequest, userId);
            if (ownerResult != null) {
                return ownerResult;
            }
        }
        if (!Integer.valueOf(STATUS_APPROVING).equals(leaveRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有审批中的请假单可以撤销");
        }
        terminateProcess(leaveRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        leaveRequest.setStatus(STATUS_CANCELED);
        leaveRequestMapper.updateById(leaveRequest);
        return ResultData.success(null);
    }

    /**
     * 保存流程待发事项
     */
    private void saveProcessDraft(LeaveRequest leaveRequest) {
        if (leaveRequest == null || leaveRequest.getId() == null
                || !Integer.valueOf(STATUS_DRAFT).equals(leaveRequest.getStatus())) {
            return;
        }
        deleteProcessDraft(leaveRequest);
        Date now = new Date();
        SysProcessDraft draft = new SysProcessDraft();
        draft.setCompanyId(leaveRequest.getCompanyId());
        draft.setUserId(leaveRequest.getApplicantId());
        draft.setBusinessType(BUSINESS_TYPE);
        draft.setBusinessId(leaveRequest.getId());
        draft.setBusinessTitle(buildFlowTitle(leaveRequest));
        draft.setSummary(buildDraftSummary(leaveRequest));
        draft.setCreatedBy(leaveRequest.getApplicantId());
        draft.setCreatedAt(now);
        draft.setUpdatedBy(leaveRequest.getApplicantId());
        draft.setUpdatedAt(now);
        processDraftMapper.insert(draft);
    }

    /**
     * 删除流程待发事项
     */
    private void deleteProcessDraft(LeaveRequest leaveRequest) {
        if (leaveRequest == null || leaveRequest.getId() == null || leaveRequest.getCompanyId() == null) {
            return;
        }
        processDraftMapper.delete(new LambdaQueryWrapper<SysProcessDraft>()
                .eq(SysProcessDraft::getCompanyId, leaveRequest.getCompanyId())
                .eq(SysProcessDraft::getBusinessType, BUSINESS_TYPE)
                .eq(SysProcessDraft::getBusinessId, leaveRequest.getId()));
    }

    /**
     * 构建待发摘要
     */
    private String buildDraftSummary(LeaveRequest leaveRequest) {
        return limitSummary("请假时长：" + leaveRequest.getDuration() + "，事由：" + leaveRequest.getReason());
    }

    /**
     * 限制待发摘要长度
     */
    private String limitSummary(String summary) {
        if (summary == null || summary.length() <= 500) {
            return summary;
        }
        return summary.substring(0, 500);
    }

    /**
     * 校验请假单
     */
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

    /**
     * 校验请假单归属
     */
    private ResultData verifyOwner(LeaveRequest leaveRequest, Long userId) {
        if (leaveRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请假单不存在");
        }
        if (userId == null || !userId.equals(leaveRequest.getApplicantId())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "无权操作请假单");
        }
        return null;
    }

    /**
     * 查询当前用户待审批请假单节点
     */
    private Map<String, String> queryTodoNodeMap(Long companyId, Long userId) {
        Map<String, String> result = new HashMap<>();
        if (companyId == null || userId == null) {
            return result;
        }
        List<WeaveTask> tasks = taskService.createTaskQuery()
                .companyId(companyId)
                .taskAssignee(String.valueOf(userId))
                .list();
        for (WeaveTask task : tasks) {
            if (BUSINESS_TYPE.equals(task.getBusinessType())) {
                result.put(task.getBusinessKey(), "待审批");
            }
        }
        return result;
    }

    /**
     * 手动分页
     */
    private PageInfo<Map<String, Object>> page(List<Map<String, Object>> list, Integer pageNo, Integer pageSize) {
        int current = pageNo == null || pageNo < 1 ? 1 : pageNo;
        int size = pageSize == null || pageSize < 1 ? 10 : pageSize;
        int fromIndex = Math.min((current - 1) * size, list.size());
        int toIndex = Math.min(fromIndex + size, list.size());
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list.subList(fromIndex, toIndex));
        pageInfo.setPageNum(current);
        pageInfo.setPageSize(size);
        pageInfo.setTotal(list.size());
        pageInfo.setPages((list.size() + size - 1) / size);
        return pageInfo;
    }

    /**
     * 转换Long
     */
    private Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    /**
     * 构建请假流程标题
     */
    private String buildFlowTitle(LeaveRequest leaveRequest) {
        SysAccount applicant = accountMapper.selectById(leaveRequest.getApplicantId());
        if (applicant != null && !StringUtils.isEmpty(applicant.getName())) {
            return "请假单-" + applicant.getName();
        }
        return "请假单-" + leaveRequest.getApplicantId();
    }

    /**
     * 构建请假流程变量
     */
    private Map<String, Object> buildFlowVariables(LeaveRequest leaveRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", leaveRequest.getId());
        variables.put("companyId", leaveRequest.getCompanyId());
        variables.put("applicantId", leaveRequest.getApplicantId());
        variables.put("leaveType", leaveRequest.getLeaveType());
        variables.put("duration", leaveRequest.getDuration());
        variables.put("reason", leaveRequest.getReason());
        variables.put("startTime", formatDate(leaveRequest.getStartTime()));
        variables.put("endTime", formatDate(leaveRequest.getEndTime()));
        return variables;
    }

    /**
     * 格式化日期时间
     */
    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 清理已有流程实例
     */
    private void clearProcess(Long companyId, String businessType, String businessKey) {
        runtimeService.deleteProcessInstanceByBusinessKey(businessType, businessKey, companyId);
    }

    /**
     * 终止流程实例
     */
    private void terminateProcess(Long companyId, String businessType, String businessKey) {
        runtimeService.terminateProcessInstanceByBusinessKey(businessType, businessKey, companyId, "流程撤销");
    }

}
