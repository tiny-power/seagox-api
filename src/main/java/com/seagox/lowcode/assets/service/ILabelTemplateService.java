package com.seagox.lowcode.assets.service;

import com.seagox.lowcode.common.ResultData;

public interface ILabelTemplateService {

    /**
     * 查询全部
     */
    public ResultData queryForList(Long companyId);

    /**
     * 根据详情
     */
    public ResultData queryById(Long id);
    
}
