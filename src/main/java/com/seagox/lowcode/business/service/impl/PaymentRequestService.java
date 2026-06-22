package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.mapper.PaymentRequestMapper;
import com.seagox.lowcode.business.service.IPaymentRequestService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请款单
 */
@Service
public class PaymentRequestService extends AbstractReadOnlyBusinessService implements IPaymentRequestService {

    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> paymentRequestMapper.queryPaymentRequests(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(paymentRequestMapper.queryPaymentRequestById(id), "请款单");
    }
}
