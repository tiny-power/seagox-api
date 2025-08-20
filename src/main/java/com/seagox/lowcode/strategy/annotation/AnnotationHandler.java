package com.seagox.lowcode.strategy.annotation;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;


public interface AnnotationHandler extends InitializingBean {

	public List<String> valid(String type, int row, int col, String cellValue, String annotation, List<String> failList);
	
}
