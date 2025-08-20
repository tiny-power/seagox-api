package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.TableColumnConfig;
import com.seagox.lowcode.service.ITableColumnConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表字段配置
 */
@RestController
@RequestMapping("/tableColumnConfig")
public class TableColumnConfigController {

    @Autowired
    private ITableColumnConfigService tableColumnConfigService;

    /**
     * 新增或者更新
     */
    @PostMapping("/insertOrUpdate")
    public ResultData insertOrUpdate(TableColumnConfig tableColumnConfig) {
        return tableColumnConfigService.insertOrUpdate(tableColumnConfig);
    }

}
