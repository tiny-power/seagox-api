package com.seagox.lowcode.strategy.node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.seagox.lowcode.exception.GrammarException;
import com.seagox.lowcode.util.XmlUtils;

/**
 * foreach表单式
 */
@Service
public class ForeachHandler implements NodeHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		NodeHandlerFactory.register("foreach", this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleNode(Element element, StringBuilder sql, Map<String, Object> variables, String id) {
		Attribute collectionAttribute = element.attribute("collection");
		if (collectionAttribute == null) {
			throw new GrammarException("foreach语法缺少collection属性");
		}
		Attribute itemAttribute = element.attribute("item");
		if (itemAttribute == null) {
			throw new GrammarException("foreach语法缺少item属性");
		}
		String collection = collectionAttribute.getValue();
		if (variables.get(collection) == null) {
			// 语法错误
			throw new GrammarException(collectionAttribute.getValue() + "参数不能为空");
		}
		Attribute indexAttribute = element.attribute("index");
		Attribute separatorAttribute = element.attribute("separator");
		Attribute openAttribute = element.attribute("open");
		Attribute closeAttribute = element.attribute("close");
		String item = itemAttribute.getValue();
		String separator = "";
		if (separatorAttribute != null) {
			separator = separatorAttribute.getValue();
		}
		String index = "";
		if (index != null) {
			index = indexAttribute.getValue();
		}
		String open = "";
		if (openAttribute != null) {
			open = openAttribute.getValue();
		}
		String close = "";
		if (closeAttribute != null) {
			close = closeAttribute.getValue();
		}
		sql.append(open);
		if (variables.get(collection).getClass().isArray()) {
			// array
			Object[] collectionArray = (Object[]) variables.get(collection);
			for (int j = 0; j < collectionArray.length; j++) {
				variables.put(item, collectionArray[j]);
				if (!StringUtils.isEmpty(index)) {
					variables.put(index, j);
				}
				sql.append(XmlUtils.resolveNode(element, variables, id));
				if (j != (collectionArray.length - 1)) {
					sql.append(separator);
				}
			}
		} else if (variables.get(collection) instanceof Collection) {
			// list
			List<Object> collectionList = (List<Object>) variables.get(collection);
			for (int j = 0; j < collectionList.size(); j++) {
				variables.put(item, collectionList.get(j));
				if (!StringUtils.isEmpty(index)) {
					variables.put(index, j);
				}
				sql.append(XmlUtils.resolveNode(element, variables, id));
				if (j != (collectionList.size() - 1)) {
					sql.append(separator);
				}
			}
		} else {
			throw new GrammarException("foreach元素中collection属性" + collectionAttribute.getValue() + "参数不合法");
		}
		sql.append(close);
		variables.remove(item);
		if (!StringUtils.isEmpty(index)) {
			variables.remove(index);
		}
	}

}
