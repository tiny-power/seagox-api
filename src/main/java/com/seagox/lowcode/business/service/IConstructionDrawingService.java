package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.ConstructionDrawing;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 施工图出图
 */
public interface IConstructionDrawingService {

    /**
     * 分页查询施工图出图
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 通过ID查询施工图出图
     */
    ResultData queryById(Long id);

    /**
     * 通过项目ID查询施工图出图
     */
    ResultData queryByProjectId(Long projectId);

    /**
     * 保存施工图出图
     */
    ResultData save(ConstructionDrawing constructionDrawing, Long userId);

    /**
     * 提交施工图出图
     */
    ResultData submit(Long id, Long userId);

    /**
     * 确认阅读施工图出图
     */
    ResultData confirmRead(Long id, String roleKey, Long userId);

    /**
     * 取消归档施工图出图
     */
    ResultData cancelArchive(Long id, Long userId);

    /**
     * 删除施工图出图
     */
    ResultData delete(Long id);
}
