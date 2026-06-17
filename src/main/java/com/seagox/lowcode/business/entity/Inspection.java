package com.seagox.lowcode.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 验收单
 */
public class Inspection {

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 当前项目阶段ID
     */
    private Long stageId;

    /**
     * 验收名称
     */
    private String inspectionName;

    /**
     * 原验收单ID，复验时关联上一轮验收单
     */
    private Long parentInspectionId;

    /**
     * 验收轮次：1首次验收，2第一次复验，以此类推
     */
    private Integer inspectionRound;

    /**
     * 验收日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectionDate;

    /**
     * 验收开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionStartedAt;

    /**
     * 验收结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishedAt;

    /**
     * 质检员用户ID
     */
    private Long qcUserId;

    /**
     * 项目经理用户ID
     */
    private Long projectManagerUserId;

    /**
     * 参与人员，格式为用户ID、姓名、角色等
     */
    private String participants;

    /**
     * 验收检查项，包含检查内容、检查结果、备注、照片等
     */
    private String checkItems;

    /**
     * 验收现场总体照片，必须上传
     */
    private String sitePhotos;

    /**
     * 验收结论：PASSED通过、RECTIFICATION_REQUIRED整改后复验、FAILED不通过
     */
    private String conclusion;

    /**
     * 本次验收生成的问题单ID集合
     */
    private String issueTicketIds;

    /**
     * 问题总数
     */
    private Integer issueCount;

    /**
     * 未关闭问题数
     */
    private Integer openIssueCount;

    /**
     * 整改截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectificationDeadline;

    /**
     * 签字信息，包含签字人、角色、签字文件、签字时间
     */
    private String signatures;

    /**
     * 质检员签字时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date qcSignedAt;

    /**
     * 项目经理签字时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pmSignedAt;

    /**
     * 业主签字时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ownerSignedAt;

    /**
     * 验收单状态：DRAFT草稿、SUBMITTED已提交、PENDING_SIGNATURE待签字、PENDING_RECTIFICATION待整改、PENDING_REINSPECTION待复验、PASSED已通过、FAILED未通过、VOID已作废
     */
    private String status;

    /**
     * 验收通过时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date passedAt;

    /**
     * 备注
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

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public Long getParentInspectionId() {
        return parentInspectionId;
    }

    public void setParentInspectionId(Long parentInspectionId) {
        this.parentInspectionId = parentInspectionId;
    }

    public Integer getInspectionRound() {
        return inspectionRound;
    }

    public void setInspectionRound(Integer inspectionRound) {
        this.inspectionRound = inspectionRound;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public Date getInspectionStartedAt() {
        return inspectionStartedAt;
    }

    public void setInspectionStartedAt(Date inspectionStartedAt) {
        this.inspectionStartedAt = inspectionStartedAt;
    }

    public Date getInspectionFinishedAt() {
        return inspectionFinishedAt;
    }

    public void setInspectionFinishedAt(Date inspectionFinishedAt) {
        this.inspectionFinishedAt = inspectionFinishedAt;
    }

    public Long getQcUserId() {
        return qcUserId;
    }

    public void setQcUserId(Long qcUserId) {
        this.qcUserId = qcUserId;
    }

    public Long getProjectManagerUserId() {
        return projectManagerUserId;
    }

    public void setProjectManagerUserId(Long projectManagerUserId) {
        this.projectManagerUserId = projectManagerUserId;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(String checkItems) {
        this.checkItems = checkItems;
    }

    public String getSitePhotos() {
        return sitePhotos;
    }

    public void setSitePhotos(String sitePhotos) {
        this.sitePhotos = sitePhotos;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getIssueTicketIds() {
        return issueTicketIds;
    }

    public void setIssueTicketIds(String issueTicketIds) {
        this.issueTicketIds = issueTicketIds;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Integer getOpenIssueCount() {
        return openIssueCount;
    }

    public void setOpenIssueCount(Integer openIssueCount) {
        this.openIssueCount = openIssueCount;
    }

    public Date getRectificationDeadline() {
        return rectificationDeadline;
    }

    public void setRectificationDeadline(Date rectificationDeadline) {
        this.rectificationDeadline = rectificationDeadline;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public Date getQcSignedAt() {
        return qcSignedAt;
    }

    public void setQcSignedAt(Date qcSignedAt) {
        this.qcSignedAt = qcSignedAt;
    }

    public Date getPmSignedAt() {
        return pmSignedAt;
    }

    public void setPmSignedAt(Date pmSignedAt) {
        this.pmSignedAt = pmSignedAt;
    }

    public Date getOwnerSignedAt() {
        return ownerSignedAt;
    }

    public void setOwnerSignedAt(Date ownerSignedAt) {
        this.ownerSignedAt = ownerSignedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPassedAt() {
        return passedAt;
    }

    public void setPassedAt(Date passedAt) {
        this.passedAt = passedAt;
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
