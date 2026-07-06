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
    ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String code, String name, String status);

    ResultData queryFinanceByPage(Integer pageNo, Integer pageSize, Long projectId);

    /**
     * 查询项目详情
     */
    ResultData queryById(Long id);

    /**
     * 新增项目及其关联数据
     */
    ResultData insert(ProjectSaveRequest request, Long userId, Long companyId);

    /**
     * 修改项目及其关联数据
     */
    ResultData update(ProjectSaveRequest request, Long userId, Long companyId);

    /**
     * 删除项目及其关联数据
     */
    ResultData delete(Long id);

    /**
     * 启动项目
     */
    ResultData start(Long id, Long userId);

    /**
     * 修改项目状态
     */
    ResultData updateStatus(Long id, Integer status, Long userId);
}
