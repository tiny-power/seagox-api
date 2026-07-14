package com.seagox.lowcode.business.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.ConstructionLog;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.mapper.ConstructionLogMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IConstructionLogService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.mapper.MessageMapper;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 施工日志
 */
@Service
public class ConstructionLogService implements IConstructionLogService {

    /**
     * 施工日志数据访问对象
     */
    @Autowired
    private ConstructionLogMapper constructionLogMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 消息数据访问对象
     */
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 分页查询施工日志
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = constructionLogMapper.queryConstructionLogs(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询施工日志详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = constructionLogMapper.queryConstructionLogById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增施工日志
     */
    @Override
    public ResultData insert(ConstructionLog constructionLog, Long userId, Long companyId) {
        ResultData verifyResult = verify(constructionLog);
        if (verifyResult != null) {
            return verifyResult;
        }
        Project project = projectMapper.selectById(constructionLog.getProjectId());
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        companyId = companyId == null ? project.getCompanyId() : companyId;

        Date now = new Date();
        if (constructionLog.getFilledBy() == null) {
            constructionLog.setFilledBy(userId);
        }
        if (constructionLog.getHasIssue() == null) {
            constructionLog.setHasIssue(StringUtils.isEmpty(constructionLog.getSiteIssues()) ? 0 : 1);
        }
        if (constructionLog.getStatus() == null) {
            constructionLog.setStatus(1);
        }
        if (Integer.valueOf(1).equals(constructionLog.getStatus()) && constructionLog.getSubmittedAt() == null) {
            constructionLog.setSubmittedAt(now);
        }
        constructionLog.setCreatedBy(userId);
        constructionLog.setUpdatedBy(userId);
        constructionLog.setCreatedAt(now);
        constructionLog.setUpdatedAt(now);
        constructionLogMapper.insert(constructionLog);
        refreshAssistantMessages(constructionLog, userId, companyId, now);
        return ResultData.success(constructionLog.getId());
    }

    /**
     * 修改施工日志
     */
    @Override
    public ResultData update(ConstructionLog constructionLog, Long userId, Long companyId) {
        ResultData verifyResult = verify(constructionLog);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (constructionLog.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志ID不能为空");
        }
        ConstructionLog exist = constructionLogMapper.selectById(constructionLog.getId());
        if (exist == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志不存在");
        }
        Project project = projectMapper.selectById(constructionLog.getProjectId());
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        companyId = companyId == null ? project.getCompanyId() : companyId;

        Date now = new Date();
        if (constructionLog.getFilledBy() == null) {
            constructionLog.setFilledBy(exist.getFilledBy());
        }
        if (constructionLog.getHasIssue() == null) {
            constructionLog.setHasIssue(StringUtils.isEmpty(constructionLog.getSiteIssues()) ? 0 : 1);
        }
        if (constructionLog.getStatus() == null) {
            constructionLog.setStatus(exist.getStatus());
        }
        if (Integer.valueOf(1).equals(constructionLog.getStatus()) && constructionLog.getSubmittedAt() == null) {
            constructionLog.setSubmittedAt(exist.getSubmittedAt() == null ? now : exist.getSubmittedAt());
        }
        constructionLog.setCreatedBy(exist.getCreatedBy());
        constructionLog.setCreatedAt(exist.getCreatedAt());
        constructionLog.setUpdatedBy(userId);
        constructionLog.setUpdatedAt(now);
        constructionLogMapper.updateById(constructionLog);
        refreshAssistantMessages(constructionLog, userId, companyId, now);
        return ResultData.success(null);
    }

    /**
     * 删除施工日志
     */
    @Override
    public ResultData delete(Long id) {
        if (constructionLogMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志不存在");
        }
        messageMapper.deleteMessage("construction_log", id);
        constructionLogMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 重建施工日志相关人员消息
     */
    private void refreshAssistantMessages(ConstructionLog constructionLog, Long userId, Long companyId, Date now) {
        if (constructionLog.getId() == null) {
            return;
        }

        messageMapper.deleteMessage("construction_log", constructionLog.getId());
        if (!Integer.valueOf(1).equals(constructionLog.getStatus()) || companyId == null) {
            return;
        }

        Set<Long> userIds = parseAssistantUserIds(constructionLog.getAssistants());
        for (Long toUserId : userIds) {
            if (toUserId == null || toUserId.equals(userId)) {
                continue;
            }
            SysMessage message = new SysMessage();
            message.setCompanyId(companyId);
            message.setType(10);
            message.setFromUserId(userId);
            message.setToUserId(toUserId);
            message.setTitle("您有一条新的施工日记");
            message.setBusinessType("construction_log");
            message.setBusinessKey(constructionLog.getId());
            message.setStatus(0);
            message.setCreatedBy(userId);
            message.setCreatedAt(now);
            message.setUpdatedBy(userId);
            message.setUpdatedAt(now);
            messageMapper.insert(message);
        }
    }

    /**
     * 解析施工日志相关人员用户ID
     */
    private Set<Long> parseAssistantUserIds(String assistants) {
        Set<Long> userIds = new HashSet<>();
        if (StringUtils.isEmpty(assistants)) {
            return userIds;
        }
        try {
            JSONArray items = JSONArray.parseArray(assistants);
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                Long userId = getAssistantUserId(item);
                if (userId != null) {
                    userIds.add(userId);
                }
            }
        } catch (Exception e) {
            return userIds;
        }
        return userIds;
    }

    /**
     * 获取相关人员账号ID
     */
    private Long getAssistantUserId(JSONObject item) {
        if (item == null) {
            return null;
        }
        Object value = item.get("userId");
        if (value == null) {
            value = item.get("accountId");
        }
        if (value == null) {
            value = item.get("backendUserId");
        }
        if (value == null || StringUtils.isEmpty(String.valueOf(value))) {
            return null;
        }
        return Long.valueOf(String.valueOf(value));
    }

    /**
     * 校验施工日志
     */
    private ResultData verify(ConstructionLog constructionLog) {
        if (constructionLog == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志不能为空");
        }
        if (constructionLog.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (constructionLog.getLogDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "日记日期不能为空");
        }
        if (StringUtils.isEmpty(constructionLog.getCurrentProgressSummary())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "今日工作内容不能为空");
        }
        if (constructionLog.getExpectedCompletionAt() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "预计完成时间不能为空");
        }
        return null;
    }
}
