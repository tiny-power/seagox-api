package com.seagox.lowcode.util;

import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

public class IfFunction extends AbstractFunction {

	private static final long serialVersionUID = 1L;

	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
		Boolean flag = FunctionUtils.getBooleanValue(arg1, env);
		//FunctionUtils.getJavaObject(arg1, env);
		return AviatorRuntimeJavaType.valueOf(flag ? arg2.getValue(env) : arg3.getValue(env));
	}

	@Override
	public String getName() {
		return "IF";
	}

}
