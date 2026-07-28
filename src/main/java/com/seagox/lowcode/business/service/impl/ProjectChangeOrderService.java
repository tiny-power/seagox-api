package com.seagox.lowcode.business.service.impl;

import app.tinybrief.weave.api.RepositoryService;
import app.tinybrief.weave.api.RuntimeService;
import app.tinybrief.weave.api.TaskService;
import app.tinybrief.weave.api.dto.WeaveDefinition;
import app.tinybrief.weave.api.dto.WeaveInstance;
import app.tinybrief.weave.api.dto.WeaveTask;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.ProjectChangeOrder;
import com.seagox.lowcode.business.entity.ProjectChangeOrderItem;
import com.seagox.lowcode.business.mapper.ProjectChangeOrderItemMapper;
import com.seagox.lowcode.business.mapper.ProjectChangeOrderMapper;
import com.seagox.lowcode.business.service.IProjectChangeOrderService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.entity.SysProcessDraft;
import com.seagox.lowcode.system.mapper.AccountMapper;
import com.seagox.lowcode.system.mapper.ProcessDraftMapper;
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
 * 签证单
 */
@Service
public class ProjectChangeOrderService implements IProjectChangeOrderService {

    /**
     * 签证单流程业务类型
     */
    public static final String BUSINESS_TYPE = "project_change_order";
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
     * 签证单数据访问对象
     */
    @Autowired
    private ProjectChangeOrderMapper projectChangeOrderMapper;

    /**
     * 签证单工程量明细数据访问对象
     */
    @Autowired
    private ProjectChangeOrderItemMapper projectChangeOrderItemMapper;

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
     * 分页查询签证单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = projectChangeOrderMapper.queryProjectChangeOrders(params);
        fillPendingReviewer(list, params);
        MapDateFormatUtils.formatDateValues(list);
        formatOrderDate(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询签证单详情
     */
    @Override
    public ResultData queryById(Long id, Map<String, Object> params) {
        Map<String, Object> data = projectChangeOrderMapper.queryProjectChangeOrderById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单不存在");
        }
        data.put("items", projectChangeOrderItemMapper.queryByOrderId(id));
        fillPendingReviewer(data, params);
        MapDateFormatUtils.formatDateValues(data);
        formatOrderDate(data);
        return ResultData.success(data);
    }

    /**
     * 新增签证单
     */
    @Transactional
    @Override
    public ResultData insert(ProjectChangeOrder projectChangeOrder) {
        if (StringUtils.isEmpty(projectChangeOrder.getOrderNo())) {
            projectChangeOrder.setOrderNo(buildOrderNo());
        }
        ResultData verifyResult = verify(projectChangeOrder);
        if (verifyResult != null) {
            return verifyResult;
        }
        Date now = new Date();
        projectChangeOrder.setStatus(STATUS_DRAFT);
        projectChangeOrder.setCreatedAt(now);
        projectChangeOrder.setUpdatedAt(now);
        projectChangeOrderMapper.insert(projectChangeOrder);
        saveItems(projectChangeOrder);
        saveProcessDraft(projectChangeOrder);
        return ResultData.success(projectChangeOrder.getId());
    }

    /**
     * 修改签证单
     */
    @Transactional
    @Override
    public ResultData update(ProjectChangeOrder projectChangeOrder) {
        ProjectChangeOrder original = projectChangeOrderMapper.selectById(projectChangeOrder.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的签证单不可以修改");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的签证单不可以修改");
        }
        ResultData verifyResult = verify(projectChangeOrder);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (StringUtils.isEmpty(projectChangeOrder.getOrderNo())) {
            projectChangeOrder.setOrderNo(original.getOrderNo());
        }
        projectChangeOrder.setStatus(original.getStatus());
        projectChangeOrder.setCreatedBy(original.getCreatedBy());
        projectChangeOrder.setCreatedAt(original.getCreatedAt());
        projectChangeOrder.setUpdatedAt(new Date());
        projectChangeOrderMapper.updateById(projectChangeOrder);
        saveItems(projectChangeOrder);
        if (Integer.valueOf(STATUS_DRAFT).equals(projectChangeOrder.getStatus())) {
            saveProcessDraft(projectChangeOrder);
        } else {
            deleteProcessDraft(projectChangeOrder);
        }
        return ResultData.success(null);
    }

    /**
     * 删除签证单
     */
    @Transactional
    @Override
    public ResultData delete(Long id) {
        ProjectChangeOrder projectChangeOrder = projectChangeOrderMapper.selectById(id);
        if (projectChangeOrder == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(projectChangeOrder.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "审批中的签证单不可以删除");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(projectChangeOrder.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已通过的签证单不可以删除");
        }
        clearProcess(projectChangeOrder.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        deleteProcessDraft(projectChangeOrder);
        deleteItems(id);
        projectChangeOrderMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 提交签证单审批
     */
    @Transactional
    @Override
    public ResultData submit(Long id) {
        ProjectChangeOrder projectChangeOrder = projectChangeOrderMapper.selectById(id);
        if (projectChangeOrder == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单不存在");
        }
        if (Integer.valueOf(STATUS_APPROVING).equals(projectChangeOrder.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单正在审批中");
        }
        if (Integer.valueOf(STATUS_APPROVED).equals(projectChangeOrder.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单已经审批通过");
        }
        ResultData verifyResult = verify(projectChangeOrder);
        if (verifyResult != null) {
            return verifyResult;
        }
        WeaveDefinition definition = repositoryService.getDefinition(BUSINESS_TYPE);
        if (definition == null || StringUtils.isEmpty(definition.getResources())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请先维护签证单流程定义");
        }
        clearProcess(projectChangeOrder.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        Long processInstanceId = runtimeService.startProcessInstanceByKey(BUSINESS_TYPE, String.valueOf(id),
                projectChangeOrder.getCompanyId(), projectChangeOrder.getApplicantId(), buildFlowTitle(projectChangeOrder),
                buildFlowVariables(projectChangeOrder));
        WeaveInstance processInstance = runtimeService.getProcessInstance(processInstanceId);
        int processStatus = processInstance == null || processInstance.getStatus() == null ? 0 : processInstance.getStatus();
        projectChangeOrder.setStatus(processStatus == 1 ? STATUS_APPROVED : STATUS_APPROVING);
        projectChangeOrder.setUpdatedAt(new Date());
        projectChangeOrderMapper.updateById(projectChangeOrder);
        deleteProcessDraft(projectChangeOrder);
        return ResultData.success(null);
    }

    /**
     * 撤销签证单审批
     */
    @Transactional
    @Override
    public ResultData cancel(Long id) {
        ProjectChangeOrder projectChangeOrder = projectChangeOrderMapper.selectById(id);
        if (projectChangeOrder == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "签证单不存在");
        }
        if (!Integer.valueOf(STATUS_APPROVING).equals(projectChangeOrder.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有审批中的签证单可以撤销");
        }
        terminateProcess(projectChangeOrder.getCompanyId(), BUSINESS_TYPE, String.valueOf(id));
        projectChangeOrder.setStatus(STATUS_CANCELED);
        projectChangeOrder.setUpdatedAt(new Date());
        projectChangeOrderMapper.updateById(projectChangeOrder);
        return ResultData.success(null);
    }

    /**
     * 校验签证单
     */
    private ResultData verify(ProjectChangeOrder projectChangeOrder) {
        if (projectChangeOrder.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择项目");
        }
        if (projectChangeOrder.getCompanyId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择公司");
        }
        if (projectChangeOrder.getApplicantId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择申请人");
        }
        if (projectChangeOrder.getOrderType() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择签证类型");
        }
        if (projectChangeOrder.getOrderDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择签证日期");
        }
        if (projectChangeOrder.getAmount() == null || projectChangeOrder.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写签证金额");
        }
        if (StringUtils.isEmpty(projectChangeOrder.getReason())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写签证原因");
        }
        List<ProjectChangeOrderItem> items = parseItems(projectChangeOrder);
        if (items.isEmpty()) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请填写工程量明细");
        }
        for (ProjectChangeOrderItem item : items) {
            if (StringUtils.isEmpty(item.getItemName())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请填写工程量名称");
            }
            if (StringUtils.isEmpty(item.getUnit())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请填写工程量单位");
            }
            if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请填写工程量数量");
            }
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "请填写工程量单价");
            }
            if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                item.setAmount(item.getQuantity().multiply(item.getUnitPrice()));
            }
        }
        return null;
    }

    /**
     * 构建签证流程标题
     */
    private String buildFlowTitle(ProjectChangeOrder projectChangeOrder) {
        if (!StringUtils.isEmpty(projectChangeOrder.getOrderNo())) {
            return "签证单-" + projectChangeOrder.getOrderNo();
        }
        SysAccount applicant = accountMapper.selectById(projectChangeOrder.getApplicantId());
        if (applicant != null && !StringUtils.isEmpty(applicant.getName())) {
            return "签证单-" + applicant.getName();
        }
        return "签证单-" + projectChangeOrder.getApplicantId();
    }

    /**
     * 生成签证编号
     */
    private String buildOrderNo() {
        return "QZ" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 构建签证流程变量
     */
    private Map<String, Object> buildFlowVariables(ProjectChangeOrder projectChangeOrder) {
        Map<String, Object> variables = new HashMap<>();
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
        variables.put("items", projectChangeOrderItemMapper.queryByOrderId(projectChangeOrder.getId()));
        return variables;
    }

    /**
     * 保存工程量明细
     */
    private void saveItems(ProjectChangeOrder projectChangeOrder) {
        deleteItems(projectChangeOrder.getId());
        List<ProjectChangeOrderItem> items = parseItems(projectChangeOrder);
        Date now = new Date();
        for (int index = 0; index < items.size(); index++) {
            ProjectChangeOrderItem item = items.get(index);
            item.setId(null);
            item.setOrderId(projectChangeOrder.getId());
            item.setSort(index + 1);
            item.setCreatedAt(now);
            item.setUpdatedAt(now);
            if (item.getAmount() == null) {
                item.setAmount(item.getQuantity().multiply(item.getUnitPrice()));
            }
            projectChangeOrderItemMapper.insert(item);
        }
    }

    /**
     * 删除工程量明细
     */
    private void deleteItems(Long orderId) {
        projectChangeOrderItemMapper.delete(new LambdaQueryWrapper<ProjectChangeOrderItem>()
                .eq(ProjectChangeOrderItem::getOrderId, orderId));
    }

    /**
     * 解析工程量明细
     */
    private List<ProjectChangeOrderItem> parseItems(ProjectChangeOrder projectChangeOrder) {
        if (projectChangeOrder.getItems() != null) {
            return projectChangeOrder.getItems();
        }
        if (StringUtils.isEmpty(projectChangeOrder.getItemData())) {
            if (projectChangeOrder.getId() != null) {
                return projectChangeOrderItemMapper.queryByOrderId(projectChangeOrder.getId());
            }
            return java.util.Collections.emptyList();
        }
        return JSON.parseArray(projectChangeOrder.getItemData(), ProjectChangeOrderItem.class);
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
     * 格式化日期
     */
    private String formatDateOnly(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 签证日期只保留年月日
     */
    private void formatOrderDate(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        for (Map<String, Object> item : list) {
            formatOrderDate(item);
        }
    }

    /**
     * 签证日期只保留年月日
     */
    private void formatOrderDate(Map<String, Object> data) {
        Object value = data == null ? null : data.get("orderDate");
        if (value == null) {
            return;
        }
        String text = String.valueOf(value);
        data.put("orderDate", text.length() > 10 ? text.substring(0, 10) : text);
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
     * 保存流程待发事项
     */
    private void saveProcessDraft(ProjectChangeOrder projectChangeOrder) {
        if (projectChangeOrder == null || projectChangeOrder.getId() == null
                || !Integer.valueOf(STATUS_DRAFT).equals(projectChangeOrder.getStatus())) {
            return;
        }
        deleteProcessDraft(projectChangeOrder);
        Date now = new Date();
        SysProcessDraft draft = new SysProcessDraft();
        draft.setCompanyId(projectChangeOrder.getCompanyId());
        draft.setUserId(projectChangeOrder.getApplicantId());
        draft.setBusinessType(BUSINESS_TYPE);
        draft.setBusinessId(projectChangeOrder.getId());
        draft.setBusinessTitle(buildFlowTitle(projectChangeOrder));
        draft.setSummary(buildDraftSummary(projectChangeOrder));
        draft.setCreatedBy(projectChangeOrder.getApplicantId());
        draft.setCreatedAt(now);
        draft.setUpdatedBy(projectChangeOrder.getApplicantId());
        draft.setUpdatedAt(now);
        processDraftMapper.insert(draft);
    }

    /**
     * 删除流程待发事项
     */
    private void deleteProcessDraft(ProjectChangeOrder projectChangeOrder) {
        if (projectChangeOrder == null || projectChangeOrder.getId() == null || projectChangeOrder.getCompanyId() == null) {
            return;
        }
        processDraftMapper.delete(new LambdaQueryWrapper<SysProcessDraft>()
                .eq(SysProcessDraft::getCompanyId, projectChangeOrder.getCompanyId())
                .eq(SysProcessDraft::getBusinessType, BUSINESS_TYPE)
                .eq(SysProcessDraft::getBusinessId, projectChangeOrder.getId()));
    }

    /**
     * 构建待发摘要
     */
    private String buildDraftSummary(ProjectChangeOrder projectChangeOrder) {
        return limitSummary("签证编号：" + projectChangeOrder.getOrderNo() + "，金额：" + projectChangeOrder.getAmount()
                + "，原因：" + projectChangeOrder.getReason());
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
     * 标记当前登录用户是否为该签证单当前待审批人
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
     * 标记当前登录用户是否为该签证单当前待审批人
     */
    private void fillPendingReviewer(Map<String, Object> item, Map<String, Object> params) {
        Long companyId = toLong(params == null ? null : params.get("companyId"));
        Long userId = toLong(params == null ? null : params.get("userId"));
        Set<String> todoKeys = queryTodoPaymentKeys(companyId, userId);
        fillPendingReviewer(item, todoKeys);
    }

    /**
     * 标记当前登录用户是否为该签证单当前待审批人
     */
    private void fillPendingReviewer(Map<String, Object> item, Set<String> todoKeys) {
        Object id = item.get("id");
        item.put("pendingReviewer", id != null && todoKeys.contains(String.valueOf(id)));
    }

    /**
     * 查询当前用户待审批签证单
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
