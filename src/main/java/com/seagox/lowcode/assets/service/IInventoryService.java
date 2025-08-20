package com.seagox.lowcode.assets.service;

import com.seagox.lowcode.assets.entity.InventoryPlanDetail;
import com.seagox.lowcode.common.ResultData;

public interface IInventoryService {
	
	/**
     * 更新盘点计划
     */
    public ResultData updateInventoryPlan(Long id, Integer status, String checkStr);
    
    /**
     * 更新盘点计划详情
     */
    public ResultData updateInventoryPlanDetail(InventoryPlanDetail inventoryPlanDetail);
    
}
