package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.entity.OperationLog;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.mapper.OperationLogMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IIssueTicketService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.mapper.AccountMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 问题单
 */
@Service
public class IssueTicketService implements IIssueTicketService {

    /**
     * 激活
     */
    public static final int STATUS_ACTIVE = 1;
    /**
     * 已解决
     */
    public static final int STATUS_RESOLVED = 2;
    /**
     * 已关闭
     */
    public static final int STATUS_CLOSED = 3;
    /**
     * 复验通过
     */
    public static final int REVIEW_PASS = 1;
    /**
     * 复验不通过
     */
    public static final int REVIEW_REJECT = 2;

    private static final String OBJECT_TYPE_ISSUE = "issue";
    private static final String ACTION_ACTIVATED = "activated";
    private static final String ACTION_ASSIGNED = "assigned";
    private static final String ACTION_EDITED = "edited";
    private static final String ACTION_RESOLVED = "resolved";
    private static final String ACTION_CLOSED = "closed";

    /**
     * 问题单数据访问对象
     */
    @Autowired
    private IssueTicketMapper issueTicketMapper;

    /**
     * 操作记录数据访问对象
     */
    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 账号数据访问对象
     */
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 分页查询问题单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = issueTicketMapper.queryIssueTickets(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询问题单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = issueTicketMapper.queryIssueTicketById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        List<Map<String, Object>> operationLogs = operationLogMapper.queryByObject(OBJECT_TYPE_ISSUE, id);
        MapDateFormatUtils.formatDateValues(operationLogs);
        fillCompatibleFields(data, operationLogs);
        data.put("operationLogs", operationLogs);
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增问题单
     */
    @Override
    public ResultData insert(IssueTicket issueTicket, Long userId) {
        ResultData verifyResult = verifyBase(issueTicket);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(issueTicket.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        issueTicket.setStatus(STATUS_ACTIVE);
        issueTicket.setConfirmed(defaultInteger(issueTicket.getConfirmed(), 0));
        issueTicket.setCreatedBy(userId);
        issueTicket.setUpdatedBy(userId);
        issueTicket.setCreatedAt(now);
        issueTicket.setUpdatedAt(now);
        issueTicketMapper.insert(issueTicket);
        insertLog(issueTicket.getId(), ACTION_ACTIVATED, userId, "创建问题单", null, now);
        if (issueTicket.getAssignee() != null) {
            insertLog(issueTicket.getId(), ACTION_ASSIGNED, userId, "指派问题负责人",
                    buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), null, null), now);
        }
        return ResultData.success(issueTicket.getId());
    }

    /**
     * 修改问题单
     */
    @Override
    public ResultData update(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        ResultData verifyResult = verifyBase(issueTicket);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(issueTicket.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        issueTicket.setStatus(defaultInteger(issueTicket.getStatus(), original.getStatus()));
        issueTicket.setConfirmed(defaultInteger(issueTicket.getConfirmed(), original.getConfirmed()));
        issueTicket.setCreatedBy(original.getCreatedBy());
        issueTicket.setCreatedAt(original.getCreatedAt());
        issueTicket.setUpdatedBy(userId);
        issueTicket.setUpdatedAt(now);
        issueTicketMapper.updateById(issueTicket);
        insertLog(issueTicket.getId(), ACTION_EDITED, userId, "编辑问题单", null, now);
        if (issueTicket.getAssignee() != null && !issueTicket.getAssignee().equals(original.getAssignee())) {
            insertLog(issueTicket.getId(), ACTION_ASSIGNED, userId, "指派问题负责人",
                    buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), null, null), now);
        }
        return ResultData.success(null);
    }

    /**
     * 指派整改人
     */
    @Override
    public ResultData assign(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (Integer.valueOf(STATUS_CLOSED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已关闭的问题单不可以指派");
        }
        Long assignee = defaultUser(issueTicket.getAssignee(), issueTicket.getRectificationUserId());
        if (assignee == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "负责人不能为空");
        }

        Date now = new Date();
        Date dueDate = defaultDate(issueTicket.getDueDate(), issueTicket.getRectificationDeadline());
        original.setAssignee(assignee);
        original.setDueDate(dueDate);
        original.setStatus(STATUS_ACTIVE);
        original.setConfirmed(1);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        insertLog(original.getId(), ACTION_ASSIGNED, userId, "指派问题负责人", buildExtra(assignee, dueDate, null, null), now);
        return ResultData.success(null);
    }

    /**
     * 提交整改
     */
    @Override
    public ResultData rectify(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (Integer.valueOf(STATUS_CLOSED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已关闭的问题单不可以提交整改");
        }
        if (StringUtils.isEmpty(issueTicket.getRectificationDescription())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "整改说明不能为空");
        }

        Date now = new Date();
        Date submittedAt = defaultDate(issueTicket.getRectificationSubmittedAt(), now);
        original.setStatus(STATUS_RESOLVED);
        original.setResolution(defaultText(issueTicket.getResolution(), "已解决"));
        original.setConfirmed(0);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        insertLog(original.getId(), ACTION_RESOLVED, userId, issueTicket.getRectificationDescription(),
                buildExtra(null, null, issueTicket.getRectificationAttachments(), null), submittedAt);
        return ResultData.success(null);
    }

    /**
     * 复验问题单
     */
    @Override
    public ResultData review(IssueTicket issueTicket, Long userId) {
        if (issueTicket == null || issueTicket.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单ID不能为空");
        }
        IssueTicket original = issueTicketMapper.selectById(issueTicket.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        if (!Integer.valueOf(STATUS_RESOLVED).equals(original.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "只有已解决的问题单可以复验");
        }
        if (!Integer.valueOf(REVIEW_PASS).equals(issueTicket.getReviewResult())
                && !Integer.valueOf(REVIEW_REJECT).equals(issueTicket.getReviewResult())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "复验结果不正确");
        }

        Date now = new Date();
        Date reviewedAt = defaultDate(issueTicket.getReviewedAt(), now);
        boolean pass = Integer.valueOf(REVIEW_PASS).equals(issueTicket.getReviewResult());
        original.setStatus(pass ? STATUS_CLOSED : STATUS_ACTIVE);
        original.setConfirmed(pass ? 1 : 0);
        original.setUpdatedBy(userId);
        original.setUpdatedAt(now);
        issueTicketMapper.updateById(original);
        insertLog(original.getId(), pass ? ACTION_CLOSED : ACTION_ACTIVATED, userId, issueTicket.getReviewRemark(),
                buildExtra(null, null, issueTicket.getReviewAttachments(), issueTicket.getReviewResult()), reviewedAt);
        return ResultData.success(null);
    }

    /**
     * 删除问题单
     */
    @Override
    public ResultData delete(Long id) {
        IssueTicket issueTicket = issueTicketMapper.selectById(id);
        if (issueTicket == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        deleteLogs(id);
        issueTicketMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 基础校验
     */
    private ResultData verifyBase(IssueTicket issueTicket) {
        if (issueTicket == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不能为空");
        }
        if (issueTicket.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (StringUtils.isEmpty(issueTicket.getTitle())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题标题不能为空");
        }
        if (StringUtils.isEmpty(issueTicket.getDescription())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题描述不能为空");
        }
        return null;
    }

    /**
     * 写入操作记录
     */
    private void insertLog(Long objectId, String action, Long userId, String comment, String extra, Date createdAt) {
        OperationLog log = new OperationLog();
        log.setObjectType(OBJECT_TYPE_ISSUE);
        log.setObjectId(objectId);
        log.setAction(action);
        log.setActorId(userId);
        log.setActorName(getActorName(userId));
        log.setComment(comment);
        log.setExtra(extra);
        log.setCreatedBy(userId);
        log.setCreatedAt(createdAt == null ? new Date() : createdAt);
        operationLogMapper.insert(log);
    }

    private void deleteLogs(Long objectId) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_type", OBJECT_TYPE_ISSUE);
        queryWrapper.eq("object_id", objectId);
        operationLogMapper.delete(queryWrapper);
    }

    private String getActorName(Long userId) {
        if (userId == null) {
            return null;
        }
        SysAccount account = accountMapper.selectById(userId);
        return account == null ? null : account.getName();
    }

    /**
     * 兼容旧详情字段
     */
    private void fillCompatibleFields(Map<String, Object> data, List<Map<String, Object>> operationLogs) {
        data.put("reportedBy", data.get("createdBy"));
        data.put("reportedByName", data.get("createdByName"));
        data.put("reportedAt", data.get("createdAt"));
        data.put("rectificationUserId", data.get("assignee"));
        data.put("rectificationUserName", data.get("assigneeName"));
        data.put("rectificationDeadline", data.get("dueDate"));
        data.put("rectificationCount", countAction(operationLogs, ACTION_RESOLVED));

        for (Map<String, Object> log : operationLogs) {
            String action = String.valueOf(log.get("action"));
            if (ACTION_ASSIGNED.equals(action)) {
                data.put("assignedBy", log.get("actorId"));
                data.put("assignedByName", log.get("actorName"));
                data.put("assignedAt", log.get("createdAt"));
            } else if (ACTION_RESOLVED.equals(action)) {
                data.put("rectificationDescription", log.get("comment"));
                data.put("rectificationAttachments", findJsonArray(log.get("extra")));
                data.put("rectificationSubmittedAt", log.get("createdAt"));
            } else if (ACTION_CLOSED.equals(action) || ACTION_ACTIVATED.equals(action)) {
                data.put("reviewUserId", log.get("actorId"));
                data.put("reviewUserName", log.get("actorName"));
                data.put("reviewRemark", log.get("comment"));
                data.put("reviewAttachments", findJsonArray(log.get("extra")));
                data.put("reviewedAt", log.get("createdAt"));
                if (ACTION_CLOSED.equals(action)) {
                    data.put("reviewResult", REVIEW_PASS);
                    data.put("closedBy", log.get("actorId"));
                    data.put("closedByName", log.get("actorName"));
                    data.put("closedAt", log.get("createdAt"));
                } else if (!"创建问题单".equals(log.get("comment"))) {
                    data.put("reviewResult", REVIEW_REJECT);
                }
            }
        }
    }

    private int countAction(List<Map<String, Object>> operationLogs, String action) {
        int count = 0;
        for (Map<String, Object> log : operationLogs) {
            if (action.equals(log.get("action"))) {
                count++;
            }
        }
        return count;
    }

    private Long defaultUser(Long first, Long second) {
        return first == null ? second : first;
    }

    private Date defaultDate(Date first, Date second) {
        return first == null ? second : first;
    }

    private Integer defaultInteger(Integer first, Integer second) {
        return first == null ? second : first;
    }

    private String defaultText(String first, String second) {
        return StringUtils.isEmpty(first) ? second : first;
    }

    private String buildExtra(Long assignee, Date dueDate, String attachments, Integer result) {
        StringBuilder builder = new StringBuilder("{");
        boolean hasField = false;
        if (assignee != null) {
            builder.append("\"assignee\":").append(assignee);
            hasField = true;
        }
        if (dueDate != null) {
            appendSeparator(builder, hasField);
            builder.append("\"dueDate\":\"").append(new SimpleDateFormat("yyyy-MM-dd").format(dueDate)).append("\"");
            hasField = true;
        }
        if (!StringUtils.isEmpty(attachments)) {
            appendSeparator(builder, hasField);
            builder.append("\"attachments\":").append(attachments);
            hasField = true;
        }
        if (result != null) {
            appendSeparator(builder, hasField);
            builder.append("\"result\":").append(result);
        }
        builder.append("}");
        return builder.toString();
    }

    private void appendSeparator(StringBuilder builder, boolean hasField) {
        if (hasField) {
            builder.append(",");
        }
    }

    private String findJsonArray(Object extra) {
        if (extra == null) {
            return null;
        }
        String value = String.valueOf(extra);
        int keyIndex = value.indexOf("\"attachments\"");
        if (keyIndex < 0) {
            return null;
        }
        int start = value.indexOf('[', keyIndex);
        int end = value.lastIndexOf(']');
        if (start < 0 || end < start) {
            return null;
        }
        return value.substring(start, end + 1);
    }
}
