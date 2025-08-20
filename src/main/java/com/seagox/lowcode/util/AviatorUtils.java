package com.seagox.lowcode.util;

import java.util.Map;

import com.googlecode.aviator.AviatorEvaluator;

public class AviatorUtils {
	
	public static Object execute(String expression, Map<String, Object> env) {
		
		return AviatorEvaluator.execute(expression, env);
	}
}
