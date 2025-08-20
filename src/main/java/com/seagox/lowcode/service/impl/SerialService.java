package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Serial;
import com.seagox.lowcode.mapper.SerialMapper;
import com.seagox.lowcode.service.ISerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerialService implements ISerialService {

    @Autowired
    private SerialMapper serialMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<Serial> qw = new LambdaQueryWrapper<>();
    	qw.eq(Serial::getCompanyId, companyId)
    	.orderByDesc(Serial::getCreateTime);
        List<Serial> list = serialMapper.selectList(qw);
        PageInfo<Serial> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }
    
    @Override
    public ResultData queryAll(Long companyId) {
        LambdaQueryWrapper<Serial> qw = new LambdaQueryWrapper<>();
    	qw.eq(Serial::getCompanyId, companyId);
        List<Serial> list = serialMapper.selectList(qw);
        return ResultData.success(list);
    }

    @Override
    public ResultData insert(Serial serial) {
    	serialMapper.insert(serial);
        return ResultData.success(null);
    }

    @Override
    public ResultData update(Serial serial) {
    	serialMapper.updateById(serial);
        return ResultData.success(null);
    }

    @Override
    public ResultData delete(Long id) {
    	serialMapper.deleteById(id);
        return ResultData.success(null);
    }
    
}
