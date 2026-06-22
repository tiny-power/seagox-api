package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.ProjectHandoverMapper;
import com.seagox.lowcode.business.service.IProjectHandoverService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交接单
 */
@Service
public class ProjectHandoverService implements IProjectHandoverService {

    /**
     * 交接单数据访问对象
     */
    @Autowired
    private ProjectHandoverMapper projectHandoverMapper;

    /**
     * 分页查询交接单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = projectHandoverMapper.queryProjectHandovers(params);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询交接单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = projectHandoverMapper.queryProjectHandoverById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "交接单不存在");
        }
        return ResultData.success(data);
    }
}
