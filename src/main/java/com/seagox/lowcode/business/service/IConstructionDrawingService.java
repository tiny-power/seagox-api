package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.ConstructionDrawing;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 施工图出图
 */
public interface IConstructionDrawingService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData queryById(Long id);

    ResultData queryByProjectId(Long projectId);

    ResultData save(ConstructionDrawing constructionDrawing, Long userId);

    ResultData submit(Long id, Long userId);

    ResultData confirmRead(Long id, String roleKey, Long userId);

    ResultData cancelArchive(Long id, Long userId);

    ResultData delete(Long id);
}
