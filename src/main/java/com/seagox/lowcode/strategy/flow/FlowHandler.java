package com.seagox.lowcode.strategy.flow;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;

public interface FlowHandler extends InitializingBean {

	public JSONObject execute(Long processInstanceId, String curNodeKey, String path, String precede, JSONObject nodeObj, JSONObject edgesObj, Map<String, Object> variables);
	
}
