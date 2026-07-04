package com.seagox.lowcode.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.MaterialArrivalMapper;
import com.seagox.lowcode.business.service.IMcpToolService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * MCP工具服务
 */
@Service
public class McpToolService implements IMcpToolService {

    private static final String QUERY_MATERIAL_ARRIVALS = "query_material_arrivals";

    /**
     * 材料到场记录数据访问对象
     */
    @Autowired
    private MaterialArrivalMapper materialArrivalMapper;

    @Override
    public JSONArray queryDeepSeekTools() {
        JSONArray tools = new JSONArray();
        tools.add(buildQueryMaterialArrivalsTool());
        return tools;
    }

    @Override
    public JSONObject call(String name, String arguments, Long userId) {
        if (QUERY_MATERIAL_ARRIVALS.equals(name)) {
            return queryMaterialArrivals(arguments, userId);
        }
        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("message", "不支持的MCP工具：" + name);
        return result;
    }

    private JSONObject queryMaterialArrivals(String arguments, Long userId) {
        JSONObject args = parseArguments(arguments);
        Integer pageNo = normalizePageNo(args.getInteger("pageNo"));
        Integer pageSize = normalizePageSize(args.getInteger("pageSize"));

        Map<String, Object> params = new HashMap<>();
        putIfPresent(params, "projectId", args.get("projectId"));
        putIfPresent(params, "projectName", args.get("projectName"));
        putIfPresent(params, "name", args.get("name"));
        putIfPresent(params, "startDate", args.get("startDate"));
        putIfPresent(params, "endDate", args.get("endDate"));
        putIfPresent(params, "createdBy", args.get("createdBy"));
        if (Boolean.TRUE.equals(args.getBoolean("mine")) && userId != null) {
            params.put("createdBy", userId);
        }

        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = materialArrivalMapper.queryMaterialArrivals(params);
        MapDateFormatUtils.formatDateValues(list);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("tool", QUERY_MATERIAL_ARRIVALS);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("total", pageInfo.getTotal());
        result.put("records", list);
        return result;
    }

    private JSONObject parseArguments(String arguments) {
        if (!StringUtils.hasText(arguments)) {
            return new JSONObject();
        }
        return JSON.parseObject(arguments);
    }

    private Integer normalizePageNo(Integer pageNo) {
        return pageNo == null || pageNo < 1 ? 1 : pageNo;
    }

    private Integer normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }
        return Math.min(pageSize, 20);
    }

    private void putIfPresent(Map<String, Object> params, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String && !StringUtils.hasText((String) value)) {
            return;
        }
        params.put(key, value);
    }

    private JSONObject buildQueryMaterialArrivalsTool() {
        JSONObject tool = new JSONObject();
        tool.put("type", "function");

        JSONObject function = new JSONObject();
        function.put("name", QUERY_MATERIAL_ARRIVALS);
        function.put("description", "查询材料到场记录，支持按项目、材料名称、到场日期范围、创建人分页筛选。");
        function.put("parameters", buildQueryMaterialArrivalsParameters());
        tool.put("function", function);
        return tool;
    }

    private JSONObject buildQueryMaterialArrivalsParameters() {
        JSONObject parameters = new JSONObject();
        parameters.put("type", "object");

        JSONObject properties = new JSONObject();
        properties.put("projectId", buildProperty("integer", "项目ID。"));
        properties.put("projectName", buildProperty("string", "项目名称，支持模糊查询。"));
        properties.put("name", buildProperty("string", "材料名称，支持模糊查询。"));
        properties.put("startDate", buildProperty("string", "到场开始时间，格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。"));
        properties.put("endDate", buildProperty("string", "到场结束时间，格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。"));
        properties.put("createdBy", buildProperty("integer", "创建人ID。"));
        properties.put("mine", buildProperty("boolean", "是否只查询当前登录用户创建的记录。"));
        properties.put("pageNo", buildProperty("integer", "页码，默认1。"));
        properties.put("pageSize", buildProperty("integer", "每页条数，默认10，最大20。"));

        parameters.put("properties", properties);
        parameters.put("required", new JSONArray());
        return parameters;
    }

    private JSONObject buildProperty(String type, String description) {
        JSONObject property = new JSONObject();
        property.put("type", type);
        property.put("description", description);
        return property;
    }
}
