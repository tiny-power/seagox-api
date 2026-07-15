package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.business.service.ILeaveRequestService;
import com.seagox.lowcode.common.ResultData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 请假单
 */
@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    /**
     * 请假单服务
     */
    @Autowired
    private ILeaveRequestService leaveRequestService;

    /**
     * 分页查询
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  Long companyId, Long applicantId, String applicantName, Integer leaveType,
                                  Integer status, String startTime, String endTime, Long userId) {
        if (applicantId == null && userId != null) {
            applicantId = userId;
        }
        return leaveRequestService.queryByPage(pageNo, pageSize, companyId, applicantId, applicantName, leaveType,
                status, startTime, endTime, userId);
    }

    /**
     * 查询详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, Long userId) {
        return leaveRequestService.queryById(id, userId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增请假单")
    public ResultData insert(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.insert(leaveRequest, userId);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改请假单")
    public ResultData update(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.update(leaveRequest, userId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除请假单")
    public ResultData delete(@PathVariable Long id, Long userId) {
        return leaveRequestService.delete(id, userId);
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
     * 提交
     */
    @PostMapping("/submit")
    @LogPoint("提交请假单")
    public ResultData submit(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.submit(leaveRequest, userId);
    }

    /**
     * 撤销
     */
    @PostMapping("/cancel/{id}")
    @LogPoint("撤销请假单")
    public ResultData cancel(@PathVariable Long id, Long userId) {
        return leaveRequestService.cancel(id, userId);
    }

}
