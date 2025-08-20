package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;

public interface IGenerateService {
	
	public ResultData generateCode(String mark, String tableName);
}
