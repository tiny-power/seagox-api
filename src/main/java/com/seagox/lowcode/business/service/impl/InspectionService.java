package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.service.IInspectionService;
import com.seagox.lowcode.business.mapper.BusinessDocumentMapper;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验收单
 */
@Service
public class InspectionService extends AbstractReadOnlyBusinessService implements IInspectionService {

    @Autowired
    private BusinessDocumentMapper documentMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> documentMapper.queryInspections(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(documentMapper.queryInspectionById(id), "验收单");
    }
}
