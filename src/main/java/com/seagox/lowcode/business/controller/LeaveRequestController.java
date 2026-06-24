package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.LeaveRequest;
import com.seagox.lowcode.business.service.ILeaveRequestService;
import com.seagox.lowcode.business.template.LeaveRequestModel;
import com.seagox.lowcode.business.verify.LeaveRequestExcelVerifyHandler;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 请假单导入校验器
     */
    @Autowired
    private LeaveRequestExcelVerifyHandler leaveRequestExcelVerifyHandler;

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
     * 小程序分页查询
     */
    @GetMapping("/mini/queryByPage")
    public ResultData miniQueryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      Long companyId, Long userId, Integer status) {
        return leaveRequestService.miniQueryByPage(pageNo, pageSize, companyId, userId, status);
    }

    /**
     * 小程序查询详情
     */
    @GetMapping("/mini/queryById/{id}")
    public ResultData miniQueryById(@PathVariable Long id, Long userId) {
        return leaveRequestService.miniQueryById(id, userId);
    }

    /**
     * 小程序保存草稿
     */
    @PostMapping("/mini/saveDraft")
    @LogPoint("小程序保存请假单草稿")
    public ResultData miniSaveDraft(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.miniSaveDraft(leaveRequest, userId);
    }

    /**
     * 小程序修改草稿
     */
    @PostMapping("/mini/updateDraft")
    @LogPoint("小程序修改请假单草稿")
    public ResultData miniUpdateDraft(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.miniUpdateDraft(leaveRequest, userId);
    }

    /**
     * 小程序提交
     */
    @PostMapping("/mini/submit")
    @LogPoint("小程序提交请假单")
    public ResultData miniSubmit(@Valid LeaveRequest leaveRequest, Long userId) {
        return leaveRequestService.miniSubmit(leaveRequest, userId);
    }

    /**
     * 小程序删除
     */
    @PostMapping("/mini/delete/{id}")
    @LogPoint("小程序删除请假单")
    public ResultData miniDelete(@PathVariable Long id, Long userId) {
        return leaveRequestService.miniDelete(id, userId);
    }

    /**
     * 小程序撤销
     */
    @PostMapping("/mini/cancel/{id}")
    @LogPoint("小程序撤销请假单")
    public ResultData miniCancel(@PathVariable Long id, Long userId) {
        return leaveRequestService.miniCancel(id, userId);
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

    /**
     * 导入
     */
    @PostMapping("/import")
    public ResultData importHandle(@RequestParam("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setNeedVerify(true);
        params.setVerifyHandler(leaveRequestExcelVerifyHandler);
        try {
            ExcelImportResult<LeaveRequestModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(),
                    LeaveRequestModel.class, params);
            if (result.isVerifyFail()) {
                for (LeaveRequestModel entity : result.getFailList()) {
                    return ResultData.warn(ResultCode.OTHER_ERROR,
                            "第" + entity.getRowNum() + "行的错误是：" + entity.getErrorMsg());
                }
            } else {
                List<LeaveRequestModel> resultList = result.getList();
                leaveRequestService.importHandle(resultList);
            }
            return ResultData.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, Long exportCompanyId,
                       String exportApplicantName, Integer exportLeaveType, Integer exportStatus,
                       String exportStartTime, String exportEndTime) {
        leaveRequestService.export(request, response, exportCompanyId, exportApplicantName, exportLeaveType,
                exportStatus, exportStartTime, exportEndTime);
    }

}
