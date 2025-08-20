package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.ImportRule;

public interface IImportRuleService {
	
	public ResultData queryByFormId(Long formId);

    /**
     * 修改
     */
    public ResultData update(ImportRule importRule);

}
