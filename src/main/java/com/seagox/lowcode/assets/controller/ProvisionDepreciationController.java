package com.seagox.lowcode.assets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seagox.lowcode.assets.entity.ProvisionDepreciation;
import com.seagox.lowcode.assets.service.IProvisionDepreciationService;
import com.seagox.lowcode.common.ResultData;

/**
* 计提折旧
*/
@RestController
@RequestMapping("/provisionDepreciation")
public class ProvisionDepreciationController {
	
	@Autowired
    private IProvisionDepreciationService provisionDepreciationService;
	
	/**
     * 折旧建帐
     */
    @GetMapping("/accountInfo")
    public ResultData accountInfo(Long companyId) {
        return provisionDepreciationService.accountInfo(companyId);
    }
    
    /**
     * 折旧详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return provisionDepreciationService.queryById(id);
    }
    
    /**
     * 建帐
     */
    @PostMapping("/insert")
    public ResultData insert(ProvisionDepreciation provisionDepreciation) {
        return provisionDepreciationService.insert(provisionDepreciation);
    }
    
    /**
     * 更新状态
     */
    @PostMapping("/updateStatus")
    public ResultData updateStatus(Long companyId, Long id, int status) {
        return provisionDepreciationService.updateStatus(companyId, id, status);
    }
    
    /**
     * 反建帐
     */
    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Long id, Long companyId) {
        return provisionDepreciationService.delete(companyId, id);
    }
    
}