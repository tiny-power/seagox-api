package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.DesignCase;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 案例库服务
 */
public interface IDesignCaseService {

    /**
     * 分页查询案例库
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询案例详情
     *
     * @param id 案例ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

    /**
     * 新增案例
     *
     * @param designCase 案例
     * @param userId 当前用户ID
     * @return 保存结果
     */
    ResultData insert(DesignCase designCase, Long userId);

    /**
     * 更新案例
     *
     * @param designCase 案例
     * @param userId 当前用户ID
     * @return 更新结果
     */
    ResultData update(DesignCase designCase, Long userId);

    /**
     * 删除案例
     *
     * @param id 案例ID
     * @return 删除结果
     */
    ResultData delete(Long id);
}
