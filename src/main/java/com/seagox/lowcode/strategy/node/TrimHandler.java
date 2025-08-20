package com.seagox.lowcode.strategy.node;

import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.seagox.lowcode.util.XmlUtils;

/**
 * trim表单式
 */
@Service
public class TrimHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("trim", this);
	}

	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		String text = XmlUtils.resolveNode(element, variables, id).trim();
		if (!StringUtils.isEmpty(text)) {
			Attribute prefix‌Attribute = element.attribute("prefix‌");
			if (prefix‌Attribute != null) {
				String value = prefix‌Attribute.getValue();
				sql.append(value);
				sql.append(" ");
				sql.append(text);
			}
			Attribute prefixOverrides‌‌Attribute = element.attribute("prefixOverrides‌");
			if (prefixOverrides‌‌Attribute != null) {
				String value = prefixOverrides‌‌Attribute.getValue();
				String[] prefixArray = value.split("\\|");
				for (int i = 0; i < prefixArray.length; i++) {
					String prefix = prefixArray[i];
					if (text.startsWith(prefix)) {
						text = text.substring(prefix.length());
					}
				}
			}
			Attribute suffix‌Attribute = element.attribute("suffix‌");
			if (suffix‌Attribute != null) {
				String value = suffix‌Attribute.getValue();
				sql.append(text);
				sql.append(" ");
				sql.append(value);
			}
			Attribute suffixOverrides‌‌Attribute = element.attribute("suffixOverrides‌");
			if (suffixOverrides‌‌Attribute != null) {
				String value = suffixOverrides‌‌Attribute.getValue();
				String[] suffixArray = value.split("\\|");
				for (int i = 0; i < suffixArray.length; i++) {
					String suffix = suffixArray[i];
					if (text.startsWith(suffix)) {
						text = text.substring(0, text.length() - suffix.length());
					}
				}
			}
		}
	}
}
