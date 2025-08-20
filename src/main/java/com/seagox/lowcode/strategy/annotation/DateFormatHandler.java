package com.seagox.lowcode.strategy.annotation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.seagox.lowcode.util.ExcelUtils;

/**
 * 日期格式注解
 */
@Service
public class DateFormatHandler implements AnnotationHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		AnnotationHandlerFactory.register("DateFormat", this);
	}

	@Override
	public List<String> valid(String type, int row, int col, String cellValue, String annotation,
			List<String> failList) {
		List<String> formatList = new ArrayList<String>();
		formatList.add("yyy-MM-dd");
		formatList.add("yyy/MM/dd");
		formatList.add("yyy年MM月dd日");
		boolean result = false;
		for (int i = 0; i < formatList.size(); i++) {
			result = isValidDate(cellValue, formatList.get(i));
			if(result) {
				break;
			}
		}
		if(!result) {
			failList.add("第" + (row + 1) + "行" + ExcelUtils.letterList.get(col) + "列错误：日期格式不对");
		}
		return failList;
	}

	public boolean isValidDate(String value, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		try {
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
