package com.seagox.lowcode.strategy.flow;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.impl.RuntimeService;
import com.seagox.lowcode.util.AviatorUtils;

/**
 * 排他网关节点
 */
@Service
public class ExclusiveGateWayHandler implements FlowHandler {
	
	@Autowired
    private SeaNodeMapper seaNodeMapper;
	
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void afterPropertiesSet() throws Exception {
		FlowHandlerFactory.register("exclusiveGateWay", this);
	}

	@Override
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		SeaNode seaNode = new SeaNode();
		seaNode.setDefId(processInstanceId);
		seaNode.setMark(curNode.getString("id"));
		seaNode.setLabel(curNode.getString("label"));
		seaNode.setType(6);
		seaNode.setStatus(3);
		seaNode.setStartTime(new Date());
		seaNode.setEndTime(new Date());
		seaNode.setPath(path + "_" + curNode.getString("id"));
		seaNode.setPrecede(precede);
		seaNodeMapper.insert(seaNode);
		JSONArray target = edgesObj.getJSONArray(curNodeKey);
		for(int i=0;i<target.size();i++) {
			JSONObject targetObject = target.getJSONObject(i);
			String condition = targetObject.getString("condition");
			if(StringUtils.isEmpty(condition)) {
				throw new IllegalArgumentException("条件分支未设置条件");
			} else {
				try {
					boolean flag = (boolean)AviatorUtils.execute(condition, variables);
					if(flag) {
						result = runtimeService.execute(processInstanceId, target.getJSONObject(i).getString("target"), seaNode.getPath(), precede, nodeObj, edgesObj, variables);
						break;
					}
				} catch (Exception e) {
					throw new IllegalArgumentException("条件分支执行错误");
				}
			}
		}
		curNode.put("status", 1);
		return result;
	}
	
}
