package com.seagox.lowcode.business.template;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.seagox.lowcode.system.template.ImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class LeaveRequestModel extends ImportModel {

    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    @NotNull(message = "不能为空或有误")
    private String companyName;

    /**
     * 申请人账号
     */
    @Excel(name = "申请人账号")
    @NotNull(message = "不能为空或有误")
    private String applicantAccount;

    /**
     * 申请人姓名
     */
    @Excel(name = "申请人姓名")
    private String applicantName;

    /**
     * 请假类型(1:事假;2:病假;3:年假;4:调休;5:婚假;6:产假;7:丧假;8:其他;)
     */
    @Excel(name = "请假类型", replace = { "事假_1", "病假_2", "年假_3", "调休_4", "婚假_5", "产假_6", "丧假_7", "其他_8" })
    @NotNull(message = "不能为空或有误")
    private Integer leaveType;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间")
    @NotNull(message = "不能为空或有误")
    private String startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间")
    @NotNull(message = "不能为空或有误")
    private String endTime;

    /**
     * 请假时长
     */
    @Excel(name = "时长(天)")
    @NotNull(message = "不能为空或有误")
    private BigDecimal duration;

    /**
     * 请假事由
     */
    @Excel(name = "请假事由")
    @NotNull(message = "不能为空或有误")
    @Size(max = 500, message = "长度最多500个字符")
    private String reason;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getApplicantAccount() {
        return applicantAccount;
    }

    public void setApplicantAccount(String applicantAccount) {
        this.applicantAccount = applicantAccount;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
