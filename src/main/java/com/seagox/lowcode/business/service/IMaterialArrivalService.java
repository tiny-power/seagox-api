package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 材料到场记录
 */
public interface IMaterialArrivalService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData queryById(Long id);

}
