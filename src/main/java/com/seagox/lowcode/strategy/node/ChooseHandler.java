package com.seagox.lowcode.strategy.node;

import java.util.List;
import java.util.Map;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.stereotype.Service;

import com.seagox.lowcode.exception.GrammarException;
import com.seagox.lowcode.util.XmlUtils;

/**
 * choose表单式
 */
@Service
public class ChooseHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("choose", this);
	}

	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		List<Node> list = element.content();
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			String typeName = node.getNodeTypeName();
			if(typeName.equals("when")) {
				Element whenElement = (Element) node;
				Attribute attribute = whenElement.attribute("test");
				if (attribute != null) {
					String attrValue = attribute.getValue();
					attrValue = attrValue.replaceAll(" and ", " && ");
					attrValue = attrValue.replaceAll(" or ", " || ");
					try {
						JexlEngine jexl = new JexlBuilder().create();
						JexlContext context = new MapContext(variables);
						JexlExpression expression = jexl.createExpression(attrValue);
						if ((boolean) expression.evaluate(context)) {
							flag = false;
							sql.append(XmlUtils.resolveNode(whenElement, variables, id));
						}
					} catch (Exception e) {
						
					}
				} else {
					throw new GrammarException("when语法缺少test属性");
				}
			} else if(typeName.equals("otherwise")) {
				if(flag) {
					XmlUtils.resolveNode(element, variables, id);
				}
			} else {
				throw new GrammarException("choose元素下" + typeName + "不支持");
			}
		}
	}
	
}
