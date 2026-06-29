package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.Inspection;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.InspectionMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.business.service.IInspectionService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
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
    private static final int STAGE_STATUS_COMPLETED = 5;

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
    @Override
    public ResultData insert(Inspection inspection, Long userId) {
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
        return ResultData.success(inspection.getId());
    }

    /**
     * 修改验收单
     */
    @Override
    public ResultData update(Inspection inspection, Long userId) {
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
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单已完成");
        }
        ResultData participantResult = verifyParticipant(exist, userId);
        if (participantResult != null) {
            return participantResult;
        }

        Date now = new Date();
        exist.setInspectionItems(inspection.getInspectionItems() == null ? exist.getInspectionItems() : inspection.getInspectionItems());
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
        completeProjectStage(exist, userId, now);
        return ResultData.success(null);
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
     * 删除验收单
     */
    @Override
    public ResultData delete(Long id) {
        if (inspectionMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        inspectionMapper.deleteById(id);
        return ResultData.success(null);
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
