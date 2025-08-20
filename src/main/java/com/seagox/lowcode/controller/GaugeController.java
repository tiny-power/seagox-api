package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Gauge;
import com.seagox.lowcode.service.IGaugeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 仪表盘
 */
@RestController
@RequestMapping("/gauge")
public class GaugeController {

    @Autowired
    private IGaugeService gaugeService;

    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     * @param name      名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, String name) {
        return gaugeService.queryByPage(pageNo, pageSize, companyId, name);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public ResultData insert(@Valid Gauge gauge) {
        return gaugeService.insert(gauge);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResultData update(@Valid Gauge gauge) {
        return gaugeService.update(gauge);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Long id) {
        return gaugeService.delete(id);
    }

    /**
     * 信息
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, Long userId) {
        return gaugeService.queryById(id, userId);
    }

    /**
     * 查询全部通过公司id
     */
    @GetMapping("/queryByCompanyId")
    public ResultData queryByCompanyId(Long companyId) {
        return gaugeService.queryByCompanyId(companyId);
    }

    /**
     * 查询全部
     */
    @GetMapping("/queryAll")
    public ResultData queryAll() {
        return gaugeService.queryAll();
    }

    /**
     * 图标sql
     */
    @PostMapping("/chartSql")
    public ResultData chartSql(String tableName, String dimension, String metrics, String filterData) {
        return gaugeService.chartSql(tableName, dimension, metrics, filterData);
    }

}
