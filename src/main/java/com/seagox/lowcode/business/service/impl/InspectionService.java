package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.InspectionMapper;
import com.seagox.lowcode.business.service.IInspectionService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验收单
 */
@Service
public class InspectionService implements IInspectionService {

    /**
     * 验收单数据访问对象
     */
    @Autowired
    private InspectionMapper inspectionMapper;

    /**
     * 分页查询验收单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = inspectionMapper.queryInspections(params);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询验收单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = inspectionMapper.queryInspectionById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "验收单不存在");
        }
        return ResultData.success(data);
    }
}
