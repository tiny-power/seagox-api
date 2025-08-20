package com.seagox.lowcode.service.impl;

import java.util.*;

import com.seagox.lowcode.entity.*;
import com.seagox.lowcode.mapper.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.service.IFormAthorityService;

@Service
public class FormAthorityService implements IFormAthorityService {
	
    @Autowired
    private FormAthorityMapper formAthorityMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long formId, int type) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<FormAthority> qw = new LambdaQueryWrapper<>();
        qw.eq(FormAthority::getFormId, formId).eq(FormAthority::getType, type);
        List<FormAthority> list = formAthorityMapper.selectList(qw);
        PageInfo<FormAthority> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }


    @Transactional
    @Override
    public ResultData insert(FormAthority formAthority) {
    	formAthorityMapper.insert(formAthority);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(FormAthority formAthority) {
    	formAthorityMapper.updateById(formAthority);
    	return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData delete(Long id) {
    	formAthorityMapper.deleteById(id);
        return ResultData.success(null);
    }
    
}
