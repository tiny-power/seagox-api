package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 材料到场记录
 */
public interface IMaterialArrivalService {

    /**
     * 分页查询材料到场记录
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询材料到场记录详情
     *
     * @param id 材料到场记录ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

}
