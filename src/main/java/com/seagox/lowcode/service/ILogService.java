package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

public interface ILogService {

    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param account 操作人
     * @param name   名称
     * @param uri 地址
     * @param status 状态
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String type, String account, String name, String uri, Integer status);

}
