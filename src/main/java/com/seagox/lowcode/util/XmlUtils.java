package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.exception.GrammarException;
import com.seagox.lowcode.strategy.node.NodeHandler;
import com.seagox.lowcode.strategy.node.NodeHandlerFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtils {

	public static String asXML(String text, JSONArray searchColumn, boolean format)
			throws DocumentException, IOException {
		Document document = DocumentHelper.parseText(text);
		Element rootElement = document.getRootElement();
		if (searchColumn != null && !searchColumn.isEmpty()) {
			Element whereElm = rootElement.element("where");
			if (whereElm == null) {
				whereElm = rootElement.addElement("where");
			}
			for (int i = 0; i < searchColumn.size(); i++) {
				JSONObject column = searchColumn.getJSONObject(i);
				String type = column.getString("type");
				String field = column.getString("field");
				String value = field + " != null and " + field + " != ''";
				if (text.indexOf(value) == -1) {
					Element ifElm = whereElm.addElement("if");
					ifElm.addAttribute("test", value);
					if (type.equals("input")) {
						ifElm.setText("AND a." + field + " like concat('%', #{" + field + "}, '%')");
					} else if (type.equals("select")) {
						if (column.getBooleanValue("multiple")) {
							ifElm.setText("\n            AND a." + field + " IN (${" + field + "})");
						} else {
							ifElm.setText("AND a." + field + " = #{" + field + "}");
						}
					} else if (type.equals("datePicker")) {
						ifElm.setText("AND a." + field + " = #{" + field + "}");
					}
				}
			}
		}
		if (format) {
			StringWriter writer = new StringWriter();
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setTrimText(false);
			outputFormat.setIndentSize(4);
			outputFormat.setExpandEmptyElements(true);
			XMLWriter xmlWriter = new XMLWriter(writer, outputFormat);
			xmlWriter.write(rootElement);
			xmlWriter.close();
			writer.close();
			return writer.toString().trim();
		} else {
			return rootElement.asXML();
		}
	}

	public static String toSql(String text, Map<String, Object> params, String id) {
		try {
			text = text.replaceAll("<!--.*?-->", "");
			text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + text;
			Document document = DocumentHelper.parseText(text);
			Element rootElement = document.getRootElement();
			return resolveNode(rootElement, params, id);
		} catch (Exception e) {
			throw new GrammarException(e.getMessage());
		}
	}

	public static String resolveNode(Element target, Map<String, Object> params, String id) {
		StringBuilder sql = new StringBuilder();
		List<Node> list = target.content();
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			String typeName = node.getNodeTypeName();
			if (typeName.equals("Text")) {
				String text = replace(node.getText(), params);
				sql.append(text);
			} else if (typeName.equals("Element")) {
				Element element = (Element) node;
				String eleName = element.getName();
				List<String> basicList = Arrays.asList("select", "insert", "update", "delete");
				if (basicList.contains(eleName)) {
					Attribute attribute = element.attribute("id");
					if (attribute != null) {
						if (attribute.getValue().equals(id)) {
							sql.append(resolveNode(element, params, id));
							break;
						}
					} else {
						throw new GrammarException(eleName + "元素id属性不存在");
					}
				} else {
					NodeHandler nodeHandler = NodeHandlerFactory.getHandler(eleName);
					if (nodeHandler != null) {
						nodeHandler.handleNode(element, sql, params, id);
					} else {
						throw new GrammarException(eleName + "元素不支持");
					}
				}
			}
		}
		String result = sql.toString();
		result = result.replaceAll("&lt;", "<");
		result = result.replaceAll("&lt;=", "<=");
		result = result.replaceAll("&gt;", ">");
		result = result.replaceAll("&gt;=", ">=");
		return result;
	}

	@SuppressWarnings("unchecked")
	public static String replace(String expression, Map<String, Object> env) {
		Pattern pattern = Pattern.compile("(\\#\\{[^}]+\\})");
		Matcher matcher = pattern.matcher(expression);
		while (matcher.find()) {
			String field = matcher.group(1).substring(2, matcher.group(1).length() - 1);
			if (env != null) {
				if(field.contains(".")) {
					String parentField = field.split("\\.")[0];
					String subField = field.split("\\.")[1];
					Map<String, Object> value = (Map<String, Object>) env.get(parentField);
					if (!StringUtils.isEmpty(value.get(subField))) {
						if (value.get(subField) instanceof String) {
							expression = expression.replaceAll("#\\{" + field + "\\}",
									"'" + StringEscapeUtils.escapeSql(value.get(subField).toString()) + "'");
						} else {
							expression = expression.replaceAll("#\\{" + field + "\\}", value.get(subField).toString());
						}
					} else {
						expression = expression.replaceAll("#\\{" + field + "\\}", "null");
					}
				} else {
					if (!StringUtils.isEmpty(env.get(field))) {
						if(field.contains(".")) {
							String parentField = field.split("\\.")[0];
							String subField = field.split("\\.")[1];
							Map<String, Object> value = (Map<String, Object>) env.get(parentField);
							if (value.get(subField) instanceof String) {
								expression = expression.replaceAll("#\\{" + field + "\\}",
										"'" + StringEscapeUtils.escapeSql(value.get(subField).toString()) + "'");
							} else {
								expression = expression.replaceAll("#\\{" + field + "\\}", value.get(subField).toString());
							}
						} else {
							if (env.get(field) instanceof String) {
								expression = expression.replaceAll("#\\{" + field + "\\}",
										"'" + StringEscapeUtils.escapeSql(env.get(field).toString()) + "'");
							} else {
								expression = expression.replaceAll("#\\{" + field + "\\}", env.get(field).toString());
							}
						}
					} else {
						expression = expression.replaceAll("#\\{" + field + "\\}", "null");
					}
				}
			} else {
				expression = expression.replaceAll("#\\{" + field + "\\}", "null");
			}
		}

		Pattern $pattern = Pattern.compile("(\\$\\{[^}]+\\})");
		Matcher $matcher = $pattern.matcher(expression);
		while ($matcher.find()) {
			String field = $matcher.group(1).substring(2, $matcher.group(1).length() - 1);
			if(field.contains(".")) {
				String parentField = field.split("\\.")[0];
				String subField = field.split("\\.")[1];
				Map<String, Object> value = (Map<String, Object>) env.get(parentField);
				if (!StringUtils.isEmpty(value.get(subField))) {
					expression = expression.replaceAll("\\$\\{" + field + "\\}",
							StringEscapeUtils.escapeSql(value.get(subField).toString()));
				} else {
					expression = expression.replaceAll("\\$\\{" + field + "\\}", "null");
				}
			} else {
				if (!StringUtils.isEmpty(env.get(field))) {
					expression = expression.replaceAll("\\$\\{" + field + "\\}",
							StringEscapeUtils.escapeSql(env.get(field).toString()));
				} else {
					expression = expression.replaceAll("\\$\\{" + field + "\\}", "null");
				}
			}
		}
		return expression;
	}
}
