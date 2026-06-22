package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.PaymentRequest;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 请款单
 */
public interface PaymentRequestMapper extends BaseMapper<PaymentRequest> {

    /**
     * 查询请款单列表
     *
     * @param params 查询条件
     * @return 请款单列表
     */
    List<Map<String, Object>> queryPaymentRequests(@Param("params") Map<String, Object> params);

    /**
     * 查询请款单详情
     *
     * @param id 请款单ID
     * @return 请款单详情
     */
    Map<String, Object> queryPaymentRequestById(@Param("id") Long id);
}
