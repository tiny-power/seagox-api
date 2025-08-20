package com.seagox.lowcode.strategy.node;

import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.seagox.lowcode.exception.GrammarException;
import com.seagox.lowcode.util.XmlUtils;

/**
 * if表单式
 */
@Service
public class IfHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("if", this);
	}

	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		Attribute attribute = element.attribute("test");
		if (attribute != null) {
			String attrValue = attribute.getValue();
			attrValue = attrValue.replaceAll(" and ", " && ");
			attrValue = attrValue.replaceAll(" or ", " || ");
			try {
				JexlEngine jexl = new JexlBuilder().create();
				JexlContext context = new MapContext(variables);
				JexlExpression expression = jexl.createExpression(attrValue);
				if ((boolean) expression.evaluate(context)) {
					sql.append(XmlUtils.resolveNode(element, variables, id));
				}
			} catch (Exception e) {
				
			}
		} else {
			throw new GrammarException("if语法缺少test属性");
		}	
	}
}
