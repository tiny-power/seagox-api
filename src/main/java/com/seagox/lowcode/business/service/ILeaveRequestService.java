package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.common.ResultData;

/**
 * 请假单服务
 */
public interface ILeaveRequestService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime, Long userId);

    /**
     * 查询详情
     */
    public ResultData queryById(Long id, Long userId);

    /**
     * 新增
     */
    public ResultData insert(LeaveRequest leaveRequest, Long userId);

    /**
     * 修改
     */
    public ResultData update(LeaveRequest leaveRequest, Long userId);

    /**
     * 删除
     */
    public ResultData delete(Long id, Long userId);

    /**
     * 提交
     */
    public ResultData submit(Long id);

    /**
     * 提交
     */
    public ResultData submit(LeaveRequest leaveRequest, Long userId);

    /**
     * 撤销
     */
    public ResultData cancel(Long id, Long userId);

}
