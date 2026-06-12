package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.mapper.SeaDefinitionMapper;
import com.seagox.lowcode.service.ISeaDefinitionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeaDefinitionService implements ISeaDefinitionService {

    @Autowired
    private SeaDefinitionMapper seaDefinitionMapper;

    @Override
    public ResultData insert(SeaDefinition seaDefinition) {
        seaDefinitionMapper.insert(seaDefinition);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(SeaDefinition seaDefinition) {
        seaDefinitionMapper.updateById(seaDefinition);
        return ResultData.success(null);
    }

    @Override
    public ResultData delete(Long id) {
    	seaDefinitionMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryById(Long id, String dataSource) {
        SeaDefinition seaDefinition = seaDefinitionMapper.selectById(id);
        return ResultData.success(seaDefinition);
    }

	@Override
	public ResultData queryByBusinessType(String businessType) {
		LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaDefinition::getBusinessType, businessType);
		return ResultData.success(seaDefinitionMapper.selectList(qw));
	}

}
