package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ProjectHandover;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 交接单
 */
public interface ProjectHandoverMapper extends BaseMapper<ProjectHandover> {

    /**
     * 查询交接单列表
     *
     * @param params 查询条件
     * @return 交接单列表
     */
    List<Map<String, Object>> queryProjectHandovers(@Param("params") Map<String, Object> params);

    /**
     * 查询交接单详情
     *
     * @param id 交接单ID
     * @return 交接单详情
     */
    Map<String, Object> queryProjectHandoverById(@Param("id") Long id);
}
