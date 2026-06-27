package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;

public interface IDashboardService {

    /**
     * 首页数据
     */
    ResultData home(Long companyId, Long userId);
}
