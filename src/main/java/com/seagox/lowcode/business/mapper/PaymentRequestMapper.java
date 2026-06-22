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

    List<Map<String, Object>> queryPaymentRequests(@Param("params") Map<String, Object> params);

    Map<String, Object> queryPaymentRequestById(@Param("id") Long id);
}
