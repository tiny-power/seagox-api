package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.common.ResultData;

/**
 * 项目阶段
 */
public interface IProjectStageService {

    /**
     * 新增项目阶段
     *
     * @param projectStage 项目阶段
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData insert(ProjectStage projectStage, Long userId, String inspectionItems);

    /**
     * 编辑项目阶段
     *
     * @param projectStage 项目阶段
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData update(ProjectStage projectStage, Long userId, String inspectionItems);

    /**
     * 删除项目阶段
     *
     * @param id 阶段ID
     * @return 操作结果
     */
    ResultData delete(Long id);

    /**
     * 启动项目阶段
     *
     * @param id 阶段ID
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData start(Long id, Long userId);
}
