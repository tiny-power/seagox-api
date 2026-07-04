package com.seagox.lowcode.business.dto;

/**
 * MCP工具调用请求
 */
public class McpToolCallRequest {

    /**
     * 工具名称
     */
    private String name;

    /**
     * 工具参数JSON
     */
    private String arguments;

    /**
     * 当前用户ID
     */
    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
