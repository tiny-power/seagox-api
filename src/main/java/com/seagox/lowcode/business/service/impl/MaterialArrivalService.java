package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.MaterialArrivalMapper;
import com.seagox.lowcode.business.service.IMaterialArrivalService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 材料到场记录
 */
@Service
public class MaterialArrivalService implements IMaterialArrivalService {

    /**
     * 材料到场记录数据访问对象
     */
    @Autowired
    private MaterialArrivalMapper materialArrivalMapper;

    /**
     * 分页查询材料到场记录
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = materialArrivalMapper.queryMaterialArrivals(params);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询材料到场记录详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = materialArrivalMapper.queryMaterialArrivalById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "材料到场记录不存在");
        }
        return ResultData.success(data);
    }
}
