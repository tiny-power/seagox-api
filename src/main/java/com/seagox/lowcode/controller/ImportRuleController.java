package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.ImportRule;
import com.seagox.lowcode.service.IImportRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 导入规则
 */
@RestController
@RequestMapping("/importRule")
public class ImportRuleController {

    @Autowired
    private IImportRuleService importRuleService;

    @GetMapping("/queryByFormId/{formId}")
    public ResultData queryByFormId(@PathVariable Long formId) {
        return importRuleService.queryByFormId(formId);
    }
    
    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改规则")
    public ResultData update(ImportRule importRule) {
        return importRuleService.update(importRule);
    }

}
