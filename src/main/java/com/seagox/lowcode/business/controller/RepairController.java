package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.Repair;
import com.seagox.lowcode.business.service.IRepairService;
import com.seagox.lowcode.common.ResultData;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报修单
 */
@RestController
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private IRepairService repairService;

    /**
     * 分页查询报修单
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return repairService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 通过ID查询报修单
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return repairService.queryById(id);
    }

    /**
     * 新增报修单
     */
    @PostMapping("/insert")
    @LogPoint("新增报修单")
    public ResultData insert(Repair repair, Long userId) {
        return repairService.insert(repair, userId);
    }

    /**
     * 修改报修单
     */
    @PostMapping("/update")
    @LogPoint("修改报修单")
    public ResultData update(Repair repair, Long userId) {
        return repairService.update(repair, userId);
    }

    /**
     * 指派报修单处理人
     */
    @PostMapping("/assign/{id}")
    @LogPoint("指派报修维修人员")
    public ResultData assign(@PathVariable Long id, Long repairMemberId,
                             @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expectedVisitAt,
                             Long userId) {
        return repairService.assign(id, repairMemberId, expectedVisitAt, userId);
    }

    /**
     * 完成报修单
     */
    @PostMapping("/complete/{id}")
    @LogPoint("提交报修维修完成")
    public ResultData complete(@PathVariable Long id, Repair repair, Long userId) {
        return repairService.complete(id, repair, userId);
    }

    /**
     * 确认报修单
     */
    @PostMapping("/confirm/{id}")
    @LogPoint("确认报修完成")
    public ResultData confirm(@PathVariable Long id, Long userId) {
        return repairService.confirm(id, userId);
    }

    /**
     * 重新维修报修单
     */
    @PostMapping("/rework/{id}")
    @LogPoint("报修重新维修")
    public ResultData rework(@PathVariable Long id, Long userId) {
        return repairService.rework(id, userId);
    }

    /**
     * 取消报修单
     */
    @PostMapping("/cancel/{id}")
    @LogPoint("取消报修单")
    public ResultData cancel(@PathVariable Long id, Long userId) {
        return repairService.cancel(id, userId);
    }

    /**
     * 删除报修单
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除报修单")
    public ResultData delete(@PathVariable Long id) {
        return repairService.delete(id);
    }
}
