package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 施工日志
 */
public interface IConstructionLogService {

    /**
     * 分页查询施工日志
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询施工日志详情
     *
     * @param id 施工日志ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

}
