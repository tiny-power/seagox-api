package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.service.IProjectStageService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
