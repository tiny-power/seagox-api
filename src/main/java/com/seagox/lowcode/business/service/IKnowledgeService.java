package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.Knowledge;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 科普内容服务
 */
public interface IKnowledgeService {

    /**
     * 分页查询科普内容
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询上架科普内容
     *
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryList(Map<String, Object> params);

    /**
     * 查询科普内容详情
     *
     * @param id 科普内容ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

    /**
     * 新增科普内容
     *
     * @param knowledge 科普内容
     * @return 保存结果
     */
    ResultData insert(Knowledge knowledge);

    /**
     * 更新科普内容
     *
     * @param knowledge 科普内容
     * @return 更新结果
     */
    ResultData update(Knowledge knowledge);

    /**
     * 删除科普内容
     *
     * @param id 科普内容ID
     * @return 删除结果
     */
    ResultData delete(Long id);
}
