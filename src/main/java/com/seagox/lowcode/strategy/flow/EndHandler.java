package com.seagox.lowcode.strategy.flow;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.mapper.SeaNodeMapper;

/**
 * 结束节点
 */
@Service
public class EndHandler implements FlowHandler {
	
	@Autowired
    private SeaNodeMapper seaNodeMapper;

	@Override
	public void afterPropertiesSet() throws Exception {
		FlowHandlerFactory.register("end", this);
	}

	@Override
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		SeaNode seaNode = new SeaNode();
		seaNode.setDefId(processInstanceId);
		seaNode.setMark(curNode.getString("id"));
		seaNode.setLabel(curNode.getString("label"));
		seaNode.setType(2);
		seaNode.setStatus(3);
		seaNode.setStartTime(new Date());
		seaNode.setEndTime(new Date());
		seaNode.setPath(path + "_" + curNode.getString("id"));
		seaNodeMapper.insert(seaNode);
		curNode.put("status", 1);
		result.put("finish", true);
		return result;
	}
	
}
