package com.seagox.lowcode.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.mapper.LogMapper;
import com.seagox.lowcode.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogService implements ILogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String type, String account, String name, String uri, Integer status) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = logMapper.queryList(type, account, name, uri, status);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

}
