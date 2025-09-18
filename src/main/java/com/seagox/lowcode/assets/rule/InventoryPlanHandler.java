package com.seagox.lowcode.assets.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.assets.mapper.AssetsMapper;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;
import com.seagox.lowcode.util.JdbcTemplateUtils;
import com.seagox.lowcode.util.VerifyHandlerResult;

@Service
public class InventoryPlanHandler implements RuleHandler {

	@Autowired
	private JdbcTemplateUtils jdbcTemplateUtils;

	@Autowired
	private AssetsMapper commonMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		RuleHandlerFactory.register("inventory_plan", this);
	}

	@Override
	public void insertBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insertAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteBefore(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteAfter(Map<String, Object> params) {
		// TODO Auto-generated method stub
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
		if (result.size() != 0) {
			jdbcTemplateUtils.batchInsert(tableName, result);
		}
	}

	@Override
	public Map<String, Object> detailData(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("tableData", commonMapper.queryInventoryDetail(params.get("id").toString()));
		return result;
	}

	@Override
	public void flowFinish(Map<String, Object> params) {
		String businessKey = params.get("businessKey").toString();
		String companyId = params.get("companyId").toString();
		List<String> zcmlList = new ArrayList<>();// 资产门类
		if (!StringUtils.isEmpty(params.get("zcml"))) {
			zcmlList = Arrays.asList(params.get("zcml").toString().split(","));
		}
		List<String> statusList = new ArrayList<>();// 资产状态
		if (!StringUtils.isEmpty(params.get("status"))) {
			statusList = Arrays.asList(params.get("status").toString().split(","));
		}
		List<String> sybmList = new ArrayList<>();// 使用部门
		if (!StringUtils.isEmpty(params.get("sybm"))) {
			sybmList = Arrays.asList(params.get("sybm").toString().split(","));
		}
		List<String> zcwzList = new ArrayList<>();// 资产位置
		if (!StringUtils.isEmpty(params.get("zcwz"))) {
			zcwzList = Arrays.asList(params.get("zcwz").toString().split(","));
		}
		List<Map<String, Object>> result = commonMapper.queryInventoryAssetsDetail(companyId, zcmlList, statusList,
				sybmList, zcwzList);
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> item = new HashMap<>();
			item.put("plan_id", businessKey);
			item.put("assets_id", result.get(i).get("id"));
			item.put("num", result.get(i).get("num"));
			item.put("actual_num", result.get(i).get("num"));
			list.add(item);
		}
		jdbcTemplate.update("update inventory_plan set status = 1 where id = ?", businessKey);
		jdbcTemplateUtils.batchInsert("inventory_plan_detail", list);
	}

	@Override
	public void printData(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}
}
