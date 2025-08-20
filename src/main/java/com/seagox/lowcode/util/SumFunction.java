package com.seagox.lowcode.util;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class SumFunction extends AbstractVariadicFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
		Double sum = 0D;
		for (AviatorObject arg : args) {
			Number number = FunctionUtils.getNumberValue(arg, env);
			sum += number.doubleValue();
		}
		return new AviatorDouble(sum);
	}

	@Override
	public String getName() {
		return "SUM";
	}

}
