package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Gauge;

public interface IGaugeService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name);

    /**
     * 添加
     */
    public ResultData insert(Gauge gauge);

    /**
     * 修改
     */
    public ResultData update(Gauge gauge);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 详情
     */
    public ResultData queryById(Long id, Long userId);

    /**
     * 查询全部通过公司id
     */
    public ResultData queryByCompanyId(Long companyId);

    /**
     * 图标sql
     */
    public ResultData chartSql(String tableName, String dimension, String metrics, String filterData);

    /**
     * 查询全部
     */
    public ResultData queryAll();

}
