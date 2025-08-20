package com.seagox.lowcode.assets.service;

import org.springframework.web.bind.annotation.PathVariable;

import com.seagox.lowcode.assets.entity.ProvisionDepreciation;
import com.seagox.lowcode.common.ResultData;

/**
* 计提折旧
*/
public interface IProvisionDepreciationService {
	
	/**
     * 折旧建帐
     */
    public ResultData accountInfo(Long companyId);
    
    /**
     * 折旧详情
     */
    public ResultData queryById(@PathVariable Long id);
    
    /**
     * 建帐
     */
    public ResultData insert(ProvisionDepreciation provisionDepreciation);
    
    /**
     * 更新状态
     */
    public ResultData updateStatus(Long companyId, Long id, int status);
    
    /**
     * 反建帐
     */
    public ResultData delete(Long companyId, Long id);
    
}