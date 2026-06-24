package com.seagox.lowcode.business.service;

import java.util.List;

import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.business.template.LeaveRequestModel;
import com.seagox.lowcode.common.ResultData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请假单服务
 */
public interface ILeaveRequestService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long applicantId,
                                  String applicantName, Integer leaveType, Integer status, String startTime,
                                  String endTime);

    /**
     * 小程序分页查询
     */
    public ResultData miniQueryByPage(Integer pageNo, Integer pageSize, Long companyId, Long userId, Integer status);

    /**
     * 小程序查询详情
     */
    public ResultData miniQueryById(Long id, Long userId);

    /**
     * 小程序保存草稿
     */
    public ResultData miniSaveDraft(LeaveRequest leaveRequest, Long userId);

    /**
     * 小程序修改草稿
     */
    public ResultData miniUpdateDraft(LeaveRequest leaveRequest, Long userId);

    /**
     * 小程序提交
     */
    public ResultData miniSubmit(LeaveRequest leaveRequest, Long userId);

    /**
     * 小程序删除草稿
     */
    public ResultData miniDelete(Long id, Long userId);

    /**
     * 小程序撤销审批
     */
    public ResultData miniCancel(Long id, Long userId);

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

    /**
     * 导入
     */
    public void importHandle(List<LeaveRequestModel> resultList);

    /**
     * 导出
     */
    public void export(HttpServletRequest request, HttpServletResponse response, Long exportCompanyId,
                       String exportApplicantName, Integer exportLeaveType, Integer exportStatus,
                       String exportStartTime, String exportEndTime);

}
