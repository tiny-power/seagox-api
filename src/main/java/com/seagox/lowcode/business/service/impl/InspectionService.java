package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Inspection;
import com.seagox.lowcode.business.entity.InspectionItem;
import com.seagox.lowcode.business.entity.InspectionParticipant;
import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.InspectionItemMapper;
import com.seagox.lowcode.business.mapper.InspectionMapper;
import com.seagox.lowcode.business.mapper.InspectionParticipantMapper;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.business.service.IInspectionService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.entity.SysMessage;
import com.seagox.lowcode.system.mapper.AccountMapper;
import com.seagox.lowcode.system.mapper.MessageMapper;
import com.seagox.lowcode.util.WeiChatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
     * 整改中
     */
    private static final int STATUS_RECTIFYING = 4;

    /**
     * 待复验
     */
    private static final int STATUS_RECHECK_PENDING = 5;

    /**
     * 项目阶段已完成
     */
    private static final int STAGE_STATUS_COMPLETED = 3;

    /**
     * 项目进行中
     */
    private static final int PROJECT_STATUS_PROCESSING = 2;

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
    private static final int MESSAGE_TYPE_INSPECTION = 6;

    /**
     * 验收单数据访问对象
     */
    @Autowired
    private InspectionMapper inspectionMapper;

    /**
     * 验收单验收项数据访问对象
     */
    @Autowired
    private InspectionItemMapper inspectionItemMapper;

    /**
     * 验收单参与者数据访问对象
     */
    @Autowired
    private InspectionParticipantMapper inspectionParticipantMapper;

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
    
    @Autowired
    private WeiChatUtils weiChatUtils;
    
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 分页查询验收单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = inspectionMapper.queryInspections(params);
        for (Map<String, Object> item : list) {
            fillInspectionChildren(item);
        }
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
        Map<String, Object> issueParams = new HashMap<>();
        issueParams.put("inspectionId", id);
        List<Map<String, Object>> issueTickets = issueTicketMapper.queryIssueTickets(issueParams);
        MapDateFormatUtils.formatDateValues(issueTickets);
        data.put("issueTickets", issueTickets);
        fillInspectionChildren(data);
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 组装验收单子表数据
     */
    private void fillInspectionChildren(Map<String, Object> data) {
        Object idValue = data.get("id");
        if (idValue == null) {
            return;
        }
        Long inspectionId = Long.valueOf(String.valueOf(idValue));
        List<Map<String, Object>> inspectionItems = inspectionItemMapper.queryByInspectionId(inspectionId);
        List<Map<String, Object>> participants = inspectionParticipantMapper.queryByInspectionId(inspectionId);
        data.put("inspectionItems", inspectionItems);
        data.put("items", inspectionItems);
        data.put("participants", participants);
        data.put("signatures", participants);
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
        if (Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus()) && inspection.getResult() == null) {
            inspection.setResult("pass");
        }
        inspection.setCreatedBy(userId);
        inspection.setUpdatedBy(userId);
        inspection.setCreatedAt(now);
        inspection.setUpdatedAt(now);
        inspectionMapper.insert(inspection);
        saveInspectionItems(inspection, userId, now, Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus()));
        saveInspectionParticipants(inspection, now);
        refreshInspectionMessages(inspection, userId, companyId, now);
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
        if (Integer.valueOf(STATUS_RECHECK_PENDING).equals(inspection.getStatus())) {
            ResultData issueResult = verifyNoOpenInspectionIssueTickets(exist.getId());
            if (issueResult != null) {
                return issueResult;
            }
        }

        Date now = new Date();
        if (inspection.getStatus() == null) {
            inspection.setStatus(exist.getStatus());
        }
        boolean startRecheck = Integer.valueOf(STATUS_RECHECK_PENDING).equals(exist.getStatus())
                && Integer.valueOf(STATUS_PROCESSING).equals(inspection.getStatus());
        if (!Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())) {
            inspection.setPassedAt(null);
        } else if (inspection.getPassedAt() == null) {
            inspection.setPassedAt(exist.getPassedAt() == null ? now : exist.getPassedAt());
        }
        if (startRecheck) {
            inspection.setResult("");
            inspection.setAcceptanceComments("");
        } else if (inspection.getResult() == null) {
            inspection.setResult(exist.getResult());
        }
        inspection.setCreatedBy(exist.getCreatedBy());
        inspection.setCreatedAt(exist.getCreatedAt());
        inspection.setUpdatedBy(userId);
        inspection.setUpdatedAt(now);
        inspectionMapper.updateById(inspection);
        saveInspectionItems(inspection, userId, now, Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus()));
        saveInspectionParticipants(inspection, now);
        refreshInspectionMessages(inspection, userId, companyId, now);
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
            ResultData operatorResult = verifyCompleteOperator(exist, inspection, userId);
            if (operatorResult != null) {
                return operatorResult;
            }
            if (inspection.getInspectionItems() != null && inspection.getInspectionItems().trim().length() > 0) {
                Date now = new Date();
                saveInspectionItems(inspection, userId, now, true);
                exist.setUpdatedBy(userId);
                exist.setUpdatedAt(now);
                inspectionMapper.updateById(exist);
                return ResultData.success(null);
            }
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单已完成");
        }
        ResultData operatorResult = verifyCompleteOperator(exist, inspection, userId);
        if (operatorResult != null) {
            return operatorResult;
        }
        ResultData issueResult = verifyNoOpenIssueTickets(exist.getProjectId());
        if (issueResult != null) {
            return issueResult;
        }

        Date now = new Date();
        exist.setSitePhotos(inspection.getSitePhotos() == null ? exist.getSitePhotos() : inspection.getSitePhotos());
        exist.setAcceptanceComments(inspection.getAcceptanceComments() == null ? exist.getAcceptanceComments() : inspection.getAcceptanceComments());
        exist.setRemark(inspection.getRemark() == null ? exist.getRemark() : inspection.getRemark());
        exist.setResult(inspection.getResult() == null ? "pass" : inspection.getResult());
        exist.setStatus(STATUS_COMPLETED);
        exist.setPassedAt(inspection.getPassedAt() == null ? now : inspection.getPassedAt());
        exist.setUpdatedBy(userId);
        exist.setUpdatedAt(now);
        inspectionMapper.updateById(exist);
        inspection.setId(exist.getId());
        saveInspectionItems(inspection, userId, now, true);
        saveInspectionParticipants(inspection, now);
        messageMapper.deleteMessage(BUSINESS_TYPE, exist.getId());
        completeProjectStage(exist, userId, now);
        return ResultData.success(null);
    }

    /**
     * 保存验收单验收项
     */
    private void saveInspectionItems(Inspection inspection, Long userId, Date now, boolean completed) {
        if (inspection.getInspectionItems() == null) {
            return;
        }
        inspectionItemMapper.delete(new LambdaQueryWrapper<InspectionItem>()
                .eq(InspectionItem::getInspectionId, inspection.getId()));
        if (inspection.getInspectionItems().trim().length() == 0) {
            return;
        }
        JSONArray items = JSONArray.parseArray(inspection.getInspectionItems());
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = toJSONObject(items.get(i));
            if (item == null) {
                continue;
            }
            InspectionItem inspectionItem = new InspectionItem();
            inspectionItem.setInspectionId(inspection.getId());
            inspectionItem.setSourceItemId(parseLong(firstNotNull(item, "sourceItemId", "sourceId", "value", "id")));
            inspectionItem.setName(parseString(firstNotNull(item, "name", "title", "label", "inspectionName")));
            if (isBlank(inspectionItem.getName())) {
                continue;
            }
            Integer result = parseInspectionItemResult(item);
            if (completed && (result == null || result == 0)) {
                result = 1;
            }
            inspectionItem.setResult(result == null ? 0 : result);
            inspectionItem.setRemark(parseString(item.get("remark")));
            inspectionItem.setAttachments(toJsonString(item.get("attachments")));
            inspectionItem.setInspectedBy(parseLong(firstNotNull(item, "inspectedBy", "inspectorId")));
            inspectionItem.setInspectedAt(parseDate(firstNotNull(item, "inspectedAt", "inspectionTime")));
            if (completed && inspectionItem.getInspectedBy() == null) {
                inspectionItem.setInspectedBy(userId);
            }
            if (completed && inspectionItem.getInspectedAt() == null) {
                inspectionItem.setInspectedAt(now);
            }
            inspectionItem.setCreatedBy(userId);
            inspectionItem.setCreatedAt(now);
            inspectionItem.setUpdatedBy(userId);
            inspectionItem.setUpdatedAt(now);
            inspectionItemMapper.insert(inspectionItem);
        }
    }

    /**
     * 保存验收单参与者与签名
     */
    private void saveInspectionParticipants(Inspection inspection, Date now) {
        if (inspection.getParticipants() == null && inspection.getSignatures() == null) {
            return;
        }
        inspectionParticipantMapper.delete(new LambdaQueryWrapper<InspectionParticipant>()
                .eq(InspectionParticipant::getInspectionId, inspection.getId()));
        Map<String, JSONObject> participantMap = new LinkedHashMap<>();
        mergeParticipants(participantMap, inspection.getParticipants());
        mergeParticipants(participantMap, inspection.getSignatures());
        for (JSONObject item : participantMap.values()) {
            Long userId = parseMemberUserId(item);
            if (userId == null) {
                continue;
            }
            InspectionParticipant participant = new InspectionParticipant();
            participant.setInspectionId(inspection.getId());
            participant.setUserId(userId);
            participant.setProjectMemberId(parseLong(firstNotNull(item, "projectMemberId", "memberId")));
            participant.setRoleName(parseString(firstNotNull(item, "roleName", "role")));
            participant.setSignatureUrl(parseString(firstNotNull(item, "signatureUrl", "signaturePath", "url", "fileUrl", "filePath")));
            participant.setSignedAt(parseDate(firstNotNull(item, "signedAt", "signTime")));
            if (!isBlank(participant.getSignatureUrl()) && participant.getSignedAt() == null) {
                participant.setSignedAt(now);
            }
            participant.setCreatedAt(now);
            participant.setUpdatedAt(now);
            inspectionParticipantMapper.insert(participant);
        }
    }

    private void mergeParticipants(Map<String, JSONObject> participantMap, String json) {
        if (json == null || json.trim().length() == 0) {
            return;
        }
        JSONArray items = JSONArray.parseArray(json);
        for (int i = 0; i < items.size(); i++) {
            JSONObject item = toJSONObject(items.get(i));
            if (item == null) {
                continue;
            }
            Long userId = parseMemberUserId(item);
            if (userId == null) {
                continue;
            }
            String key = String.valueOf(userId);
            JSONObject merged = participantMap.get(key);
            if (merged == null) {
                participantMap.put(key, item);
            } else {
                merged.putAll(item);
            }
        }
    }

    private List<InspectionParticipant> queryInspectionParticipants(Long inspectionId) {
        if (inspectionId == null) {
            return new ArrayList<>();
        }
        return inspectionParticipantMapper.selectList(new LambdaQueryWrapper<InspectionParticipant>()
                .eq(InspectionParticipant::getInspectionId, inspectionId));
    }

    private JSONObject toJSONObject(Object value) {
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        if (value == null) {
            return null;
        }
        JSONObject item = new JSONObject();
        item.put("name", value);
        return item;
    }

    private Object firstNotNull(JSONObject item, String... keys) {
        for (String key : keys) {
            Object value = item.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private Integer parseInspectionItemResult(JSONObject item) {
        Integer result = parseInteger(item.get("result"));
        if (result != null) {
            return result;
        }
        String status = parseString(item.get("status"));
        if ("completed".equals(status) || "pass".equals(status) || "passed".equals(status)) {
            return 1;
        }
        if ("failed".equals(status) || "fail".equals(status)) {
            return 2;
        }
        if ("notApplicable".equals(status)) {
            return 3;
        }
        return 0;
    }

    private String toJsonString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return JSONObject.toJSONString(value);
    }

    private String parseString(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    private Long parseLong(Object value) {
        if (value == null || String.valueOf(value).trim().length() == 0) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(Object value) {
        if (value == null || String.valueOf(value).trim().length() == 0) {
            return null;
        }
        try {
            return Integer.valueOf(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    private Date parseDate(Object value) {
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value == null || String.valueOf(value).trim().length() == 0) {
            return null;
        }
        try {
            JSONObject wrapper = new JSONObject();
            wrapper.put("value", value);
            return wrapper.getDate("value");
        } catch (Exception e) {
            return null;
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
            List<InspectionItem> completedItems = inspectionItemMapper.selectList(
                    new LambdaQueryWrapper<InspectionItem>()
                            .eq(InspectionItem::getInspectionId, completedInspection.getId()));
            for (InspectionItem completedItem : completedItems) {
                if (completedItem.getSourceItemId() != null) {
                    passedItemIds.add(String.valueOf(completedItem.getSourceItemId()));
                }
            }
        }
        return passedItemIds.containsAll(requiredItemIds);
    }

    /**
     * 校验当前操作人是否为验收负责人
     */
    private ResultData verifyInspector(Inspection inspection, Long userId) {
        if (inspection.getInspectorId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收负责人不能为空");
        }
        if (!inspection.getInspectorId().equals(userId)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "仅本单验收负责人可操作");
        }
        return null;
    }

    private ResultData verifyCompleteOperator(Inspection exist, Inspection submitted, Long userId) {
        ResultData inspectorResult = verifyInspector(exist, userId);
        if (inspectorResult == null) {
            return null;
        }
        String signatures = submitted == null ? null : submitted.getSignatures();
        if (signatures != null && allSignaturesSigned(signatures) && isSignedByUser(signatures, userId)) {
            return null;
        }
        if (signatures == null && allParticipantsSigned(exist.getId()) && isParticipantSignedByUser(exist.getId(), userId)) {
            return null;
        }
        return inspectorResult;
    }

    private boolean allParticipantsSigned(Long inspectionId) {
        List<InspectionParticipant> participants = queryInspectionParticipants(inspectionId);
        if (participants.size() == 0) {
            return false;
        }
        for (InspectionParticipant participant : participants) {
            if (isBlank(participant.getSignatureUrl())) {
                return false;
            }
        }
        return true;
    }

    private boolean isParticipantSignedByUser(Long inspectionId, Long userId) {
        if (userId == null) {
            return false;
        }
        List<InspectionParticipant> participants = queryInspectionParticipants(inspectionId);
        for (InspectionParticipant participant : participants) {
            if (userId.equals(participant.getUserId()) && !isBlank(participant.getSignatureUrl())) {
                return true;
            }
        }
        return false;
    }

    private boolean isSignedByUser(String signatures, Long userId) {
        if (userId == null || signatures == null || signatures.trim().length() == 0) {
            return false;
        }
        try {
            JSONArray items = JSONArray.parseArray(signatures);
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item == null || !Boolean.TRUE.equals(item.getBoolean("signed"))) {
                    continue;
                }
                Long signerUserId = parseMemberUserId(item);
                if (userId.equals(signerUserId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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
     * 校验验收单下不存在未关闭的问题单
     */
    private ResultData verifyNoOpenInspectionIssueTickets(Long inspectionId) {
        if (inspectionId == null) {
            return null;
        }
        Long count = issueTicketMapper.selectCount(new LambdaQueryWrapper<IssueTicket>()
                .eq(IssueTicket::getInspectionId, inspectionId)
                .and(wrapper -> wrapper.ne(IssueTicket::getStatus, ISSUE_STATUS_CLOSED)
                        .or()
                        .isNull(IssueTicket::getStatus)));
        if (count != null && count > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前验收单存在未关闭的问题单，请关闭后再提交复验");
        }
        return null;
    }

    /**
     * 删除验收单
     */
    @Override
    @Transactional
    public ResultData delete(Long id) {
        if (inspectionMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        inspectionItemMapper.delete(new LambdaQueryWrapper<InspectionItem>()
                .eq(InspectionItem::getInspectionId, id));
        inspectionParticipantMapper.delete(new LambdaQueryWrapper<InspectionParticipant>()
                .eq(InspectionParticipant::getInspectionId, id));
        inspectionMapper.deleteById(id);
        messageMapper.deleteMessage(BUSINESS_TYPE, id);
        return ResultData.success(null);
    }

    /**
     * 重建验收单待办消息
     */
    private void refreshInspectionMessages(Inspection inspection, Long userId, Long companyId, Date now) {
        messageMapper.deleteMessage(BUSINESS_TYPE, inspection.getId());
        if (companyId == null || inspection.getId() == null
                || Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())
                || Integer.valueOf(STATUS_RECTIFYING).equals(inspection.getStatus())) {
            return;
        }
        if (isWaitingForSignatures(inspection)) {
            Set<Long> signerUserIds = queryUnsignedSignerUserIds(inspection.getId());
            for (Long toUserId : signerUserIds) {
                insertInspectionMessage(companyId, userId, toUserId, inspection.getId(), now, "您有一条验收单待签名");
            }
            return;
        }
        Long inspectorId = inspection.getInspectorId();
        String title = Integer.valueOf(STATUS_RECHECK_PENDING).equals(inspection.getStatus())
                ? "您有一条验收单待复验"
                : "您有一条验收单待质检";
        insertInspectionMessage(companyId, userId, inspectorId, inspection.getId(), now, title);
    }

    private void insertInspectionMessage(Long companyId, Long fromUserId, Long toUserId, Long inspectionId, Date now, String title) {
        if (toUserId == null || toUserId.equals(fromUserId)) {
            return;
        }
        SysMessage message = new SysMessage();
        message.setCompanyId(companyId);
        message.setType(MESSAGE_TYPE_INSPECTION);
        message.setFromUserId(fromUserId);
        message.setToUserId(toUserId);
        message.setTitle(title);
        message.setBusinessType(BUSINESS_TYPE);
        message.setBusinessKey(inspectionId);
        message.setStatus(0);
        message.setCreatedBy(fromUserId);
        message.setCreatedAt(now);
        message.setUpdatedBy(fromUserId);
        message.setUpdatedAt(now);
        SysAccount fromAccount = accountMapper.selectById(fromUserId);
        if(fromAccount != null) {
        	SysAccount toAccount = accountMapper.selectById(toUserId);
        	if(!StringUtils.isEmpty(toAccount.getMpOpenid())) {
        		JSONObject content = new JSONObject();
        		content.put("thing6", "您有一条验收单待确认");
        		content.put("thing3", fromAccount.getName());
        		content.put("phone_number4", fromAccount.getPhone());
        		weiChatUtils.sendServiceMsg("5FTU2ZmycYMeTEvypLrh7IPevOPCZeEA0l1qLg_jnCU", toAccount.getMpOpenid(), "pages/workbench/construction-drawing/construction-drawing", content);
        	}
        }
        messageMapper.insert(message);
    }

    private boolean isWaitingForSignatures(Inspection inspection) {
        return Integer.valueOf(STATUS_PROCESSING).equals(inspection.getStatus())
                && "pass".equals(inspection.getResult())
                && hasInspectionParticipants(inspection.getId())
                && !allParticipantsSigned(inspection.getId());
    }

    private boolean hasInspectionParticipants(Long inspectionId) {
        return queryInspectionParticipants(inspectionId).size() > 0;
    }

    private boolean allSignaturesSigned(String signatures) {
        if (signatures == null || signatures.trim().length() == 0) {
            return false;
        }
        try {
            JSONArray items = JSONArray.parseArray(signatures);
            if (items.size() == 0) {
                return false;
            }
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item == null || !Boolean.TRUE.equals(item.getBoolean("signed"))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Set<Long> queryUnsignedSignerUserIds(Long inspectionId) {
        Set<Long> userIds = new HashSet<>();
        List<InspectionParticipant> participants = queryInspectionParticipants(inspectionId);
        for (InspectionParticipant participant : participants) {
            if (participant.getUserId() != null && isBlank(participant.getSignatureUrl())) {
                userIds.add(participant.getUserId());
            }
        }
        return userIds;
    }

    private boolean isBlank(Object value) {
        if (value == null) {
            return true;
        }
        String text = String.valueOf(value).trim();
        return text.length() == 0 || "[]".equals(text) || "null".equalsIgnoreCase(text);
    }

    private Long parseMemberUserId(JSONObject item) {
        if (item == null) {
            return null;
        }
        Object userId = item.get("userId");
        if (userId == null) {
            userId = item.get("accountId");
        }
        if (userId == null) {
            userId = item.get("backendUserId");
        }
        if (userId == null) {
            userId = item.get("id");
        }
        if (userId == null) {
            return null;
        }
        try {
            return Long.valueOf(String.valueOf(userId));
        } catch (Exception e) {
            return null;
        }
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
        if (inspection.getInspectorId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收负责人不能为空");
        }
        if (inspection.getStatus() != null
                && !Integer.valueOf(STATUS_PENDING).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_PROCESSING).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_COMPLETED).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_RECTIFYING).equals(inspection.getStatus())
                && !Integer.valueOf(STATUS_RECHECK_PENDING).equals(inspection.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收状态不正确");
        }
        if (inspection.getResult() != null
                && inspection.getResult().trim().length() > 0
                && !"pass".equals(inspection.getResult())
                && !"fail".equals(inspection.getResult())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收结果不正确");
        }
        return null;
    }
}
