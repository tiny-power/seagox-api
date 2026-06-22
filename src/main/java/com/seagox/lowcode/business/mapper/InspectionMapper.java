package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.Inspection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 验收单
 */
public interface InspectionMapper extends BaseMapper<Inspection> {

    List<Map<String, Object>> queryInspections(@Param("params") Map<String, Object> params);

    Map<String, Object> queryInspectionById(@Param("id") Long id);
}
