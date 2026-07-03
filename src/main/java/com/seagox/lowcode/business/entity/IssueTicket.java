package com.seagox.lowcode.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 问题单
 */
public class IssueTicket {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题附件
     */
    private String issueAttachments;

    /**
     * 负责人
     */
    private Long assignee;

    /**
     * 截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    /**
     * 是否确认(0:未确认;1:已确认;)
     */
    private Integer confirmed;

    /**
     * 解决方案
     */
    private String resolution;

    /**
     * 状态(1:激活;2:已解决;3:已关闭;)
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

    /**
     * 兼容旧接口：整改责任人
     */
    @TableField(exist = false)
    private Long rectificationUserId;

    /**
     * 兼容旧接口：整改截止时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationDeadline;

    /**
     * 兼容旧接口：整改说明
     */
    @TableField(exist = false)
    private String rectificationDescription;

    /**
     * 兼容旧接口：整改附件
     */
    @TableField(exist = false)
    private String rectificationAttachments;

    /**
     * 兼容旧接口：整改提交时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationSubmittedAt;

    /**
     * 兼容旧接口：复验结果
     */
    @TableField(exist = false)
    private Integer reviewResult;

    /**
     * 兼容旧接口：复验说明
     */
    @TableField(exist = false)
    private String reviewRemark;

    /**
     * 兼容旧接口：复验附件
     */
    @TableField(exist = false)
    private String reviewAttachments;

    /**
     * 兼容旧接口：复验时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewedAt;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueAttachments() {
        return issueAttachments;
    }

    public void setIssueAttachments(String issueAttachments) {
        this.issueAttachments = issueAttachments;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
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

    public Long getRectificationUserId() {
        return rectificationUserId;
    }

    public void setRectificationUserId(Long rectificationUserId) {
        this.rectificationUserId = rectificationUserId;
    }

    public Date getRectificationDeadline() {
        return rectificationDeadline;
    }

    public void setRectificationDeadline(Date rectificationDeadline) {
        this.rectificationDeadline = rectificationDeadline;
    }

    public String getRectificationDescription() {
        return rectificationDescription;
    }

    public void setRectificationDescription(String rectificationDescription) {
        this.rectificationDescription = rectificationDescription;
    }

    public String getRectificationAttachments() {
        return rectificationAttachments;
    }

    public void setRectificationAttachments(String rectificationAttachments) {
        this.rectificationAttachments = rectificationAttachments;
    }

    public Date getRectificationSubmittedAt() {
        return rectificationSubmittedAt;
    }

    public void setRectificationSubmittedAt(Date rectificationSubmittedAt) {
        this.rectificationSubmittedAt = rectificationSubmittedAt;
    }

    public Integer getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Integer reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public String getReviewAttachments() {
        return reviewAttachments;
    }

    public void setReviewAttachments(String reviewAttachments) {
        this.reviewAttachments = reviewAttachments;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
