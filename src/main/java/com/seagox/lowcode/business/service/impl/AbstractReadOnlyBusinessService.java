package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 工程业务单据只读服务基类
 */
abstract class AbstractReadOnlyBusinessService {

    protected ResultData queryByPage(Integer pageNo, Integer pageSize,
                                     Supplier<List<Map<String, Object>>> supplier) {
        PageHelper.startPage(pageNo, pageSize);
        return ResultData.success(new PageInfo<>(supplier.get()));
    }

    protected ResultData queryById(Map<String, Object> data, String documentName) {
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, documentName + "不存在");
        }
        return ResultData.success(data);
    }
}
