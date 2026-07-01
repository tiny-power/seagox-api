package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.PaymentRequest;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 请款单
 */
public interface IPaymentRequestService {

    /**
     * 分页查询请款单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询请款单详情
     *
     * @param id 请款单ID
     * @return 查询结果
     */
    ResultData queryById(Long id, Map<String, Object> params);

    /**
     * 新增请款单
     *
     * @param paymentRequest 请款单
     * @return 操作结果
     */
    ResultData insert(PaymentRequest paymentRequest);

    /**
     * 修改请款单
     *
     * @param paymentRequest 请款单
     * @return 操作结果
     */
    ResultData update(PaymentRequest paymentRequest);

    /**
     * 删除请款单
     *
     * @param id 请款单ID
     * @return 操作结果
     */
    ResultData delete(Long id);

    /**
     * 提交请款单审批
     *
     * @param id 请款单ID
     * @return 操作结果
     */
    ResultData submit(Long id);

    /**
     * 撤销请款单审批
     *
     * @param id 请款单ID
     * @return 操作结果
     */
    ResultData cancel(Long id);

}
