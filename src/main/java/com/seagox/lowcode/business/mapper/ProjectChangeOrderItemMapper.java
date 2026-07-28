package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ProjectChangeOrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 签证单工程量明细
 */
public interface ProjectChangeOrderItemMapper extends BaseMapper<ProjectChangeOrderItem> {

    /**
     * 查询签证单工程量明细
     *
     * @param orderId 签证单ID
     * @return 工程量明细
     */
    List<ProjectChangeOrderItem> queryByOrderId(@Param("orderId") Long orderId);
}
