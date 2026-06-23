package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.PaymentRequestMapper;
import com.seagox.lowcode.business.service.IPaymentRequestService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 请款单
 */
@Service
public class PaymentRequestService implements IPaymentRequestService {

    /**
     * 请款单数据访问对象
     */
    @Autowired
    private PaymentRequestMapper paymentRequestMapper;

    /**
     * 分页查询请款单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = paymentRequestMapper.queryPaymentRequests(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询请款单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = paymentRequestMapper.queryPaymentRequestById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请款单不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }
}
