package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.service.IProjectHandoverService;
import com.seagox.lowcode.business.mapper.BusinessDocumentMapper;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交接单
 */
@Service
public class ProjectHandoverService extends AbstractReadOnlyBusinessService implements IProjectHandoverService {

    @Autowired
    private BusinessDocumentMapper documentMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> documentMapper.queryProjectHandovers(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(documentMapper.queryProjectHandoverById(id), "交接单");
    }
}
