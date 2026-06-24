package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.MaterialArrival;
import com.seagox.lowcode.business.mapper.MaterialArrivalMapper;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.service.IMaterialArrivalService;
import com.seagox.lowcode.business.util.MapDateFormatUtils;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * 项目数据访问对象
     */
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 分页查询材料到场记录
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = materialArrivalMapper.queryMaterialArrivals(params);
        MapDateFormatUtils.formatDateValues(list);
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
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增材料到场记录
     */
    @Override
    public ResultData insert(MaterialArrival materialArrival, Long userId) {
        ResultData verifyResult = verify(materialArrival);
        if (verifyResult != null) {
            return verifyResult;
        }
        if (projectMapper.selectById(materialArrival.getProjectId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "项目不存在");
        }

        Date now = new Date();
        materialArrival.setCreatedBy(userId);
        materialArrival.setUpdatedBy(userId);
        materialArrival.setCreatedAt(now);
        materialArrival.setUpdatedAt(now);
        materialArrivalMapper.insert(materialArrival);
        return ResultData.success(materialArrival.getId());
    }

    /**
     * 删除材料到场记录
     */
    @Override
    public ResultData delete(Long id) {
        if (materialArrivalMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "材料到场记录不存在");
        }
        materialArrivalMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 校验材料到场记录
     */
    private ResultData verify(MaterialArrival materialArrival) {
        if (materialArrival == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "材料到场记录不能为空");
        }
        if (materialArrival.getProjectId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "所属项目不能为空");
        }
        if (StringUtils.isEmpty(materialArrival.getName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "材料名称不能为空");
        }
        if (materialArrival.getArrivalAt() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "到达时间不能为空");
        }
        return null;
    }
}
