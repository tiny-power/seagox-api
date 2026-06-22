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
     * 项目阶段ID
     */
    private Long stageId;

    /**
     * 日志日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date logDate;

    /**
     * 填写人用户ID
     */
    private Long filledBy;

    /**
     * 今日工作内容
     */
    private String currentProgressSummary;

    /**
     * 预计完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedCompletionAt;

    /**
     * 明日计划
     */
    private String nextDayPlan;

    /**
     * 需要协调事项
     */
    private String siteIssues;

    /**
     * 是否存在现场问题
     */
    private Integer hasIssue;

    /**
     * 配合人员
     */
    private String assistants;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submittedAt;

    /**
     * 附件
     */
    private String attachments;

    /**
     * 状态
     */
    private Integer status;

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

    public String getCurrentProgressSummary() {
        return currentProgressSummary;
    }

    public void setCurrentProgressSummary(String currentProgressSummary) {
        this.currentProgressSummary = currentProgressSummary;
    }

    public Date getExpectedCompletionAt() {
        return expectedCompletionAt;
    }

    public void setExpectedCompletionAt(Date expectedCompletionAt) {
        this.expectedCompletionAt = expectedCompletionAt;
    }

    public String getNextDayPlan() {
        return nextDayPlan;
    }

    public void setNextDayPlan(String nextDayPlan) {
        this.nextDayPlan = nextDayPlan;
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

    public String getAssistants() {
        return assistants;
    }

    public void setAssistants(String assistants) {
        this.assistants = assistants;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
