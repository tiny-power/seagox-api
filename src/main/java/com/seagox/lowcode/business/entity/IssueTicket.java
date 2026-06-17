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
     * 编号
     */
    private String code;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 关联项目阶段ID
     */
    private Long stageId;

    /**
     * 关联验收单ID
     */
    private Long inspectionId;

    /**
     * 关联施工日记ID
     */
    private Long dailyLogId;

    /**
     * 问题来源：MANUAL手动创建、DAILY_LOG施工日记、INSPECTION验收发现、OWNER_FEEDBACK业主反馈、AFTER_SALES售后报修
     */
    private String sourceType;

    /**
     * 问题类型：QUALITY质量问题、PROGRESS进度问题、SAFETY安全问题、MATERIAL材料问题、DESIGN设计问题、SERVICE服务问题、OTHER其他问题
     */
    private String issueType;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题详细描述
     */
    private String description;

    /**
     * 严重程度：LOW一般、MEDIUM中等、HIGH严重、CRITICAL紧急
     */
    private String severity;

    /**
     * 问题现场照片，JSON数组
     */
    private String issuePhotos;

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
     * 整改责任人用户ID
     */
    private Long responsibleUserId;

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
     * 问题状态：OPEN待分配、ASSIGNED已分配、RECTIFYING整改中、PENDING_REVIEW待复验、REJECTED复验不通过、CLOSED已关闭、CANCELLED已取消
     */
    private String status;

    /**
     * 整改提交次数
     */
    private Integer rectificationCount;

    /**
     * 最新整改说明
     */
    private String rectificationDescription;

    /**
     * 最新整改完成照片，JSON数组
     */
    private String rectificationPhotos;

    /**
     * 整改提交人用户ID
     */
    private Long rectificationSubmittedBy;

    /**
     * 整改提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationSubmittedAt;

    /**
     * 复验人或质检员用户ID
     */
    private Long reviewUserId;

    /**
     * 复验结果：PASSED通过、REJECTED不通过
     */
    private String reviewResult;

    /**
     * 复验说明
     */
    private String reviewRemark;

    /**
     * 复验现场照片，JSON数组
     */
    private String reviewPhotos;

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
     * 问题关闭时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closedAt;

    /**
     * 取消原因
     */
    private String cancelReason;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(Long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Long getDailyLogId() {
        return dailyLogId;
    }

    public void setDailyLogId(Long dailyLogId) {
        this.dailyLogId = dailyLogId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getIssuePhotos() {
        return issuePhotos;
    }

    public void setIssuePhotos(String issuePhotos) {
        this.issuePhotos = issuePhotos;
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

    public Long getResponsibleUserId() {
        return responsibleUserId;
    }

    public void setResponsibleUserId(Long responsibleUserId) {
        this.responsibleUserId = responsibleUserId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRectificationPhotos() {
        return rectificationPhotos;
    }

    public void setRectificationPhotos(String rectificationPhotos) {
        this.rectificationPhotos = rectificationPhotos;
    }

    public Long getRectificationSubmittedBy() {
        return rectificationSubmittedBy;
    }

    public void setRectificationSubmittedBy(Long rectificationSubmittedBy) {
        this.rectificationSubmittedBy = rectificationSubmittedBy;
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

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public String getReviewPhotos() {
        return reviewPhotos;
    }

    public void setReviewPhotos(String reviewPhotos) {
        this.reviewPhotos = reviewPhotos;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
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
