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

    /**
     * 查询材料到场记录列表
     *
     * @param params 查询条件
     * @return 材料到场记录列表
     */
    List<Map<String, Object>> queryMaterialArrivals(@Param("params") Map<String, Object> params);

    /**
     * 查询材料到场记录详情
     *
     * @param id 材料到场记录ID
     * @return 材料到场记录详情
     */
    Map<String, Object> queryMaterialArrivalById(@Param("id") Long id);
}
