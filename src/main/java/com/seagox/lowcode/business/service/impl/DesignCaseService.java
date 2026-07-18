package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.entity.DesignCase;
import com.seagox.lowcode.business.mapper.DesignCaseMapper;
import com.seagox.lowcode.business.service.IDesignCaseService;
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
 * 案例库服务实现
 */
@Service
public class DesignCaseService implements IDesignCaseService {

    /**
     * 案例库数据访问对象
     */
    @Autowired
    private DesignCaseMapper designCaseMapper;

    /**
     * 分页查询案例库
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = designCaseMapper.queryDesignCases(params);
        MapDateFormatUtils.formatDateValues(list);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询案例详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = designCaseMapper.queryDesignCaseById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例不存在");
        }
        MapDateFormatUtils.formatDateValues(data);
        return ResultData.success(data);
    }

    /**
     * 新增案例
     */
    @Override
    public ResultData insert(DesignCase designCase, Long userId) {
        ResultData verifyResult = verify(designCase);
        if (verifyResult != null) {
            return verifyResult;
        }

        Date now = new Date();
        designCase.setCreatedBy(userId);
        designCase.setUpdatedBy(userId);
        designCase.setCreatedAt(now);
        designCase.setUpdatedAt(now);
        if (designCase.getStatus() == null) {
            designCase.setStatus(0);
        }
        if (designCase.getSort() == null) {
            designCase.setSort(0);
        }
        designCaseMapper.insert(designCase);
        return ResultData.success(designCase.getId());
    }

    /**
     * 更新案例
     */
    @Override
    public ResultData update(DesignCase designCase, Long userId) {
        if (designCase == null || designCase.getId() == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例不能为空");
        }
        if (designCaseMapper.selectById(designCase.getId()) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例不存在");
        }
        ResultData verifyResult = verify(designCase);
        if (verifyResult != null) {
            return verifyResult;
        }

        designCase.setUpdatedBy(userId);
        designCase.setUpdatedAt(new Date());
        designCaseMapper.updateById(designCase);
        return ResultData.success(null);
    }

    /**
     * 删除案例
     */
    @Override
    public ResultData delete(Long id) {
        if (designCaseMapper.selectById(id) == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例不存在");
        }
        designCaseMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 校验案例
     */
    private ResultData verify(DesignCase designCase) {
        if (designCase == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例不能为空");
        }
        if (StringUtils.isEmpty(designCase.getTitle())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例标题不能为空");
        }
        if (StringUtils.isEmpty(designCase.getCoverUrl())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "封面图片不能为空");
        }
        if (StringUtils.isEmpty(designCase.getSummary())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "案例简介不能为空");
        }
        if (StringUtils.isEmpty(designCase.getStyleList())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "风格不能为空");
        }
        if (StringUtils.isEmpty(designCase.getRegionText())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "地区不能为空");
        }
        if (StringUtils.isEmpty(designCase.getProvinceName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "省名称不能为空");
        }
        if (StringUtils.isEmpty(designCase.getCityName())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "城市名称不能为空");
        }
        if (StringUtils.isEmpty(designCase.getRoofType())) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "屋顶类型不能为空");
        }
        return null;
    }
}
