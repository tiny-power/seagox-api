package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.LeaveRequest;

public interface ILeaveRequestService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime);

    /**
     * 查询详情
     */
    public ResultData queryById(Long id);

    /**
     * 新增
     */
    public ResultData insert(LeaveRequest leaveRequest);

    /**
     * 修改
     */
    public ResultData update(LeaveRequest leaveRequest);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 提交
     */
    public ResultData submit(Long id);

    /**
     * 撤销
     */
    public ResultData cancel(Long id);

}
