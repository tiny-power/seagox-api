package com.seagox.lowcode.strategy.node;

import java.util.Map;

import org.dom4j.Element;
import org.springframework.beans.factory.InitializingBean;


public interface NodeHandler extends InitializingBean {

	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id);
	
}
