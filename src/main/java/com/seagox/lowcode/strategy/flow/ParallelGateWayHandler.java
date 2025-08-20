package com.seagox.lowcode.strategy.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.SeaNode;
import com.seagox.lowcode.mapper.SeaNodeMapper;
import com.seagox.lowcode.service.impl.RuntimeService;

/**
 * 并行网关节点
 */
@Service
public class ParallelGateWayHandler implements FlowHandler {
	
	@Autowired
    private SeaNodeMapper seaNodeMapper;
	
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void afterPropertiesSet() throws Exception {
		FlowHandlerFactory.register("parallelGateWay", this);
	}

	@Override
	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables) {
		JSONObject result = new JSONObject();
		JSONObject curNode = nodeObj.getJSONObject(curNodeKey);
		boolean isComplete = true;
		LambdaQueryWrapper<SeaNode> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaNode::getDefId, processInstanceId)
    	.eq(SeaNode::getStatus, 0);
		List<SeaNode> seaNodeList = seaNodeMapper.selectList(qw);
		List<String> precedeList = new ArrayList<>();
		for(int i=0;i<seaNodeList.size();i++) {
			precedeList.add(seaNodeList.get(i).getPrecede());
		}
		if(precedeList.size() > 0) {
			List<String> intersection = precedeList.stream()
					.filter(Arrays.asList(precede.split("_"))::contains)
					.collect(Collectors.toList());
			if(intersection.size() > 0) {
				isComplete = false;
			}
		}
		
		if(isComplete) {
			SeaNode seaNode = new SeaNode();
			seaNode.setDefId(processInstanceId);
			seaNode.setMark(curNode.getString("id"));
			seaNode.setLabel(curNode.getString("label"));
			seaNode.setType(7);
			seaNode.setStatus(3);
			seaNode.setStartTime(new Date());
			seaNode.setEndTime(new Date());
			seaNode.setPath(path + "_" + curNode.getString("id"));
			seaNode.setPrecede(precede);
			seaNodeMapper.insert(seaNode);
			JSONArray target = edgesObj.getJSONArray(curNodeKey);
			if(target.size() > 1) {
				if(StringUtils.isEmpty(precede)) {
					precede = curNode.getString("id");
				} else {
					precede = precede + "," + curNode.getString("id");
				}
			}
			for(int i=0;i<target.size();i++) {
				result = runtimeService.execute(processInstanceId, target.getJSONObject(i).getString("target"), seaNode.getPath(), precede, nodeObj, edgesObj, variables);
			}
			curNode.put("status", 1);
		}
		return result;
	}
	
}
