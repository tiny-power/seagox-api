package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.service.IAiSecretaryService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI小秘书
 */
@RestController
@RequestMapping("/aiSecretary")
public class AiSecretaryController {

    /**
     * AI小秘书服务
     */
    @Autowired
    private IAiSecretaryService aiSecretaryService;

    /**
     * 对话
     *
     * @param message 当前消息
     * @param history 历史消息JSON
     * @param userId 当前用户ID
     * @return AI回复
     */
    @PostMapping("/chat")
    public ResultData chat(String message, String history, Long userId) {
        return aiSecretaryService.chat(message, history, userId);
    }
}
