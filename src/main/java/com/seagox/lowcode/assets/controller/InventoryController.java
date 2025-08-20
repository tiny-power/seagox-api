package com.seagox.lowcode.assets.controller;

import com.seagox.lowcode.assets.entity.InventoryPlanDetail;
import com.seagox.lowcode.assets.service.IInventoryService;
import com.seagox.lowcode.common.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 盘点
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;
    
    /**
     * 更新盘点详情
     */
    @PostMapping("/updateInventoryPlan")
    public ResultData updateInventoryPlan(Long id, Integer status, String checkStr) {
        return inventoryService.updateInventoryPlan(id, status, checkStr);
    }

    /**
     * 更新盘点计划详情
     */
    @PostMapping("/updateInventoryPlanDetail")
    public ResultData updateInventoryPlanDetail(InventoryPlanDetail inventoryPlanDetail) {
        return inventoryService.updateInventoryPlanDetail(inventoryPlanDetail);
    }

}
