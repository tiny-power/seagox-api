package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ProjectChangeOrder;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 签证单
 */
public interface ProjectChangeOrderMapper extends BaseMapper<ProjectChangeOrder> {

    /**
     * 查询签证单列表
     *
     * @param params 查询条件
     * @return 签证单列表
     */
    List<Map<String, Object>> queryProjectChangeOrders(@Param("params") Map<String, Object> params);

    /**
     * 查询签证单详情
     *
     * @param id 签证单ID
     * @return 签证单详情
     */
    Map<String, Object> queryProjectChangeOrderById(@Param("id") Long id);
}
