package com.seagox.lowcode.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.dto.ProjectSaveRequest;
import com.seagox.lowcode.business.dto.ProjectStageSaveRequest;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.ProjectStageDependency;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.ProjectStageDependencyMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.business.service.IProjectService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 项目管理服务实现
 */
@Service
public class ProjectService implements IProjectService {

    /**
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 项目成员数据访问对象
     */
    @Autowired
    private ProjectMemberMapper memberMapper;

    /**
     * 项目阶段数据访问对象
     */
    @Autowired
    private ProjectStageMapper stageMapper;

    /**
     * 阶段依赖数据访问对象
     */
    @Autowired
    private ProjectStageDependencyMapper dependencyMapper;

    /**
     * 阶段验收条目数据访问对象
     */
    @Autowired
    private StageInspectionItemMapper inspectionMapper;

    /**
     * 分页查询项目
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String code, String name, String status) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<Project> query = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(code)) {
            query.like(Project::getCode, code);
        }
        if (!StringUtils.isEmpty(name)) {
            query.like(Project::getName, name);
        }
        if (!StringUtils.isEmpty(status)) {
            query.eq(Project::getStatus, status);
        }
        query.orderByDesc(Project::getCreatedAt);
        return ResultData.success(new PageInfo<>(projectMapper.selectList(query)));
    }

    /**
     * 查询项目详情
     */
    @Override
    public ResultData queryById(Long id) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("project", project);
        data.put("members", memberMapper.selectList(new LambdaQueryWrapper<ProjectMember>()
                .eq(ProjectMember::getProjectId, id)
                .orderByAsc(ProjectMember::getId)));

        List<ProjectStage> stages = stageMapper.selectList(new LambdaQueryWrapper<ProjectStage>()
                .eq(ProjectStage::getProjectId, id)
                .orderByAsc(ProjectStage::getId));
        List<ProjectStageDependency> dependencies = dependencyMapper.selectList(
                new LambdaQueryWrapper<ProjectStageDependency>()
                        .eq(ProjectStageDependency::getProjectId, id));
        List<Map<String, Object>> stageRows = new ArrayList<>();
        for (ProjectStage stage : stages) {
            Map<String, Object> stageRow = new HashMap<>();
            stageRow.put("stage", stage);
            List<Long> predecessorStageIds = new ArrayList<>();
            for (ProjectStageDependency dependency : dependencies) {
                if (stage.getId().equals(dependency.getStageId())) {
                    predecessorStageIds.add(dependency.getPredecessorStageId());
                }
            }
            stageRow.put("predecessorStageIds", predecessorStageIds);
            stageRow.put("inspectionItems", inspectionMapper.selectList(
                    new LambdaQueryWrapper<StageInspectionItem>()
                            .eq(StageInspectionItem::getStageId, stage.getId())
                            .orderByAsc(StageInspectionItem::getId)));
            stageRows.add(stageRow);
        }
        data.put("stages", stageRows);
        return ResultData.success(data);
    }

    /**
     * 新增项目及其关联数据
     */
    @Transactional
    @Override
    public ResultData insert(ProjectSaveRequest request, Long userId) {
        ResultData validationResult = validate(request, false);
        if (validationResult != null) {
            return validationResult;
        }

        Project project = request.getProject();
        if (projectMapper.selectCount(new LambdaQueryWrapper<Project>()
                .eq(Project::getCode, project.getCode())) > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目编号已存在");
        }

        Date now = new Date();
        project.setStatus(StringUtils.isEmpty(project.getStatus()) ? 1 : project.getStatus());
        project.setHealthStatus(StringUtils.isEmpty(project.getHealthStatus()) ? 1 : project.getHealthStatus());
        project.setCreatedBy(userId);
        project.setUpdatedBy(userId);
        project.setCreatedAt(now);
        project.setUpdatedAt(now);
        projectMapper.insert(project);
        saveChildren(project.getId(), request, userId, now);
        return ResultData.success(project.getId());
    }

    /**
     * 修改项目及其关联数据
     */
    @Transactional
    @Override
    public ResultData update(ProjectSaveRequest request, Long userId) {
        ResultData validationResult = validate(request, true);
        if (validationResult != null) {
            return validationResult;
        }

        Project project = request.getProject();
        Project original = projectMapper.selectById(project.getId());
        if (original == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        if (projectMapper.selectCount(new LambdaQueryWrapper<Project>()
                .eq(Project::getCode, project.getCode())
                .ne(Project::getId, project.getId())) > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目编号已存在");
        }

        project.setCreatedBy(original.getCreatedBy());
        project.setCreatedAt(original.getCreatedAt());
        project.setUpdatedBy(userId);
        project.setUpdatedAt(new Date());
        projectMapper.updateById(project);
        clearChildren(project.getId());
        saveChildren(project.getId(), request, userId, new Date());
        return ResultData.success(null);
    }

    /**
     * 删除项目及其关联数据
     */
    @Transactional
    @Override
    public ResultData delete(Long id) {
        if (projectMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        clearChildren(id);
        projectMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 启动项目
     */
    @Transactional
    @Override
    public ResultData start(Long id, Long userId) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }
        if (!Integer.valueOf(1).equals(project.getStatus())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "仅待启动项目可以启动");
        }
        project.setStatus(2);
        project.setUpdatedBy(userId);
        project.setUpdatedAt(new Date());
        projectMapper.updateById(project);
        return ResultData.success(null);
    }

    /**
     * 校验项目保存请求
     */
    private ResultData validate(ProjectSaveRequest request, boolean update) {
        if (request == null || request.getProject() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目基本信息不能为空");
        }
        Project project = request.getProject();
        if (update && project.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目ID不能为空");
        }
        if (StringUtils.isEmpty(project.getCode())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目编号不能为空");
        }
        if (StringUtils.isEmpty(project.getName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目名称不能为空");
        }
        if (StringUtils.isEmpty(project.getAddress())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目地址不能为空");
        }
        if (project.getPlannedStartDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划开始日期不能为空");
        }
        if (project.getPlannedEndDate() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划结束日期不能为空");
        }
        if (project.getPlannedStartDate().after(project.getPlannedEndDate())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "计划结束日期不能早于开始日期");
        }
        if (CollectionUtils.isEmpty(request.getMembers())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目角色不能为空");
        }
        if (CollectionUtils.isEmpty(request.getStages())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目阶段不能为空");
        }
        return null;
    }

    /**
     * 保存项目成员和阶段数据
     */
    private void saveChildren(Long projectId, ProjectSaveRequest request, Long userId, Date now) {
        if (request.getMembers() != null) {
            for (ProjectMember member : request.getMembers()) {
                member.setId(null);
                member.setProjectId(projectId);
                member.setStatus(StringUtils.isEmpty(member.getStatus()) ? 1 : member.getStatus());
                member.setJoinedAt(member.getJoinedAt() == null ? now : member.getJoinedAt());
                member.setCreatedBy(userId);
                member.setUpdatedBy(userId);
                member.setCreatedAt(now);
                member.setUpdatedAt(now);
                memberMapper.insert(member);
            }
        }

        if (request.getStages() != null) {
            Map<String, Long> stageIdMap = new HashMap<>();
            for (ProjectStageSaveRequest stage : request.getStages()) {
                stage.setId(null);
                stage.setProjectId(projectId);
                stage.setStatus(StringUtils.isEmpty(stage.getStatus()) ? 1 : stage.getStatus());
                stage.setCreatedBy(userId);
                stage.setUpdatedBy(userId);
                stage.setCreatedAt(now);
                stage.setUpdatedAt(now);
                stageMapper.insert(stage);
                stageIdMap.put(stage.getKey(), stage.getId());
                saveInspectionItems(projectId, stage);
            }
            saveStageDependencies(projectId, request.getStages(), stageIdMap);
        }
    }

    /**
     * 保存项目阶段前置依赖
     */
    private void saveStageDependencies(Long projectId, List<ProjectStageSaveRequest> stages,
                                       Map<String, Long> stageIdMap) {
        for (ProjectStageSaveRequest stage : stages) {
            if (CollectionUtils.isEmpty(stage.getPredecessorKeys())) {
                continue;
            }
            Long stageId = stageIdMap.get(stage.getKey());
            for (String predecessorKey : stage.getPredecessorKeys()) {
                Long predecessorStageId = stageIdMap.get(predecessorKey);
                if (predecessorStageId == null || predecessorStageId.equals(stageId)) {
                    continue;
                }
                ProjectStageDependency dependency = new ProjectStageDependency();
                dependency.setProjectId(projectId);
                dependency.setStageId(stageId);
                dependency.setPredecessorStageId(predecessorStageId);
                dependencyMapper.insert(dependency);
            }
        }
    }

    /**
     * 保存阶段验收条目
     */
    private void saveInspectionItems(Long projectId, ProjectStageSaveRequest stage) {
        if (stage.getInspectionItems() == null) {
            return;
        }
        for (StageInspectionItem inspectionItem : stage.getInspectionItems()) {
            if (StringUtils.isEmpty(inspectionItem.getName())) {
                continue;
            }
            inspectionItem.setId(null);
            inspectionItem.setProjectId(projectId);
            inspectionItem.setStageId(stage.getId());
            inspectionItem.setStatus(StringUtils.isEmpty(inspectionItem.getStatus())
                    ? 0 : inspectionItem.getStatus());
            inspectionMapper.insert(inspectionItem);
        }
    }

    /**
     * 清理项目关联数据
     */
    private void clearChildren(Long projectId) {
        inspectionMapper.delete(new LambdaQueryWrapper<StageInspectionItem>()
                .eq(StageInspectionItem::getProjectId, projectId));
        dependencyMapper.delete(new LambdaQueryWrapper<ProjectStageDependency>()
                .eq(ProjectStageDependency::getProjectId, projectId));
        stageMapper.delete(new LambdaQueryWrapper<ProjectStage>()
                .eq(ProjectStage::getProjectId, projectId));
        memberMapper.delete(new LambdaQueryWrapper<ProjectMember>()
                .eq(ProjectMember::getProjectId, projectId));
    }
}
