package com.seagox.lowcode.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.ProjectStageDependency;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectStageDependencyMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.business.service.IProjectStageService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 项目阶段
 */
@Service
public class ProjectStageService implements IProjectStageService {

    /**
     * 未开始
     */
    private static final int STATUS_PENDING = 1;

    /**
     * 进行中
     */
    private static final int STATUS_PROCESSING = 2;

    /**
     * 已完成
     */
    private static final int STATUS_COMPLETED = 3;

    /**
     * 项目阶段数据访问对象
     */
    @Autowired
    private ProjectStageMapper projectStageMapper;

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 阶段验收条目数据访问对象
     */
    @Autowired
    private StageInspectionItemMapper stageInspectionItemMapper;

    /**
     * 项目阶段依赖数据访问对象
     */
    @Autowired
    private ProjectStageDependencyMapper projectStageDependencyMapper;

    /**
     * 新增项目阶段
     */
    @Transactional
    @Override
    public ResultData insert(ProjectStage projectStage, Long userId, String inspectionItems) {
        if (projectStage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段不能为空");
        }
        List<StageInspectionItem> inspectionItemList = parseInspectionItems(inspectionItems);
        if (inspectionItemList == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收条目格式不正确");
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        if (projectStage.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目ID不能为空");
        }
        if (projectMapper.selectById(projectStage.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        if (StringUtils.isEmpty(projectStage.getStageName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "阶段名称不能为空");
        }
        if (projectStage.getFlowType() == null || projectStage.getFlowType() < 1 || projectStage.getFlowType() > 6) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "流程类型不正确");
        }
        if (projectStage.getPlannedStartDate() == null || projectStage.getPlannedEndDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划时间不能为空");
        }
        if (projectStage.getPlannedStartDate().after(projectStage.getPlannedEndDate())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划完成日期不能早于开始日期");
        }
        if (projectStageMapper.selectCount(new LambdaQueryWrapper<ProjectStage>()
                .eq(ProjectStage::getProjectId, projectStage.getProjectId())
                .eq(ProjectStage::getStageName, projectStage.getStageName())) > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "阶段名称已存在");
        }
        if (inspectionItemList.isEmpty()) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请添加验收条目");
        }
        for (StageInspectionItem item : inspectionItemList) {
            if (StringUtils.isEmpty(item.getName())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "验收条目名称不能为空");
            }
        }

        Date now = new Date();
        projectStage.setId(null);
        projectStage.setStatus(STATUS_PENDING);
        projectStage.setCreatedBy(userId);
        projectStage.setCreatedAt(now);
        projectStage.setUpdatedBy(userId);
        projectStage.setUpdatedAt(now);
        projectStageMapper.insert(projectStage);
        saveInspectionItems(projectStage, inspectionItemList);
        return ResultData.success(projectStage);
    }

    /**
     * 编辑项目阶段
     */
    @Transactional
    @Override
    public ResultData update(ProjectStage projectStage, Long userId, String inspectionItems) {
        if (projectStage == null || projectStage.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段ID不能为空");
        }
        List<StageInspectionItem> inspectionItemList = parseInspectionItems(inspectionItems);
        if (inspectionItemList == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收条目格式不正确");
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }

        ProjectStage oldStage = projectStageMapper.selectById(projectStage.getId());
        if (oldStage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段不存在");
        }
        if (!Integer.valueOf(STATUS_PENDING).equals(oldStage.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "仅未开始阶段可以编辑");
        }
        if (StringUtils.isEmpty(projectStage.getStageName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "阶段名称不能为空");
        }
        if (projectStage.getFlowType() == null || projectStage.getFlowType() < 1 || projectStage.getFlowType() > 6) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "流程类型不正确");
        }
        if (projectStage.getPlannedStartDate() == null || projectStage.getPlannedEndDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划时间不能为空");
        }
        if (projectStage.getPlannedStartDate().after(projectStage.getPlannedEndDate())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划完成日期不能早于开始日期");
        }
        if (projectStageMapper.selectCount(new LambdaQueryWrapper<ProjectStage>()
                .eq(ProjectStage::getProjectId, oldStage.getProjectId())
                .eq(ProjectStage::getStageName, projectStage.getStageName())
                .ne(ProjectStage::getId, projectStage.getId())) > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "阶段名称已存在");
        }
        if (inspectionItemList.isEmpty()) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请添加验收条目");
        }
        for (StageInspectionItem item : inspectionItemList) {
            if (StringUtils.isEmpty(item.getName())) {
                return ResultData.warn(ResultCode.OTHER_ERROR, "验收条目名称不能为空");
            }
        }

        Date now = new Date();
        oldStage.setFlowType(projectStage.getFlowType());
        oldStage.setStageName(projectStage.getStageName());
        oldStage.setPlannedStartDate(projectStage.getPlannedStartDate());
        oldStage.setPlannedEndDate(projectStage.getPlannedEndDate());
        oldStage.setUpdatedBy(userId);
        oldStage.setUpdatedAt(now);
        projectStageMapper.updateById(oldStage);

        stageInspectionItemMapper.delete(new LambdaQueryWrapper<StageInspectionItem>()
                .eq(StageInspectionItem::getStageId, oldStage.getId()));
        saveInspectionItems(oldStage, inspectionItemList);
        return ResultData.success(oldStage);
    }

    /**
     * 解析阶段验收条目
     */
    private List<StageInspectionItem> parseInspectionItems(String inspectionItems) {
        if (StringUtils.isEmpty(inspectionItems)) {
            return java.util.Collections.emptyList();
        }
        try {
            return JSON.parseArray(inspectionItems, StageInspectionItem.class);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * 保存阶段验收条目
     */
    private void saveInspectionItems(ProjectStage projectStage, List<StageInspectionItem> inspectionItems) {
        for (StageInspectionItem item : inspectionItems) {
            item.setId(null);
            item.setProjectId(projectStage.getProjectId());
            item.setStageId(projectStage.getId());
            item.setStatus(StringUtils.isEmpty(item.getStatus()) ? 0 : item.getStatus());
            stageInspectionItemMapper.insert(item);
        }
    }

    /**
     * 删除项目阶段
     */
    @Transactional
    @Override
    public ResultData delete(Long id) {
        if (id == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段ID不能为空");
        }
        ProjectStage stage = projectStageMapper.selectById(id);
        if (stage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段不存在");
        }
        if (!Integer.valueOf(STATUS_PENDING).equals(stage.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "仅未开始阶段可以删除");
        }

        stageInspectionItemMapper.delete(new LambdaQueryWrapper<StageInspectionItem>()
                .eq(StageInspectionItem::getStageId, id));
        projectStageDependencyMapper.delete(new LambdaQueryWrapper<ProjectStageDependency>()
                .eq(ProjectStageDependency::getStageId, id)
                .or()
                .eq(ProjectStageDependency::getPredecessorStageId, id));
        projectStageMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 启动项目阶段
     */
    @Transactional
    @Override
    public ResultData start(Long id, Long userId) {
        if (id == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段ID不能为空");
        }
        if (userId == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前用户不能为空");
        }
        ProjectStage stage = projectStageMapper.selectById(id);
        if (stage == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段不存在");
        }
        if (Integer.valueOf(STATUS_COMPLETED).equals(stage.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "已完成的项目阶段不可以启动");
        }
        if (Integer.valueOf(STATUS_PROCESSING).equals(stage.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段已在进行中");
        }
        if (stage.getStatus() != null && !Integer.valueOf(STATUS_PENDING).equals(stage.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "当前项目阶段状态不可以启动");
        }

        Date now = new Date();
        stage.setActualStartDate(now);
        stage.setStatus(STATUS_PROCESSING);
        stage.setManagerUserId(userId);
        stage.setUpdatedBy(userId);
        stage.setUpdatedAt(now);
        projectStageMapper.updateById(stage);
        return ResultData.success(stage);
    }
}
