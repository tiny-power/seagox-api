package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.service.IMaterialArrivalService;
import com.seagox.lowcode.business.mapper.BusinessDocumentMapper;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 材料到场记录
 */
@Service
public class MaterialArrivalService extends AbstractReadOnlyBusinessService implements IMaterialArrivalService {

    @Autowired
    private BusinessDocumentMapper documentMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> documentMapper.queryMaterialArrivals(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(documentMapper.queryMaterialArrivalById(id), "材料到场记录");
    }
}
