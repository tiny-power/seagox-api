package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.mapper.ConstructionLogMapper;
import com.seagox.lowcode.business.service.IConstructionLogService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 施工日志
 */
@Service
public class ConstructionLogService extends AbstractReadOnlyBusinessService implements IConstructionLogService {

    @Autowired
    private ConstructionLogMapper constructionLogMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> constructionLogMapper.queryConstructionLogs(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(constructionLogMapper.queryConstructionLogById(id), "施工日志");
    }
}
