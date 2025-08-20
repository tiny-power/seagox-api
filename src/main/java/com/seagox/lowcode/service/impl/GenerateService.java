package com.seagox.lowcode.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.entity.BusinessTable;
import com.seagox.lowcode.mapper.BusinessTableMapper;
import com.seagox.lowcode.service.IGenerateService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class GenerateService implements IGenerateService {

	@Autowired
	private BusinessTableMapper businessTableMapper;

	@Override
	public ResultData generateCode(String mark, String tableName) {
		BusinessTable businessTable = businessTableMapper.queryTableByName(tableName);
		String tableComment = businessTable.getRemark();
		List<BusinessField> businessFieldList = businessTable.getChildren();
		JSONArray columns = new JSONArray();
		for (int i = 0; i < businessFieldList.size(); i++) {
			BusinessField businessField = businessFieldList.get(i);
			JSONObject column = new JSONObject();
			String field = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, businessField.getName());
			String fieldUpper = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, businessField.getName());
			column.put("field", field);
			column.put("fieldUpper", fieldUpper);
			column.put("comment", businessField.getRemark());
			column.put("type", businessField.getType());
			column.put("javaType", getJavaType(businessField.getType()));
			columns.add(column);
		}
		// 表名转换成驼峰实体名
		String tableLower = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
		String tableUpper = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
		// 创建数据模型
		Map<String, Object> dataModel = new HashMap<>();
		dataModel.put("mark", mark);
		dataModel.put("tableComment", tableComment);
		dataModel.put("tableUpper", tableUpper);
		dataModel.put("tableLower", tableLower);
		dataModel.put("columns", columns);
		// 创建 FreeMarker 配置
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
		try {
			cfg.setDirectoryForTemplateLoading(new File("src/main/resources/template"));
			// 存储生成的文件内容
			List<String> ftlList = new ArrayList<>();
			ftlList.add("controller");
			ftlList.add("service");
			ftlList.add("impl");
			ftlList.add("mapper");
			ftlList.add("entity");
			for (int i = 0; i < ftlList.size(); i++) {
				String ftl = ftlList.get(i);
				String content = generateContent(cfg, ftl + ".ftl", dataModel);
				String fileName = "";
				String dirName = "";
				if(ftl.equals("controller")) {
					fileName = tableUpper + "Controller" + ".java";
					dirName = "src/main/java/com/seagox/lowcode/" + mark + "/" + ftl;
				} else if (ftl.equals("service")) {
					fileName = "I" + tableUpper + "Service" + ".java";
					dirName = "src/main/java/com/seagox/lowcode/" + mark + "/" + ftl;
				} else if (ftl.equals("impl")) {
					fileName = tableUpper + "Service" + ".java";
					dirName = "src/main/java/com/seagox/lowcode/" + mark + "/service/" + ftl;
				} else if (ftl.equals("mapper")) {
					fileName = tableUpper + "Mapper" +".java";
					dirName = "src/main/java/com/seagox/lowcode/" + mark + "/" + ftl;
				} else if (ftl.equals("entity")) {
					fileName = tableUpper + ".java";
					dirName = "src/main/java/com/seagox/lowcode/" + mark + "/" + ftl;
				}
				File fileDir = new File(dirName);
		        if (!fileDir.exists()) {
		            fileDir.mkdirs();
		        }
				File file = new File(fileDir, fileName);
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
					writer.write(content);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultData.success(null);
	}

	/**
	 * 生成文件内容
	 *
	 * @param cfg          FreeMarker 配置
	 * @param templateName 模板文件名
	 * @param dataModel    数据模型
	 * @return 生成的文件内容
	 * @throws IOException       如果发生 I/O 错误
	 * @throws TemplateException 如果 FreeMarker 模板处理过程中发生错误
	 */
	private static String generateContent(Configuration cfg, String templateName, Map<String, Object> dataModel)
			throws IOException, TemplateException {
		Template template = cfg.getTemplate(templateName);
		StringWriter writer = new StringWriter();
		template.process(dataModel, writer);
		return writer.toString();
	}

	private static String getJavaType(String sqlType) {
		if (sqlType.equals("integer")) {
			return "Integer";
		} else if (sqlType.equals("bigint")) {
			return "Long";
		} else if (sqlType.equals("decimal")) {
			return "Double";
		} else if (sqlType.equals("varchar") || sqlType.equals("text")) {
			return "String";
		} else if (sqlType.equals("date") || sqlType.equals("timestamp")) {
			return "Date";
		}
		return "Object";
	}

}
