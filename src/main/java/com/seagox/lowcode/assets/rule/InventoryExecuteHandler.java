package com.seagox.lowcode.assets.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.assets.mapper.AssetsMapper;
import com.seagox.lowcode.strategy.rule.RuleHandler;
import com.seagox.lowcode.strategy.rule.RuleHandlerFactory;
import com.seagox.lowcode.util.JdbcTemplateUtils;
import com.seagox.lowcode.util.VerifyHandlerResult;

@Service
public class InventoryExecuteHandler implements RuleHandler {
	
	@Autowired
	private JdbcTemplateUtils jdbcTemplateUtils;
	
	@Autowired
    private AssetsMapper commonMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		RuleHandlerFactory.register("inventory_execute", this);
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
        if(result.size() != 0) {
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
		
	}
}
