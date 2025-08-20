package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

public interface IMessageService {
	
	/**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     * @param userId    用户id
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long userId, Integer status, String title);

    /**
     * 是否有未读消息
     */
    public ResultData queryUnRead(Long companyId, Long userId);

    /**
     * 处理消息
     */
    public ResultData update(Long userId, Long id);

    /**
     * 处理消息(所有未读)
     */
    public ResultData updateAll(Long userId);

    /**
     * 查询未读通知
     */
    public ResultData getUnReadMessage(Long companyId, Long userId);
}
