package com.seagox.lowcode.util;

import java.io.StringReader;
import java.util.Objects;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class JsqlparserUtils {

	public static String parser(String sql, JSONObject conditionObject) {
		try {
			CCJSqlParserManager parserManager = new CCJSqlParserManager();
			Statement statement = parserManager.parse(new StringReader(sql));
			if (statement instanceof Select) {
				Select select = (Select) statement;
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				// 获取FROM子句中的表别名
				FromItem fromItem = plainSelect.getFromItem();
				if (fromItem instanceof Table) {
					Table table = (Table) fromItem;
					Alias alias = table.getAlias();
					if (alias != null) {
						joinExpression(conditionObject, plainSelect, alias.getName());
					} else {
						joinExpression(conditionObject, plainSelect, "");
					}
				} else {
					throw new IllegalArgumentException("From语句有误");
				}
				return plainSelect.toString();
			} else {
				throw new IllegalArgumentException("非法Select语句");
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("SQL解析有误,请联系管理员");
		}
	}
	
	public static void joinExpression(JSONObject conditionObject, PlainSelect plainSelect, String aliasName) {
		if (!conditionObject.isEmpty()) {
			if(conditionObject.containsKey("companyStr")) {
				String value = conditionObject.getString("companyStr");
				try {
					String condition = "";
					if(StringUtils.isEmpty(aliasName)) {
						condition = "company_id IN(" + value + ")";
					} else {
						condition = aliasName + ".company_id IN(" + value + ")";
					}
					Expression expression = plainSelect.getWhere();
					Expression envCondition = CCJSqlParserUtil.parseCondExpression(condition);
					if (Objects.isNull(expression)) {
						plainSelect.setWhere(envCondition);
					} else {
						AndExpression andExpression = new AndExpression(expression, envCondition);
						plainSelect.setWhere(andExpression);
					}
				} catch (JSQLParserException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("非法Where语句");
				}
			}
			if(conditionObject.containsKey("userStr")) {
				String value = conditionObject.getString("userStr");
				try {
					String condition = "";
					if(StringUtils.isEmpty(aliasName)) {
						condition = "user_id IN(" + value + ")";
					} else {
						condition = aliasName + ".user_id IN(" + value + ")";
					}
					Expression expression = plainSelect.getWhere();
					Expression envCondition = CCJSqlParserUtil.parseCondExpression(condition);
					if (Objects.isNull(expression)) {
						plainSelect.setWhere(envCondition);
					} else {
						AndExpression andExpression = new AndExpression(expression, envCondition);
						plainSelect.setWhere(andExpression);
					}
				} catch (JSQLParserException e) {
					e.printStackTrace();
					throw new IllegalArgumentException("非法Where语句");
				}
			}
		}
	}
}
