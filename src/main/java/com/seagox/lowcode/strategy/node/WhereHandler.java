package com.seagox.lowcode.strategy.node;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.seagox.lowcode.util.XmlUtils;

/**
 * where表单式
 */
@Service
public class WhereHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("where", this);
	}

	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		String text = XmlUtils.resolveNode(element, variables, id).trim();
		if (!StringUtils.isEmpty(text)) {
			List<String> prefixList = Arrays.asList("AND ", "and ", "OR ", "or ");
			for (int i = 0; i < prefixList.size(); i++) {
				String prefix = prefixList.get(i);
				if (text.startsWith(prefix)) {
					text = text.substring(prefix.length());
				}
			}
			sql.append("where ");
			sql.append(text);
		}
	}
}
