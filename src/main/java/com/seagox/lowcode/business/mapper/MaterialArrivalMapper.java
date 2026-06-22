package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.MaterialArrival;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 材料到场记录
 */
public interface MaterialArrivalMapper extends BaseMapper<MaterialArrival> {

    List<Map<String, Object>> queryMaterialArrivals(@Param("params") Map<String, Object> params);

    Map<String, Object> queryMaterialArrivalById(@Param("id") Long id);
}
