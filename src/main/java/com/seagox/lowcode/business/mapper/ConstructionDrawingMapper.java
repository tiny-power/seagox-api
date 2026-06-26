package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ConstructionDrawing;
import com.seagox.lowcode.business.entity.ConstructionDrawingDetail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 施工图出图
 */
public interface ConstructionDrawingMapper extends BaseMapper<ConstructionDrawing> {

    /**
     * 查询施工图出图列表
     */
    List<Map<String, Object>> queryConstructionDrawings(@Param("params") Map<String, Object> params);

    /**
     * 通过ID查询施工图出图
     */
    Map<String, Object> queryConstructionDrawingById(@Param("id") Long id);

    /**
     * 查询施工图出图版本明细
     */
    List<Map<String, Object>> queryConstructionDrawingDetails(@Param("constructionDrawingId") Long constructionDrawingId);

    /**
     * 查询施工图出图最新版本明细
     */
    ConstructionDrawingDetail queryLatestDetail(@Param("constructionDrawingId") Long constructionDrawingId);

    /**
     * 新增施工图出图版本明细
     */
    int insertDetail(ConstructionDrawingDetail detail);

    /**
     * 通过ID更新施工图出图版本明细
     */
    int updateDetailById(ConstructionDrawingDetail detail);

    /**
     * 删除施工图出图版本明细
     */
    int deleteDetailsByConstructionDrawingId(@Param("constructionDrawingId") Long constructionDrawingId);
}
