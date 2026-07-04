package com.seagox.lowcode.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.seagox.lowcode.business.dto.AiSecretaryChatMessage;
import com.seagox.lowcode.business.service.IAiSecretaryService;
import com.seagox.lowcode.business.service.IMcpToolService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * AI小秘书服务
 */
@Service
public class AiSecretaryService implements IAiSecretaryService {

    private static final String SYSTEM_PROMPT = "你是工程项目数字化管控平台的AI小秘书。"
            + "请使用简洁、专业、可执行的中文回答，优先围绕施工进度、质量问题、材料到场、付款申请、日志和待办事项提供建议。"
            + "当用户咨询材料到场记录时，优先通过MCP工具query_material_arrivals查询真实业务数据，再基于查询结果回答。"
            + "如果用户说“我的记录”，将mine参数设为true。"
            + "如果信息不足，先说明需要补充哪些关键信息。";

    /**
     * MCP工具服务
     */
    @Autowired
    private IMcpToolService mcpToolService;

    /**
     * DeepSeek API Key
     */
    @Value("${ai.deepseek.api-key:${DEEPSEEK_API_KEY:}}")
    private String apiKey;

    /**
     * DeepSeek Base URL
     */
    @Value("${ai.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    /**
     * DeepSeek 模型
     */
    @Value("${ai.deepseek.model:deepseek-v4-flash}")
    private String model;

    /**
     * 超时时间
     */
    @Value("${ai.deepseek.timeout:30000}")
    private Integer timeout;

    /**
     * 是否启用MCP工具
     */
    @Value("${ai.deepseek.mcp.enabled:true}")
    private Boolean mcpEnabled;

    @Override
    public ResultData chat(String message, String history, Long userId) {
        if (!StringUtils.hasText(message)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "请输入要咨询的内容");
        }

        if (!StringUtils.hasText(apiKey)) {
            return ResultData.warn(ResultCode.PARAMETER_ERROR, "DeepSeek API Key未配置");
        }

        try {
            JSONObject result = requestDeepSeekWithMcp(message.trim(), history, userId);
            Map<String, Object> data = new HashMap<>();
            data.put("content", extractContent(result));
            data.put("model", result.getString("model"));
            data.put("usage", result.getJSONObject("usage"));
            return ResultData.success(data);
        } catch (RestClientException e) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "DeepSeek服务请求失败，请稍后重试");
        } catch (Exception e) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "AI小秘书暂时不可用，请稍后重试");
        }
    }

    private JSONObject requestDeepSeekWithMcp(String message, String history, Long userId) {
        JSONArray messages = buildMessages(message, history);
        JSONObject result = requestDeepSeek(messages, userId, isMcpEnabled());
        JSONArray toolCalls = extractToolCalls(result);
        if (toolCalls == null || toolCalls.isEmpty()) {
            return result;
        }

        JSONObject assistantMessage = extractMessage(result);
        assistantMessage.put("content", assistantMessage.getString("content"));
        messages.add(assistantMessage);

        for (int i = 0; i < toolCalls.size(); i++) {
            JSONObject toolCall = toolCalls.getJSONObject(i);
            JSONObject function = toolCall.getJSONObject("function");
            JSONObject toolResult = mcpToolService.call(
                    function == null ? "" : function.getString("name"),
                    function == null ? "" : function.getString("arguments"),
                    userId);
            JSONObject toolMessage = new JSONObject();
            toolMessage.put("role", "tool");
            toolMessage.put("tool_call_id", toolCall.getString("id"));
            toolMessage.put("content", toolResult.toJSONString());
            messages.add(toolMessage);
        }

        return requestDeepSeek(messages, userId, false);
    }

    private JSONObject requestDeepSeek(JSONArray messages, Long userId, boolean enableTools) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("messages", messages);
        body.put("temperature", 1.0);
        body.put("max_tokens", 1600);
        body.put("stream", false);
        JSONObject thinking = new JSONObject();
        thinking.put("type", "disabled");
        body.put("thinking", thinking);
        if (userId != null) {
            body.put("user_id", "user_" + userId);
        }
        if (enableTools) {
            JSONArray tools = mcpToolService.queryDeepSeekTools();
            if (tools != null && !tools.isEmpty()) {
                body.put("tools", tools);
                body.put("tool_choice", "auto");
            }
        }

        HttpEntity<String> request = new HttpEntity<>(body.toJSONString(), headers);
        ResponseEntity<String> response = createRestTemplate()
                .exchange(normalizeBaseUrl() + "/chat/completions", HttpMethod.POST, request, String.class);
        JSONObject result = JSON.parseObject(response.getBody());
        JSONObject error = result == null ? null : result.getJSONObject("error");
        if (error != null) {
            throw new RestClientException(error.getString("message"));
        }
        return result;
    }

    private JSONArray buildMessages(String message, String history) {
        JSONArray messages = new JSONArray();
        JSONObject system = new JSONObject();
        system.put("role", "system");
        system.put("content", SYSTEM_PROMPT);
        messages.add(system);

        List<AiSecretaryChatMessage> historyMessages = parseHistory(history);
        int start = Math.max(0, historyMessages.size() - 10);
        for (int i = start; i < historyMessages.size(); i++) {
            AiSecretaryChatMessage item = historyMessages.get(i);
            if (!isAllowedRole(item.getRole()) || !StringUtils.hasText(item.getContent())) {
                continue;
            }
            JSONObject historyItem = new JSONObject();
            historyItem.put("role", item.getRole());
            historyItem.put("content", item.getContent());
            messages.add(historyItem);
        }

        JSONObject current = new JSONObject();
        current.put("role", "user");
        current.put("content", message);
        messages.add(current);
        return messages;
    }

    private List<AiSecretaryChatMessage> parseHistory(String history) {
        if (!StringUtils.hasText(history)) {
            return java.util.Collections.emptyList();
        }
        return JSON.parseArray(history, AiSecretaryChatMessage.class);
    }

    private boolean isAllowedRole(String role) {
        return "user".equals(role) || "assistant".equals(role);
    }

    private String extractContent(JSONObject result) {
        JSONObject message = extractMessage(result);
        String content = message == null ? "" : message.getString("content");
        if (!StringUtils.hasText(content)) {
            throw new RestClientException("DeepSeek message is empty");
        }
        return content;
    }

    private JSONObject extractMessage(JSONObject result) {
        JSONArray choices = result == null ? null : result.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RestClientException("DeepSeek response is empty");
        }
        JSONObject choice = choices.getJSONObject(0);
        return choice.getJSONObject("message");
    }

    private JSONArray extractToolCalls(JSONObject result) {
        JSONObject message = extractMessage(result);
        return message == null ? null : message.getJSONArray("tool_calls");
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestTemplate(factory);
    }

    private String normalizeBaseUrl() {
        return baseUrl == null ? "https://api.deepseek.com" : baseUrl.replaceAll("/+$", "");
    }

    private boolean isMcpEnabled() {
        return Boolean.TRUE.equals(mcpEnabled);
    }
}
