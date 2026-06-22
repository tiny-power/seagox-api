package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.dto.ProjectSaveRequest;
import com.seagox.lowcode.common.ResultData;

/**
 * 项目
 */
public interface IProjectService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, String code, String name, String status);

    ResultData queryById(Long id);

    ResultData insert(ProjectSaveRequest request, Long userId);

    ResultData update(ProjectSaveRequest request, Long userId);

    ResultData delete(Long id);
}
