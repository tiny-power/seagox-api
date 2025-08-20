package com.seagox.lowcode.assets.controller;

import com.seagox.lowcode.assets.service.ILabelTemplateService;
import com.seagox.lowcode.common.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签模版
 */
@RestController
@RequestMapping("/labelTemplate")
public class LabelTemplateController {

    @Autowired
    private ILabelTemplateService labelTemplateService;

    /**
     * 查询全部
     */
    @GetMapping("/queryForList")
    public ResultData queryForList(Long companyId) {
        return labelTemplateService.queryForList(companyId);
    }

    /**
     * 查询详情
     */
    @GetMapping("/queryById")
    public ResultData queryById(Long id) {
        return labelTemplateService.queryById(id);
    }

}
