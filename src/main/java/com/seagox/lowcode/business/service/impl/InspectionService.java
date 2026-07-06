package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Inspection;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.InspectionMapper;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.business.service.IInspectionService;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * 验收单
 */
@Service
public class InspectionService implements IInspectionService {

    /**
     * 待验收
     */
    private static final int STATUS_PENDING = 1;

    /**
     * 验收中
     */
    private static final int STATUS_PROCESSING = 2;

    /**
     * 已完成
     */
    private static final int STATUS_COMPLETED = 3;

    /**
     * 项目阶段已完成
     */
    private static final int STAGE_STATUS_COMPLETED = 3;

    /**
     * 项目进行中
     */
    private static final int PROJECT_STATUS_PROCESSING = 2;

    /**
     * 项目已交付
     */
    private static final int PROJECT_STATUS_DELIVERED = 4;

    /**
     * 问题单已关闭
     */
    private static final int ISSUE_STATUS_CLOSED = 3;

    /**
     * 验收单消息业务类型
     */
    private static final String BUSINESS_TYPE = "inspection";

    /**
     * 验收单消息类型
     */
    private static final int MESSAGE_TYPE_INSPECTION = 7;

    /**
     * 验收单数据访问对象
     */
    @Autowired
    private InspectionMapper inspectionMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 项目阶段数据访问对象
     */
    @Autowired
    private ProjectStageMapper projectStageMapper;

    /**
     * 阶段验收条目数据访问对象
     */
    @Autowired
    private StageInspectionItemMapper stageInspectionItemMapper;

    /**
     * 问题单数据访问对象
     */
    @Autowired
    private IssueTicketMapper issueTicketMapper;

    /**
     * 消息数据访问对象
     */
    @Autowired
    private MessageMapper messageMapper;

    /**
     * 分页查询验收单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = inspectionMapper.queryInspections(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询验收单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = inspectionMapper.queryInspectionById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增验收单
     */
    @Transactional
    @Override
    public ResultData insert(Inspection inspection, Long userId, Long companyId) {
        ResultData verifyResult = verify(inspection);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(inspection.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        if (inspection.getStatus() == null) {
            inspection.setStatus(STATUS_PENDING);
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus()) && inspection.getPassedAt() == null) {
            inspection.setPassedAt(now);
        }
        inspection.setCreatedBy(userId);
        inspection.setUpdatedBy(userId);
        inspection.setCreatedAt(now);
        inspection.setUpdatedAt(now);
        inspectionMapper.insert(inspection);
        refreshParticipantMessages(inspection, userId, companyId, now);
        return ResultData.success(inspection.getId());
    }

    /**
     * 修改验收单
     */
    @Transactional
    @Override
    public ResultData update(Inspection inspection, Long userId, Long companyId) {
        if (inspection == null || inspection.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单ID不能为空");
        }
        ResultData verifyResult = verify(inspection);
        if (verifyResult != null) {
            return verifyResult;
        }
        Inspection exist = inspectionMapper.selectById(inspection.getId());
        if (exist == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        if (projectMapper.selectById(inspection.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        if (inspection.getStatus() == null) {
            inspection.setStatus(exist.getStatus());
        }
        if (!Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())) {
            inspection.setPassedAt(null);
        } else if (inspection.getPassedAt() == null) {
            inspection.setPassedAt(exist.getPassedAt() == null ? now : exist.getPassedAt());
        }
        inspection.setCreatedBy(exist.getCreatedBy());
        inspection.setCreatedAt(exist.getCreatedAt());
        inspection.setUpdatedBy(userId);
        inspection.setUpdatedAt(now);
        inspectionMapper.updateById(inspection);
        refreshParticipantMessages(inspection, userId, companyId, now);
        return ResultData.success(null);
    }

    /**
     * 完成验收单
     */
    @Override
    @Transactional
    public ResultData complete(Inspection inspection, Long userId) {
        if (inspection == null || inspection.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单ID不能为空");
        }
        Inspection exist = inspectionMapper.selectById(inspection.getId());
        if (exist == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(exist.getStatus())) {
            ResultData participantResult = verifyParticipant(exist, userId);
            if (participantResult != null) {
                return participantResult;
            }
            if (inspection.getInspectionItems() != null && inspection.getInspectionItems().trim().length() > 0) {
                Date now = new Date();
                exist.setInspectionItems(markInspectionItemsCompleted(inspection.getInspectionItems()));
                exist.setUpdatedBy(userId);
                exist.setUpdatedAt(now);
                inspectionMapper.updateById(exist);
                return ResultData.success(null);
            }
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单已完成");
        }
        ResultData participantResult = verifyParticipant(exist, userId);
        if (participantResult != null) {
            return participantResult;
        }
        ResultData issueResult = verifyNoOpenIssueTickets(exist.getProjectId());
        if (issueResult != null) {
            return issueResult;
        }

        Date now = new Date();
        String inspectionItems = inspection.getInspectionItems() == null ? exist.getInspectionItems() : inspection.getInspectionItems();
        exist.setInspectionItems(markInspectionItemsCompleted(inspectionItems));
        exist.setSitePhotos(inspection.getSitePhotos() == null ? exist.getSitePhotos() : inspection.getSitePhotos());
        exist.setParticipants(inspection.getParticipants() == null ? exist.getParticipants() : inspection.getParticipants());
        exist.setSignatures(inspection.getSignatures() == null ? exist.getSignatures() : inspection.getSignatures());
        exist.setAcceptanceComments(inspection.getAcceptanceComments() == null ? exist.getAcceptanceComments() : inspection.getAcceptanceComments());
        exist.setRemark(inspection.getRemark() == null ? exist.getRemark() : inspection.getRemark());
        exist.setStatus(STATUS_COMPLETED);
        exist.setPassedAt(inspection.getPassedAt() == null ? now : inspection.getPassedAt());
        exist.setUpdatedBy(userId);
        exist.setUpdatedAt(now);
        inspectionMapper.updateById(exist);
        messageMapper.deleteMessage(BUSINESS_TYPE, exist.getId());
        completeProjectStage(exist, userId, now);
        completeProjectIfAllInspectionsCompleted(exist.getProjectId(), userId, now);
        return ResultData.success(null);
    }

    /**
     * 验收完成后同步本次验收项目状态
     */
    private String markInspectionItemsCompleted(String inspectionItems) {
        if (inspectionItems == null || inspectionItems.trim().length() == 0) {
            return inspectionItems;
        }
        try {
            JSONArray items = JSONArray.parseArray(inspectionItems);
            for (int i = 0; i < items.size(); i++) {
                Object raw = items.get(i);
                if (!(raw instanceof JSONObject)) {
                    continue;
                }
                JSONObject item = (JSONObject) raw;
                item.put("status", "completed");
                item.put("statusText", "验收完成");
            }
            return items.toJSONString();
        } catch (Exception e) {
            return inspectionItems;
        }
    }

    /**
     * 验收完成后同步项目阶段状态
     */
    private void completeProjectStage(Inspection inspection, Long userId, Date completedAt) {
        if (inspection.getStageId() == null) {
            return;
        }
        Long stageId = inspection.getStageId();
        ProjectStage stage = projectStageMapper.selectById(stageId);
        if (stage == null) {
            return;
        }
        List<StageInspectionItem> stageItems = stageInspectionItemMapper.selectList(
                new LambdaQueryWrapper<StageInspectionItem>()
                        .eq(StageInspectionItem::getStageId, stageId));
        if (stageItems.size() == 0 || !allStageItemsPassed(stageId, stageItems)) {
            return;
        }
        stage.setStatus(STAGE_STATUS_COMPLETED);
        stage.setCompletedBy(userId);
        stage.setCompletedAt(completedAt);
        stage.setActualEndDate(completedAt);
        stage.setUpdatedBy(userId);
        stage.setUpdatedAt(completedAt);
        projectStageMapper.updateById(stage);
    }

    /**
     * 判断阶段下所有验收条目是否都已有完成验收记录
     */
    private boolean allStageItemsPassed(Long stageId, List<StageInspectionItem> stageItems) {
        Set<String> requiredItemIds = new HashSet<>();
        for (StageInspectionItem item : stageItems) {
            requiredItemIds.add(String.valueOf(item.getId()));
        }
        Set<String> passedItemIds = new HashSet<>();
        List<Inspection> completedInspections = inspectionMapper.selectList(new LambdaQueryWrapper<Inspection>()
                .eq(Inspection::getStageId, stageId)
                .eq(Inspection::getStatus, STATUS_COMPLETED));
        for (Inspection completedInspection : completedInspections) {
            passedItemIds.addAll(parseInspectionItemIds(completedInspection.getInspectionItems()));
        }
        return passedItemIds.containsAll(requiredItemIds);
    }

    /**
     * 进行中项目下所有验收单完成后，自动交付项目
     */
    private void completeProjectIfAllInspectionsCompleted(Long projectId, Long userId, Date completedAt) {
        if (projectId == null) {
            return;
        }
        Project project = projectMapper.selectById(projectId);
        if (project == null || !Integer.valueOf(PROJECT_STATUS_PROCESSING).equals(project.getStatus())) {
            return;
        }
        Long total = inspectionMapper.selectCount(new LambdaQueryWrapper<Inspection>()
                .eq(Inspection::getProjectId, projectId));
        if (total == null || total == 0) {
            return;
        }
        Long unfinished = inspectionMapper.selectCount(new LambdaQueryWrapper<Inspection>()
                .eq(Inspection::getProjectId, projectId)
                .ne(Inspection::getStatus, STATUS_COMPLETED));
        if (unfinished != null && unfinished > 0) {
            return;
        }
        project.setStatus(PROJECT_STATUS_DELIVERED);
        project.setDeliveredAt(completedAt);
        if (project.getActualEndDate() == null) {
            project.setActualEndDate(completedAt);
        }
        project.setUpdatedBy(userId);
        project.setUpdatedAt(completedAt);
        projectMapper.updateById(project);
    }

    /**
     * 解析验收单覆盖的阶段验收条目ID
     */
    private Set<String> parseInspectionItemIds(String inspectionItems) {
        Set<String> itemIds = new HashSet<>();
        if (inspectionItems == null || inspectionItems.trim().length() == 0) {
            return itemIds;
        }
        try {
            JSONArray items = JSONArray.parseArray(inspectionItems);
            for (int i = 0; i < items.size(); i++) {
                Object raw = items.get(i);
                if (raw instanceof String || raw instanceof Number) {
                    itemIds.add(String.valueOf(raw));
                    continue;
                }
                JSONObject item = items.getJSONObject(i);
                Object id = item.get("id");
                if (id == null) {
                    id = item.get("sourceId");
                }
                if (id == null) {
                    id = item.get("value");
                }
                if (id != null) {
                    itemIds.add(String.valueOf(id));
                }
            }
        } catch (Exception e) {
            return itemIds;
        }
        return itemIds;
    }

    /**
     * 校验当前操作人是否为验收人
     */
    private ResultData verifyParticipant(Inspection inspection, Long userId) {
        if (inspection.getParticipants() == null || inspection.getParticipants().trim().length() == 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收人不能为空");
        }
        try {
            JSONArray participants = JSONArray.parseArray(inspection.getParticipants());
            for (int i = 0; i < participants.size(); i++) {
                JSONObject participant = participants.getJSONObject(i);
                if (participant == null || participant.get("userId") == null) {
                    continue;
                }
                if (String.valueOf(userId).equals(String.valueOf(participant.get("userId")))) {
                    return null;
                }
            }
        } catch (Exception e) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收人数据格式错误");
        }
        return ResultData.warn(ResultCode.OTHER_ERROR, "仅本单验收人可操作");
    }

    /**
     * 校验项目下不存在未关闭的问题单
     */
    private ResultData verifyNoOpenIssueTickets(Long projectId) {
        if (projectId == null) {
            return null;
        }
        Long count = issueTicketMapper.selectCount(new LambdaQueryWrapper<IssueTicket>()
                .eq(IssueTicket::getProjectId, projectId)
                .and(wrapper -> wrapper.ne(IssueTicket::getStatus, ISSUE_STATUS_CLOSED)
                        .or()
                        .isNull(IssueTicket::getStatus)));
        if (count != null && count > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前项目存在未关闭的问题单，请关闭后再完成验收");
        }
        return null;
    }

    /**
     * 删除验收单
     */
    @Override
    public ResultData delete(Long id) {
        if (inspectionMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        inspectionMapper.deleteById(id);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        return ResultData.success(null);
    }

    /**
     * 重建验收人消息
     */
    private void refreshParticipantMessages(Inspection inspection, Long userId, Long companyId, Date now) {
        messageMapper.deleteMessage(BUSINESS_TYPE, inspection.getId());
        if (companyId == null || inspection.getId() == null
                || Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())) {
            return;
        }
        Set<Long> participantUserIds = parseParticipantUserIds(inspection.getParticipants());
        for (Long toUserId : participantUserIds) {
            if (toUserId == null || toUserId.equals(userId)) {
                continue;
            }
            SysMessage message = new SysMessage();
            message.setCompanyId(companyId);
            message.setType(MESSAGE_TYPE_INSPECTION);
            message.setFromUserId(userId);
            message.setToUserId(toUserId);
            message.setTitle("您有一条验收单待处理");
            message.setBusinessType(BUSINESS_TYPE);
            message.setBusinessKey(inspection.getId());
            message.setStatus(0);
            message.setCreatedBy(userId);
            message.setCreatedAt(now);
            message.setUpdatedBy(userId);
            message.setUpdatedAt(now);
            messageMapper.insert(message);
        }
    }

    /**
     * 解析验收人用户ID
     */
    private Set<Long> parseParticipantUserIds(String participants) {
        Set<Long> userIds = new HashSet<>();
        if (participants == null || participants.trim().length() == 0) {
            return userIds;
        }
        try {
            JSONArray items = JSONArray.parseArray(participants);
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item == null) {
                    continue;
                }
                Object userId = item.get("userId");
                if (userId == null) {
                    userId = item.get("accountId");
                }
                if (userId == null) {
                    userId = item.get("backendUserId");
                }
                if (userId != null) {
                    userIds.add(Long.valueOf(String.valueOf(userId)));
                }
            }
        } catch (Exception e) {
            return userIds;
        }
        return userIds;
    }

    /**
     * 校验验收单
     */
    private ResultData verify(Inspection inspection) {
        if (inspection == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不能为空");
        }
        if (inspection.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (inspection.getStatus() != null
                && !Integer.valueOf(STATUS_PENDING).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_PROCESSING).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收状态不正确");
        }
        return null;
    }
}
