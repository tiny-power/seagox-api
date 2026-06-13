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
     * 分页查询
     *
     * @param pageNo 起始页
     * @param pageSize 每页大小
     * @param businessType 业务类型
     * @param name 名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  String businessType, String name) {
        return seaDefinitionService.queryByPage(pageNo, pageSize, businessType, name);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增流程定义")
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
     * 通过业务类型查询流程
     */
    @GetMapping("/queryByBusinessType/{businessType}")
    public ResultData queryByBusinessType(@PathVariable String businessType) {
        return seaDefinitionService.queryByBusinessType(businessType);
    }

}
