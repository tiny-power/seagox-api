package com.seagox.lowcode.strategy.flow;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.exception.FlowManualException;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.impl.RuntimeService;

/**
 * 手动选择节点
 */
@Service
public class ManualTaskHandler implements FlowHandler {
	
	@Autowired
    private SeaNodeMapper seaNodeMapper;
	
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void afterPropertiesSet() throws Exception {
		FlowHandlerFactory.register("manualTask", this);
	}

	@Override
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		SeaNode seaNode = new SeaNode();
		seaNode.setDefId(processInstanceId);
		seaNode.setMark(curNode.getString("id"));
		seaNode.setLabel(curNode.getString("label"));
		seaNode.setType(8);
		seaNode.setStatus(3);
		seaNode.setStartTime(new Date());
		seaNode.setEndTime(new Date());
		seaNode.setPath(path + "_" + curNode.getString("id"));
		seaNode.setPrecede(precede);
		seaNodeMapper.insert(seaNode);
		JSONArray target = edgesObj.getJSONArray(curNodeKey);
		// 指定节点
		if(variables.containsKey("appoint")) {
			String[] appointArray = variables.get("appoint").toString().split(",");
			// 多条路径
			if(appointArray.length > 1) {
				if(StringUtils.isEmpty(precede)) {
					precede = curNode.getString("id");
				} else {
					precede = precede + "," + curNode.getString("id");
				}
			}
			for(int i=0;i<appointArray.length;i++) {
				result = runtimeService.execute(processInstanceId, appointArray[i], seaNode.getPath(), precede, nodeObj, edgesObj, variables);
			}
			curNode.put("status", 1);
		} else {
			JSONArray manualNode = new JSONArray();
			for(int i=0;i<target.size();i++) {
				manualNode.add(nodeObj.getJSONObject(target.getJSONObject(i).getString("target")));
			}
			throw new FlowManualException(manualNode.toJSONString());
		}
		return result;
	}

}
