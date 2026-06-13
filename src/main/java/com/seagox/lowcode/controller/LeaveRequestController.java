package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.LeaveRequest;
import com.seagox.lowcode.service.ILeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 请假单
 */
@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    @Autowired
    private ILeaveRequestService leaveRequestService;

    /**
     * 分页查询
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  Long companyId, Long applicantId, String applicantName, Integer leaveType,
                                  Integer status, String startTime, String endTime) {
        return leaveRequestService.queryByPage(pageNo, pageSize, companyId, applicantId, applicantName, leaveType,
                status, startTime, endTime);
    }

    /**
     * 查询详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return leaveRequestService.queryById(id);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增请假单")
    public ResultData insert(@Valid LeaveRequest leaveRequest) {
        return leaveRequestService.insert(leaveRequest);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改请假单")
    public ResultData update(@Valid LeaveRequest leaveRequest) {
        return leaveRequestService.update(leaveRequest);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除请假单")
    public ResultData delete(@PathVariable Long id) {
        return leaveRequestService.delete(id);
    }

    /**
     * 提交
     */
    @PostMapping("/submit/{id}")
    @LogPoint("提交请假单")
    public ResultData submit(@PathVariable Long id) {
        return leaveRequestService.submit(id);
    }

    /**
     * 撤销
     */
    @PostMapping("/cancel/{id}")
    @LogPoint("撤销请假单")
    public ResultData cancel(@PathVariable Long id) {
        return leaveRequestService.cancel(id);
    }

}
