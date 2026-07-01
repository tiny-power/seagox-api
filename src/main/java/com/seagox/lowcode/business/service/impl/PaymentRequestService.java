package com.seagox.lowcode.business.service.impl;

import app.tinybrief.weave.api.RepositoryService;
import app.tinybrief.weave.api.RuntimeService;
import app.tinybrief.weave.api.TaskService;
import app.tinybrief.weave.api.dto.WeaveDefinition;
import app.tinybrief.weave.api.dto.WeaveInstance;
import app.tinybrief.weave.api.dto.WeaveTask;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.PaymentRequest;
import com.seagox.lowcode.business.mapper.PaymentRequestMapper;
import com.seagox.lowcode.business.service.IPaymentRequestService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.mapper.AccountMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 请款单
 */
@Service
public class PaymentRequestService implements IPaymentRequestService {

    /**
     * 请款单流程业务类型
     */
    public static final String BUSINESS_TYPE = "payment_request";
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
     * 请款单数据访问对象
     */
    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    /**
     * 用户数据访问对象
     */
    @Autowired
    private AccountMapper accountMapper;

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
     * 分页查询请款单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = paymentRequestMapper.queryPaymentRequests(params);
        fillPendingReviewer(list, params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询请款单详情
     */
    @Override
    public ResultData queryById(Long id, Map<String, Object> params) {
        Map<String, Object> data = paymentRequestMapper.queryPaymentRequestById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        fillPendingReviewer(data, params);
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增请款单
     */
    @Override
    public ResultData insert(PaymentRequest paymentRequest) {
        ResultData verifyResult = verify(paymentRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        Date now = new Date();
        paymentRequest.setStatus(STATUS_DRAFT);
        paymentRequest.setSubmitTime(null);
        paymentRequest.setCreatedAt(now);
        paymentRequest.setUpdatedAt(now);
        paymentRequestMapper.insert(paymentRequest);
        return ResultData.success(paymentRequest.getId());
    }

    /**
     * 修改请款单
     */
    @Override
    public ResultData update(PaymentRequest paymentRequest) {
        PaymentRequest original = paymentRequestMapper.selectById(paymentRequest.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请款单不可以修改");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请款单不可以修改");
        }
        ResultData verifyResult = verify(paymentRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        paymentRequest.setStatus(original.getStatus());
        paymentRequest.setSubmitTime(original.getSubmitTime());
        paymentRequest.setCreatedBy(original.getCreatedBy());
        paymentRequest.setCreatedAt(original.getCreatedAt());
        paymentRequest.setUpdatedAt(new Date());
        paymentRequestMapper.updateById(paymentRequest);
        return ResultData.success(null);
    }

    /**
     * 删除请款单
     */
    @Transactional
    @Override
    public ResultData delete(Long id) {
        PaymentRequest paymentRequest = paymentRequestMapper.selectById(id);
        if (paymentRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(paymentRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的请款单不可以删除");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(paymentRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的请款单不可以删除");
        }
        clearProcess(paymentRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        paymentRequestMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 提交请款单审批
     */
    @Transactional
    @Override
    public ResultData submit(Long id) {
        PaymentRequest paymentRequest = paymentRequestMapper.selectById(id);
        if (paymentRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(paymentRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单正在审批中");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(paymentRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单已经审批通过");
        }
        ResultData verifyResult = verify(paymentRequest);
        if (verifyResult != null) {
            return verifyResult;
        }
        WeaveDefinition definition = repositoryService.getDefinition(BUSINESS_TYPE);
        if (definition == null || StringUtils.isEmpty(definition.getResources())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请先维护请款单流程定义");
        }
        clearProcess(paymentRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        Long processInstanceId = runtimeService.startProcessInstanceByKey(BUSINESS_TYPE, String.valueOf(id),
                paymentRequest.getCompanyId(), paymentRequest.getApplicantId(), buildFlowTitle(paymentRequest),
                buildFlowVariables(paymentRequest));
        WeaveInstance processInstance = runtimeService.getProcessInstance(processInstanceId);
        int processStatus = processInstance == null || processInstance.getStatus() == null ? 0 : processInstance.getStatus();
        paymentRequest.setStatus(processStatus == 1 ? STATUS_APPROVED : STATUS_APPROVING);
        paymentRequest.setSubmitTime(new Date());
        paymentRequest.setUpdatedAt(new Date());
        paymentRequestMapper.updateById(paymentRequest);
        return ResultData.success(null);
    }

    /**
     * 撤销请款单审批
     */
    @Transactional
    @Override
    public ResultData cancel(Long id) {
        PaymentRequest paymentRequest = paymentRequestMapper.selectById(id);
        if (paymentRequest == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        if (!Integer.valueOf(STATUS_APPROVING).equals(paymentRequest.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有审批中的请款单可以撤销");
        }
        terminateProcess(paymentRequest.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        paymentRequest.setStatus(STATUS_CANCELED);
        paymentRequest.setUpdatedAt(new Date());
        paymentRequestMapper.updateById(paymentRequest);
        return ResultData.success(null);
    }

    /**
     * 校验请款单
     */
    private ResultData verify(PaymentRequest paymentRequest) {
        if (paymentRequest.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (paymentRequest.getCompanyId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择公司");
        }
        if (paymentRequest.getApplicantId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择申请人");
        }
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请款金额");
        }
        if (StringUtils.isEmpty(paymentRequest.getReason())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写请款事由");
        }
        return null;
    }

    /**
     * 构建请款流程标题
     */
    private String buildFlowTitle(PaymentRequest paymentRequest) {
        SysAccount applicant = accountMapper.selectById(paymentRequest.getApplicantId());
        if (applicant != null && !StringUtils.isEmpty(applicant.getName())) {
            return "请款单-" + applicant.getName();
        }
        return "请款单-" + paymentRequest.getApplicantId();
    }

    /**
     * 构建请款流程变量
     */
    private Map<String, Object> buildFlowVariables(PaymentRequest paymentRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", paymentRequest.getId());
        variables.put("projectId", paymentRequest.getProjectId());
        variables.put("companyId", paymentRequest.getCompanyId());
        variables.put("applicantId", paymentRequest.getApplicantId());
        variables.put("amount", paymentRequest.getAmount());
        variables.put("reason", paymentRequest.getReason());
        variables.put("attachments", paymentRequest.getAttachments());
        variables.put("submitTime", formatDate(paymentRequest.getSubmitTime()));
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

    /**
     * 标记当前登录用户是否为该请款单当前待审批人
     */
    private void fillPendingReviewer(List<Map<String, Object>> list, Map<String, Object> params) {
        Long companyId = toLong(params == null ? null : params.get("companyId"));
        Long userId = toLong(params == null ? null : params.get("userId"));
        Set<String> todoKeys = queryTodoPaymentKeys(companyId, userId);
        for (Map<String, Object> item : list) {
            fillPendingReviewer(item, todoKeys);
        }
    }

    /**
     * 标记当前登录用户是否为该请款单当前待审批人
     */
    private void fillPendingReviewer(Map<String, Object> item, Map<String, Object> params) {
        Long companyId = toLong(params == null ? null : params.get("companyId"));
        Long userId = toLong(params == null ? null : params.get("userId"));
        Set<String> todoKeys = queryTodoPaymentKeys(companyId, userId);
        fillPendingReviewer(item, todoKeys);
    }

    /**
     * 标记当前登录用户是否为该请款单当前待审批人
     */
    private void fillPendingReviewer(Map<String, Object> item, Set<String> todoKeys) {
        Object id = item.get("id");
        item.put("pendingReviewer", id != null && todoKeys.contains(String.valueOf(id)));
    }

    /**
     * 查询当前用户待审批请款单
     */
    private Set<String> queryTodoPaymentKeys(Long companyId, Long userId) {
        Set<String> result = new HashSet<>();
        if (companyId == null || userId == null) {
            return result;
        }
        List<WeaveTask> tasks = taskService.createTaskQuery()
                .companyId(companyId)
                .taskAssignee(String.valueOf(userId))
                .list();
        for (WeaveTask task : tasks) {
            if (BUSINESS_TYPE.equals(task.getBusinessType())) {
                result.add(task.getBusinessKey());
            }
        }
        return result;
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
        String text = String.valueOf(value);
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return Long.valueOf(text);
    }
}
