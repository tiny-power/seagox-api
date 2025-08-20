package com.seagox.lowcode.strategy.annotation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.util.ExcelUtils;

/**
 * 不为空注解
 */
@Service
public class NotNullHandler implements AnnotationHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		AnnotationHandlerFactory.register("NotNull", this);
	}

	@Override
	public List<String> valid(String type , int row, int col, String cellValue, String annotation, List<String> failList) {
		JSONObject annotationJson = new JSONObject();
        String[] annotationAry = annotation.substring(type.length() + 2, annotation.length() - 1).split(",");
        for (int k = 0; k < annotationAry.length; k++) {
            annotationJson.put(annotationAry[k].split("=")[0].trim(), annotationAry[k].split("=")[1].trim());
        }
        if (StringUtils.isEmpty(cellValue)) {
            failList.add("第" + (row + 1) + "行" + ExcelUtils.letterList.get(col) + "列错误：" + annotationJson.getString("message").replace("\"", ""));
        }
		return failList;
	}
	
}
