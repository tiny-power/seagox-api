package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.service.IProjectStageService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目阶段
 */
@RestController
@RequestMapping("/projectStage")
public class ProjectStageController {

    /**
     * 项目阶段服务
     */
    @Autowired
    private IProjectStageService projectStageService;

    /**
     * 新增项目阶段
     *
     * @param projectStage 项目阶段
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/insert")
    @LogPoint("新增项目阶段")
    public ResultData insert(ProjectStage projectStage, Long userId, String inspectionItems) {
        return projectStageService.insert(projectStage, userId, inspectionItems);
    }

    /**
     * 编辑项目阶段
     *
     * @param projectStage 项目阶段
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/update")
    @LogPoint("编辑项目阶段")
    public ResultData update(ProjectStage projectStage, Long userId, String inspectionItems) {
        return projectStageService.update(projectStage, userId, inspectionItems);
    }

    /**
     * 删除项目阶段
     *
     * @param id 阶段ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除项目阶段")
    public ResultData delete(@PathVariable Long id) {
        return projectStageService.delete(id);
    }

    /**
     * 启动项目阶段
     *
     * @param id 阶段ID
     * @param userId 当前用户ID
     * @return 操作结果
     */
    @PostMapping("/start/{id}")
    @LogPoint("启动项目阶段")
    public ResultData start(@PathVariable Long id, Long userId) {
        return projectStageService.start(id, userId);
    }
}
