package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.service.IProjectService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务管理
 */
@RestController
@RequestMapping("/finance")
public class FinanceController {

    /**
     * 项目服务
     */
    @Autowired
    private IProjectService projectService;

    /**
     * 分页查询项目财务
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  String code, String name) {
        return projectService.queryFinanceByPage(pageNo, pageSize, code, name);
    }
}
