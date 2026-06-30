package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;

/**
 * 项目阶段
 */
public interface IProjectStageService {

    /**
     * 启动项目阶段
     *
     * @param id 阶段ID
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData start(Long id, Long userId);
}
