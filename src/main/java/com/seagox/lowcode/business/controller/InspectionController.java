package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.business.service.IInspectionService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验收单
 */
@RestController
@RequestMapping("/inspection")
public class InspectionController {

    @Autowired
    private IInspectionService inspectionService;

    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return inspectionService.queryByPage(pageNo, pageSize, params);
    }

    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return inspectionService.queryById(id);
    }
}
