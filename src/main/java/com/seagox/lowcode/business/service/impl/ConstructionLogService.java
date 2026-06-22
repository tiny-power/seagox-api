package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.ConstructionLogMapper;
import com.seagox.lowcode.business.service.IConstructionLogService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 施工日志
 */
@Service
public class ConstructionLogService implements IConstructionLogService {

    /**
     * 施工日志数据访问对象
     */
    @Autowired
    private ConstructionLogMapper constructionLogMapper;

    /**
     * 分页查询施工日志
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = constructionLogMapper.queryConstructionLogs(params);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询施工日志详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = constructionLogMapper.queryConstructionLogById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "施工日志不存在");
        }
        return ResultData.success(data);
    }
}
