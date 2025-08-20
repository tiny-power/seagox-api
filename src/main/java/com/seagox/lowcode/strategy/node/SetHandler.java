package com.seagox.lowcode.strategy.node;

import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.seagox.lowcode.util.XmlUtils;

/**
 * set表单式
 */
@Service
public class SetHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("set", this);
	}

	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		String text = XmlUtils.resolveNode(element, variables, id).trim();
		if (!StringUtils.isEmpty(text)) {
			String suffix = ",";
			if (text.endsWith(suffix)) {
				text = text.substring(0, text.length() - suffix.length());
			}
			sql.append("set ");
			sql.append(text);
		}
	}
}
