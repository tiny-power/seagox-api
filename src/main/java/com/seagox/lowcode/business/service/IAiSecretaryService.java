package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;

/**
 * AI小秘书服务
 */
public interface IAiSecretaryService {

    /**
     * 发送对话消息
     *
     * @param message 当前消息
     * @param history 历史消息JSON
     * @param userId 当前用户ID
     * @return 回复内容
     */
    ResultData chat(String message, String history, Long userId);
}
