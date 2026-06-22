package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 验收单
 */
public interface IInspectionService {

    /**
     * 分页查询验收单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询验收单详情
     *
     * @param id 验收单ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

}
