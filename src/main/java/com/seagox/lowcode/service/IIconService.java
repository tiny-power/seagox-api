package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

public interface IIconService {
    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param name     名称
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String name);

    /**
     * 生成
     */
    public ResultData generate();
    
}
