package com.seagox.lowcode.strategy.annotation;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.util.ExcelUtils;

/**
 * 正则表达式注解
 */
@Service
public class PatternHandler implements AnnotationHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		AnnotationHandlerFactory.register("Pattern", this);
	}

	@Override
	public List<String> valid(String type, int row, int col, String cellValue, String annotation, List<String> failList) {
		JSONObject annotationJson = new JSONObject();
        String[] annotationAry = annotation.substring(type.length() + 2, annotation.length() - 1).split(",");
        for (int k = 0; k < annotationAry.length; k++) {
            annotationJson.put(annotationAry[k].split("=")[0].trim(), annotationAry[k].split("=")[1].trim());
        }
        String regexp = annotationJson.getString("regexp");
        if (!Pattern.matches(regexp, cellValue)) {
            failList.add("第" + (row + 1) + "行" + ExcelUtils.letterList.get(col) + "列错误：" + annotationJson.getString("message").replace("\"", ""));
        }
		return failList;
	}

}
