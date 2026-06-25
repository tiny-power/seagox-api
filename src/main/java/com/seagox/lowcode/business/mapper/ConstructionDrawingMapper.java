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

    List<Map<String, Object>> queryConstructionDrawings(@Param("params") Map<String, Object> params);

    Map<String, Object> queryConstructionDrawingById(@Param("id") Long id);

    List<Map<String, Object>> queryConstructionDrawingDetails(@Param("constructionDrawingId") Long constructionDrawingId);

    ConstructionDrawingDetail queryLatestDetail(@Param("constructionDrawingId") Long constructionDrawingId);

    int insertDetail(ConstructionDrawingDetail detail);

    int updateDetailById(ConstructionDrawingDetail detail);

    int deleteDetailsByConstructionDrawingId(@Param("constructionDrawingId") Long constructionDrawingId);
}
