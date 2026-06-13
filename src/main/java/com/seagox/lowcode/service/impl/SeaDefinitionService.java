package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.mapper.SeaDefinitionMapper;
import com.seagox.lowcode.service.ISeaDefinitionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeaDefinitionService implements ISeaDefinitionService {

    @Autowired
    private SeaDefinitionMapper seaDefinitionMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String businessType, String name) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
        qw.eq(!StringUtils.isEmpty(businessType), SeaDefinition::getBusinessType, businessType)
        .like(!StringUtils.isEmpty(name), SeaDefinition::getName, name)
        .orderByDesc(SeaDefinition::getUpdateTime);
        List<SeaDefinition> list = seaDefinitionMapper.selectList(qw);
        PageInfo<SeaDefinition> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData insert(SeaDefinition seaDefinition) {
        ResultData resultData = verifyBusinessTypeUnique(seaDefinition.getBusinessType(), null);
        if (resultData != null) {
            return resultData;
        }
        seaDefinitionMapper.insert(seaDefinition);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(SeaDefinition seaDefinition) {
        if (!StringUtils.isEmpty(seaDefinition.getBusinessType())) {
            ResultData resultData = verifyBusinessTypeUnique(seaDefinition.getBusinessType(), seaDefinition.getId());
            if (resultData != null) {
                return resultData;
            }
        }
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
        if (seaDefinition != null) {
            seaDefinition.setOperationAuthority(new ArrayList<>());
        }
        return ResultData.success(seaDefinition);
    }

	@Override
	public ResultData queryByBusinessType(String businessType) {
		LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
		qw.eq(SeaDefinition::getBusinessType, businessType);
		return ResultData.success(seaDefinitionMapper.selectList(qw));
	}

    private ResultData verifyBusinessTypeUnique(String businessType, Long id) {
        if (StringUtils.isEmpty(businessType)) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "请选择业务类型");
        }
        LambdaQueryWrapper<SeaDefinition> qw = new LambdaQueryWrapper<>();
        qw.eq(SeaDefinition::getBusinessType, businessType)
        .ne(id != null, SeaDefinition::getId, id);
        Long count = seaDefinitionMapper.selectCount(qw);
        if (count > 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "该业务类型已存在流程定义");
        }
        return null;
    }

}
