package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.service.IDashboardService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页看板
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    /**
     * 首页数据
     */
    @GetMapping("/home")
    public ResultData home(Long companyId, Long userId) {
        return dashboardService.home(companyId, userId);
    }
}
