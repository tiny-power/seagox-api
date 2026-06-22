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

    /**
     * 查询验收单列表
     *
     * @param params 查询条件
     * @return 验收单列表
     */
    List<Map<String, Object>> queryInspections(@Param("params") Map<String, Object> params);

    /**
     * 查询验收单详情
     *
     * @param id 验收单ID
     * @return 验收单详情
     */
    Map<String, Object> queryInspectionById(@Param("id") Long id);
}
