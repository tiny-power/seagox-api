package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.service.IPaymentRequestService;
import com.seagox.lowcode.business.mapper.BusinessDocumentMapper;
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
    private BusinessDocumentMapper documentMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> documentMapper.queryPaymentRequests(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(documentMapper.queryPaymentRequestById(id), "请款单");
    }
}
