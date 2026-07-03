package com.seagox.lowcode.business.dto;

/**
 * AI小秘书对话消息
 */
public class AiSecretaryChatMessage {

    /**
     * 角色：user / assistant
     */
    private String role;

    /**
     * 内容
     */
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
