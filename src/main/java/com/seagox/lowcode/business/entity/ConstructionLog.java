package com.seagox.lowcode.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 施工日志
 */
public class ConstructionLog {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 当前项目阶段ID
     */
    private Long stageId;

    /**
     * 日记日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date logDate;

    /**
     * 填写人用户ID
     */
    private Long filledBy;

    /**
     * 日记类型：CIVIL土建、DECORATION精装
     */
    private String logType;

    /**
     * 今日天气
     */
    private String weather;

    /**
     * 今日温度，如20-28℃
     */
    private String temperature;

    /**
     * 各工种今日进展，包含工种、区域、部位、完成情况、遍数、照片等
     */
    private String tradeProgress;

    /**
     * 今日施工总体进展摘要
     */
    private String currentProgressSummary;

    /**
     * 当前节点预计完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedNodeCompletionAt;

    /**
     * 明日或下一步工作计划
     */
    private String nextDayPlan;

    /**
     * 明日工种安排
     */
    private String nextDayTradePlan;

    /**
     * 现场问题记录，包含问题描述、照片、责任人、整改期限、问题单ID
     */
    private String siteIssues;

    /**
     * 是否存在现场问题：0否、1是
     */
    private Integer hasIssue;

    /**
     * 需要业主配合或确认的事项
     */
    private String ownerMentions;

    /**
     * 需要质检员处理或验收的事项
     */
    private String qcMentions;

    /**
     * 状态：DRAFT草稿、SUBMITTED已提交、LOCKED已锁定
     */
    private String status;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submittedAt;

    /**
     * 锁定时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockedAt;

    /**
     * 其他备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 修改人
     */
    private Long updatedBy;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Long getFilledBy() {
        return filledBy;
    }

    public void setFilledBy(Long filledBy) {
        this.filledBy = filledBy;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTradeProgress() {
        return tradeProgress;
    }

    public void setTradeProgress(String tradeProgress) {
        this.tradeProgress = tradeProgress;
    }

    public String getCurrentProgressSummary() {
        return currentProgressSummary;
    }

    public void setCurrentProgressSummary(String currentProgressSummary) {
        this.currentProgressSummary = currentProgressSummary;
    }

    public Date getExpectedNodeCompletionAt() {
        return expectedNodeCompletionAt;
    }

    public void setExpectedNodeCompletionAt(Date expectedNodeCompletionAt) {
        this.expectedNodeCompletionAt = expectedNodeCompletionAt;
    }

    public String getNextDayPlan() {
        return nextDayPlan;
    }

    public void setNextDayPlan(String nextDayPlan) {
        this.nextDayPlan = nextDayPlan;
    }

    public String getNextDayTradePlan() {
        return nextDayTradePlan;
    }

    public void setNextDayTradePlan(String nextDayTradePlan) {
        this.nextDayTradePlan = nextDayTradePlan;
    }

    public String getSiteIssues() {
        return siteIssues;
    }

    public void setSiteIssues(String siteIssues) {
        this.siteIssues = siteIssues;
    }

    public Integer getHasIssue() {
        return hasIssue;
    }

    public void setHasIssue(Integer hasIssue) {
        this.hasIssue = hasIssue;
    }

    public String getOwnerMentions() {
        return ownerMentions;
    }

    public void setOwnerMentions(String ownerMentions) {
        this.ownerMentions = ownerMentions;
    }

    public String getQcMentions() {
        return qcMentions;
    }

    public void setQcMentions(String qcMentions) {
        this.qcMentions = qcMentions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
