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
    private static final String OBJECT_TYPE_ISSUE = "issue";
    private static final String ACTION_ACTIVATED = "activated";
    private static final String ACTION_ASSIGNED = "assigned";
    private static final String ACTION_EDITED = "edited";
    private static final String ACTION_RESOLVED = "resolved";
    private static final String ACTION_CLOSED = "closed";
    private static final String ACTION_CONFIRMED = "confirmed";

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
        insertLog(issueTicket.getId(), ACTION_ACTIVATED, userId, null, null);
        if (issueTicket.getAssignee() != null) {
            insertLog(issueTicket.getId(), ACTION_ASSIGNED, userId, null,
                    buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), null));
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
        insertUpdateLog(issueTicket, original, userId);
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
    private void insertLog(Long objectId, String action, Long userId, String comment, String extra) {
        OperationLog log = new OperationLog();
        log.setObjectType(OBJECT_TYPE_ISSUE);
        log.setObjectId(objectId);
        log.setAction(action);
        log.setActorId(userId);
        log.setActorName(getActorName(userId));
        log.setComment(comment);
        log.setExtra(extra);
        log.setCreatedBy(userId);
        operationLogMapper.insert(log);
    }

    private void insertUpdateLog(IssueTicket issueTicket, IssueTicket original, Long userId) {
        String comment = defaultText(issueTicket.getReviewRemark(), issueTicket.getRectificationDescription());
        String attachments = defaultText(issueTicket.getReviewAttachments(), issueTicket.getRectificationAttachments());
        Integer status = issueTicket.getStatus();
        if (status != null && !status.equals(original.getStatus())) {
            if (Integer.valueOf(STATUS_RESOLVED).equals(status)) {
                insertLog(issueTicket.getId(), ACTION_RESOLVED, userId, comment,
                        buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), attachments, issueTicket.getResolution()));
            } else if (Integer.valueOf(STATUS_CLOSED).equals(status)) {
                insertLog(issueTicket.getId(), ACTION_CLOSED, userId, comment,
                        buildExtra(null, null, attachments));
            } else if (Integer.valueOf(STATUS_ACTIVE).equals(status)) {
                insertLog(issueTicket.getId(), ACTION_ACTIVATED, userId, comment,
                        buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), attachments));
            }
            return;
        }
        if (Integer.valueOf(1).equals(issueTicket.getConfirmed()) && !Integer.valueOf(1).equals(original.getConfirmed())) {
            insertLog(issueTicket.getId(), ACTION_CONFIRMED, userId, comment,
                    buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), attachments));
        } else if ("assign".equals(issueTicket.getActionType())
                || (issueTicket.getAssignee() != null && !issueTicket.getAssignee().equals(original.getAssignee()))) {
            insertLog(issueTicket.getId(), ACTION_ASSIGNED, userId, comment,
                    buildExtra(issueTicket.getAssignee(), issueTicket.getDueDate(), attachments));
        } else {
            insertLog(issueTicket.getId(), ACTION_EDITED, userId, comment,
                    buildExtra(null, null, attachments));
        }
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

    private Integer defaultInteger(Integer first, Integer second) {
        return first == null ? second : first;
    }

    private String defaultText(String first, String second) {
        return StringUtils.isEmpty(first) ? second : first;
    }

    private String buildExtra(Long assignee, Date dueDate, String attachments) {
        return buildExtra(assignee, dueDate, attachments, null);
    }

    private String buildExtra(Long assignee, Date dueDate, String attachments, String resolution) {
        StringBuilder builder = new StringBuilder("{");
        boolean hasField = false;
        if (assignee != null) {
            builder.append("\"assignee\":").append(assignee);
            hasField = true;
            String assigneeName = getActorName(assignee);
            if (!StringUtils.isEmpty(assigneeName)) {
                appendSeparator(builder, hasField);
                builder.append("\"assigneeName\":\"").append(escapeJson(assigneeName)).append("\"");
                hasField = true;
            }
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
        if (!StringUtils.isEmpty(resolution)) {
            appendSeparator(builder, hasField);
            builder.append("\"resolution\":\"").append(escapeJson(resolution)).append("\"");
        }
        builder.append("}");
        return builder.toString();
    }

    private String escapeJson(String text) {
        return text == null ? "" : text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void appendSeparator(StringBuilder builder, boolean hasField) {
        if (hasField) {
            builder.append(",");
        }
    }

}
