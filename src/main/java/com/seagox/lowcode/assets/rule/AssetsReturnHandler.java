package com.seagox.lowcode.assets.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.seagox.lowcode.assets.mapper.AssetsMapper;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;
import com.seagox.lowcode.util.JdbcTemplateUtils;
import com.seagox.lowcode.util.VerifyHandlerResult;

/**
 * 资产归还单
 */
@Service
public class AssetsReturnHandler implements RuleHandler {
	
	@Autowired
	private JdbcTemplateUtils jdbcTemplateUtils;
	
	@Autowired
    private AssetsMapper commonMapper;

	@Autowired
	private NamedParameterJdbcTemplate nameParameterJdbcTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		RuleHandlerFactory.register("assets_return", this);
	}

	@Override
	public void insertBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insertAfter(Map<String, Object> params) {
		String businessKey = params.get("businessKey").toString();
		JSONArray tableData = JSONArray.parseArray(params.get("tableData").toString());
		List<Map<String, Object>> details = new ArrayList<>();
		for (int i = 0; i < tableData.size(); i++) {
			JSONObject item = tableData.getJSONObject(i);
			Map<String, Object> detail = new HashMap<>();
			detail.put("return_id", businessKey);
			detail.put("assets_id", item.get("assets_id"));
			details.add(detail);
		}
		jdbcTemplateUtils.batchInsert("assets_return_detail", details);
	}

	@Override
	public void updateBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateAfter(Map<String, Object> params) {
		String type = params.get("_type").toString();
		if(!type.equals("approve")) {
			String businessKey = params.get("id").toString();
			JSONArray tableData = JSONArray.parseArray(params.get("tableData").toString());
			JSONArray deleteData = JSONArray.parseArray(params.get("deleteData").toString());
			for (int i = 0; i < deleteData.size(); i++) {
				jdbcTemplateUtils.deleteById("assets_return_detail", deleteData.getLong(i));
			}
			for (int i = 0; i < tableData.size(); i++) {
				JSONObject item = tableData.getJSONObject(i);
				Map<String, Object> detail = new HashMap<>();
				detail.put("return_id", businessKey);
				detail.put("assets_id", item.get("assets_id"));
				if(item.containsKey("id")) {
					detail.put("id", item.get("id"));
					jdbcTemplateUtils.updateById("assets_return_detail", detail);
				} else {
					jdbcTemplateUtils.insert("assets_return_detail", detail);
				}
			}
		}
	}

	@Override
	public void deleteBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAfter(Map<String, Object> params) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("businessKeys", Arrays.asList(params.get("businessKeys").toString().split(",")));
		String sql = "delete from assets_return_detail where return_id in (:businessKeys)";
		nameParameterJdbcTemplate.update(sql, paramMap);
	}
	
	@Override
	public VerifyHandlerResult importVerify(HttpServletRequest request, JSONObject row) {
    	VerifyHandlerResult result = new VerifyHandlerResult(true);
    	List<String> msg = new ArrayList<>();
    	result.setMsg(msg);
    	result.setSuccess(true);
    	return result;
    }
	
	@Override
	public void importHandle(HttpServletRequest request, List<Map<String, Object>> result, Map<String, Object> params) {
        String tableName = params.get("tableName").toString();
        if(result.size() != 0) {
            jdbcTemplateUtils.batchInsert(tableName, result);
        }
    }

	@Override
	public Map<String, Object> detailData(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("tableData", commonMapper.queryReturnDetail(params.get("id").toString()));
		return result;
	}

	@Override
	public void flowFinish(Map<String, Object> params) {
		JSONArray tableData = JSONArray.parseArray(params.get("tableData").toString());
		for (int i = 0; i < tableData.size(); i++) {
			JSONObject item = tableData.getJSONObject(i);
			Map<String, Object> result = new HashMap<>();
			result.put("id", item.get("assets_id"));
			result.put("status", 1);
			jdbcTemplateUtils.updateById("assets_info", result);
		}
	}
}
