package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Door;

public interface IDoorService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId);

    /**
     * 添加
     */
    public ResultData insert(Door door);

    /**
     * 修改
     */
    public ResultData update(Door door);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 详情
     */
    public ResultData queryById(Long id, Long userId);

    /**
     * 统计分析
     */
    public ResultData queryAnalysis(Long companyId, Long userId);

}
