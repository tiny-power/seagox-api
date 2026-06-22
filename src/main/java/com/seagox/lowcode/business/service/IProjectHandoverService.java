package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 交接单
 */
public interface IProjectHandoverService {

    /**
     * 分页查询交接单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询交接单详情
     *
     * @param id 交接单ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

}
