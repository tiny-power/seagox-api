package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.dto.ProjectSaveRequest;
import com.seagox.lowcode.common.ResultData;

/**
 * 项目
 */
public interface IProjectService {

    /**
     * 分页查询项目
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, String code, String name, String status);

    /**
     * 查询项目详情
     */
    ResultData queryById(Long id);

    /**
     * 新增项目及其关联数据
     */
    ResultData insert(ProjectSaveRequest request, Long userId);

    /**
     * 修改项目及其关联数据
     */
    ResultData update(ProjectSaveRequest request, Long userId);

    /**
     * 删除项目及其关联数据
     */
    ResultData delete(Long id);
}
