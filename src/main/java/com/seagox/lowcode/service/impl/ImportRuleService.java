package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.*;
import com.seagox.lowcode.mapper.*;
import com.seagox.lowcode.service.IImportRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportRuleService implements IImportRuleService {

    @Autowired
    private ImportRuleMapper importRuleMapper;
    
    @Override
	public ResultData queryByFormId(Long formId) {
    	LambdaQueryWrapper<ImportRule> qw = new LambdaQueryWrapper<>();
		qw.eq(ImportRule::getFormId, formId);
		ImportRule importRule = importRuleMapper.selectOne(qw);
		if(importRule == null) {
			importRule = new ImportRule();
			importRule.setFormId(formId);
			importRule.setStartLine(2);
			importRule.setRules("[]");
			importRuleMapper.insert(importRule);
		}
		return ResultData.success(importRule);
	}
    
    @Override
    public ResultData update(ImportRule importRule) {
    	if(importRule.getId() != null) {
    		importRuleMapper.updateById(importRule);
    	} else {
    		importRuleMapper.insert(importRule);
    	}
        return ResultData.success(null);
    }

}
