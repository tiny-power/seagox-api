package com.seagox.lowcode.strategy.annotation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seagox.lowcode.util.ExcelUtils;

/**
 * 最大值注解
 */
@Service
public class MaxHandler implements AnnotationHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		AnnotationHandlerFactory.register("Max", this);
	}

	@Override
	public List<String> valid(String type, int row, int col, String cellValue, String annotation, List<String> failList) {
		int min = Integer.valueOf(annotation.substring(type.length() + 2, annotation.length() - 1));
		if (Integer.valueOf(cellValue) <= min) {
			failList.add("第" + (row + 1) + "行" + ExcelUtils.letterList.get(col) + "列错误：" + "数值不能大于" + min);
		}
		return failList;
	}

}
