package com.seagox.lowcode.business.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * MCP工具服务
 */
public interface IMcpToolService {

    /**
     * 查询DeepSeek工具定义
     *
     * @return 工具定义
     */
    JSONArray queryDeepSeekTools();

    /**
     * 调用MCP工具
     *
     * @param name 工具名称
     * @param arguments 工具参数JSON
     * @param userId 当前用户ID
     * @return 工具结果
     */
    JSONObject call(String name, String arguments, Long userId);
}
