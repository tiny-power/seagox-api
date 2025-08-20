package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaDefinition;
import com.seagox.lowcode.service.ISeaDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 流程定义
 */
@RestController
@RequestMapping("/seaDefinition")
public class SeaDefinitionController {

    @Autowired
    private ISeaDefinitionService seaDefinitionService;

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增表单设计")
    public ResultData insert(@Valid SeaDefinition seaDefinition) {
        return seaDefinitionService.insert(seaDefinition);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改流程定义")
    public ResultData update(@Valid SeaDefinition seaDefinition) {
        return seaDefinitionService.update(seaDefinition);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除流程定义")
    public ResultData delete(@PathVariable Long id) {
        return seaDefinitionService.delete(id);
    }

    /**
     * 详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, String dataSource) {
        return seaDefinitionService.queryById(id, dataSource);
    }
    
    /**
     * 通过表单id查询流程
     */
    @GetMapping("/queryByFormId/{formId}")
    public ResultData queryByFormId(@PathVariable Long formId) {
        return seaDefinitionService.queryByFormId(formId);
    }

}
