package com.seagox.lowcode.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 问题来源类型
     */
    private Integer sourceType;

    /**
     * 来源单据ID
     */
    private Long sourceId;

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
     * 问题发现人用户ID
     */
    private Long reportedBy;

    /**
     * 问题发现时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reportedAt;

    /**
     * 分配人用户ID
     */
    private Long assignedBy;

    /**
     * 分配时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date assignedAt;

    /**
     * 整改截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationDeadline;

    /**
     * 整改提交次数
     */
    private Integer rectificationCount;

    /**
     * 整改说明
     */
    private String rectificationDescription;

    /**
     * 整改附件
     */
    private String rectificationAttachments;

    /**
     * 整改责任人用户ID
     */
    private Long rectificationUserId;

    /**
     * 整改提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationSubmittedAt;

    /**
     * 复验人用户ID
     */
    private Long reviewUserId;

    /**
     * 复验结果
     */
    private Integer reviewResult;

    /**
     * 复验说明
     */
    private String reviewRemark;

    /**
     * 复验附件
     */
    private String reviewAttachments;

    /**
     * 复验时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewedAt;

    /**
     * 关闭人用户ID
     */
    private Long closedBy;

    /**
     * 关闭时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closedAt;

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

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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

    public Long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Long reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Date getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(Date reportedAt) {
        this.reportedAt = reportedAt;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Date getRectificationDeadline() {
        return rectificationDeadline;
    }

    public void setRectificationDeadline(Date rectificationDeadline) {
        this.rectificationDeadline = rectificationDeadline;
    }

    public Integer getRectificationCount() {
        return rectificationCount;
    }

    public void setRectificationCount(Integer rectificationCount) {
        this.rectificationCount = rectificationCount;
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

    public Long getRectificationUserId() {
        return rectificationUserId;
    }

    public void setRectificationUserId(Long rectificationUserId) {
        this.rectificationUserId = rectificationUserId;
    }

    public Date getRectificationSubmittedAt() {
        return rectificationSubmittedAt;
    }

    public void setRectificationSubmittedAt(Date rectificationSubmittedAt) {
        this.rectificationSubmittedAt = rectificationSubmittedAt;
    }

    public Long getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Long reviewUserId) {
        this.reviewUserId = reviewUserId;
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

    public Long getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Long closedBy) {
        this.closedBy = closedBy;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
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
