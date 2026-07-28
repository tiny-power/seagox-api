package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.ProjectChangeOrder;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 签证单
 */
public interface IProjectChangeOrderService {

    /**
     * 分页查询签证单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询签证单详情
     *
     * @param id 签证单ID
     * @return 查询结果
     */
    ResultData queryById(Long id, Map<String, Object> params);

    /**
     * 新增签证单
     *
     * @param projectChangeOrder 签证单
     * @return 操作结果
     */
    ResultData insert(ProjectChangeOrder projectChangeOrder);

    /**
     * 修改签证单
     *
     * @param projectChangeOrder 签证单
     * @return 操作结果
     */
    ResultData update(ProjectChangeOrder projectChangeOrder);

    /**
     * 删除签证单
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    ResultData delete(Long id);

    /**
     * 提交签证单审批
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    ResultData submit(Long id);

    /**
     * 撤销签证单审批
     *
     * @param id 签证单ID
     * @return 操作结果
     */
    ResultData cancel(Long id);

}
