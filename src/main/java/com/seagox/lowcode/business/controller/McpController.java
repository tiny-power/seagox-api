package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.dto.McpToolCallRequest;
import com.seagox.lowcode.business.service.IMcpToolService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MCP工具
 */
@RestController
@RequestMapping("/mcp")
public class McpController {

    /**
     * MCP工具服务
     */
    @Autowired
    private IMcpToolService mcpToolService;

    /**
     * 查询工具列表
     *
     * @return 工具列表
     */
    @GetMapping("/tools")
    public ResultData tools() {
        return ResultData.success(mcpToolService.queryDeepSeekTools());
    }

    /**
     * 调用工具
     *
     * @param request 工具调用请求
     * @return 工具调用结果
     */
    @PostMapping("/call")
    public ResultData call(@RequestBody McpToolCallRequest request) {
        return ResultData.success(mcpToolService.call(request.getName(), request.getArguments(), request.getUserId()));
    }
}
