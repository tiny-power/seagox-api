package com.seagox.lowcode.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 交接单
 */
public class ProjectHandover {

    /**
     * 主键
     */
    private Long id;

    /**
     * 交接单编号
     */
    private String handoverCode;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 交接类型：DESIGN_TO_CIVIL设计交接土建、DESIGN_TO_DECORATION设计交接精装、CIVIL_TO_DECORATION土建交接精装、OTHER其他交接
     */
    private String handoverType;

    /**
     * 交接单名称
     */
    private String handoverName;

    /**
     * 移交来源阶段ID
     */
    private Long fromStageId;

    /**
     * 接收目标阶段ID
     */
    private Long toStageId;

    /**
     * 移交流程：DESIGN设计、CIVIL土建、DECORATION精装
     */
    private String fromFlowType;

    /**
     * 接收流程：CIVIL土建、DECORATION精装
     */
    private String toFlowType;

    /**
     * 关联验收单ID，例如主体结构验收单
     */
    private Long relatedInspectionId;

    /**
     * 实际交接日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date handoverDate;

    /**
     * 移交负责人用户ID
     */
    private Long handoverUserId;

    /**
     * 接收负责人用户ID
     */
    private Long receiverUserId;

    /**
     * 交接检查项，包含交接内容、数量、状态、备注、照片等
     */
    private String handoverItems;

    /**
     * 移交资料附件，如图纸、报告、清单、现场照片
     */
    private String documentFiles;

    /**
     * 遗留事项及处理计划
     */
    private String unfinishedItems;

    /**
     * 未关闭问题数量
     */
    private Integer openIssueCount;

    /**
     * 交接结论：ACCEPTED接收通过、CONDITIONAL_ACCEPTANCE有条件接收、REJECTED拒绝接收
     */
    private String handoverConclusion;

    /**
     * 移交方说明
     */
    private String handoverRemark;

    /**
     * 接收方意见
     */
    private String receiverRemark;

    /**
     * 移交方签字文件ID
     */
    private Long handoverSignatureFileId;

    /**
     * 接收方签字文件ID
     */
    private Long receiverSignatureFileId;

    /**
     * 移交方签字时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handoverSignedAt;

    /**
     * 接收方签字时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiverSignedAt;

    /**
     * 状态：DRAFT草稿、PENDING_HANDOVER待移交、PENDING_RECEIVE待接收、REJECTED已退回、COMPLETED已完成、CANCELLED已取消
     */
    private String status;

    /**
     * 交接完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completedAt;

    /**
     * 取消原因
     */
    private String cancelReason;

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

    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(String handoverType) {
        this.handoverType = handoverType;
    }

    public String getHandoverName() {
        return handoverName;
    }

    public void setHandoverName(String handoverName) {
        this.handoverName = handoverName;
    }

    public Long getFromStageId() {
        return fromStageId;
    }

    public void setFromStageId(Long fromStageId) {
        this.fromStageId = fromStageId;
    }

    public Long getToStageId() {
        return toStageId;
    }

    public void setToStageId(Long toStageId) {
        this.toStageId = toStageId;
    }

    public String getFromFlowType() {
        return fromFlowType;
    }

    public void setFromFlowType(String fromFlowType) {
        this.fromFlowType = fromFlowType;
    }

    public String getToFlowType() {
        return toFlowType;
    }

    public void setToFlowType(String toFlowType) {
        this.toFlowType = toFlowType;
    }

    public Long getRelatedInspectionId() {
        return relatedInspectionId;
    }

    public void setRelatedInspectionId(Long relatedInspectionId) {
        this.relatedInspectionId = relatedInspectionId;
    }

    public Date getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Date handoverDate) {
        this.handoverDate = handoverDate;
    }

    public Long getHandoverUserId() {
        return handoverUserId;
    }

    public void setHandoverUserId(Long handoverUserId) {
        this.handoverUserId = handoverUserId;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getHandoverItems() {
        return handoverItems;
    }

    public void setHandoverItems(String handoverItems) {
        this.handoverItems = handoverItems;
    }

    public String getDocumentFiles() {
        return documentFiles;
    }

    public void setDocumentFiles(String documentFiles) {
        this.documentFiles = documentFiles;
    }

    public String getUnfinishedItems() {
        return unfinishedItems;
    }

    public void setUnfinishedItems(String unfinishedItems) {
        this.unfinishedItems = unfinishedItems;
    }

    public Integer getOpenIssueCount() {
        return openIssueCount;
    }

    public void setOpenIssueCount(Integer openIssueCount) {
        this.openIssueCount = openIssueCount;
    }

    public String getHandoverConclusion() {
        return handoverConclusion;
    }

    public void setHandoverConclusion(String handoverConclusion) {
        this.handoverConclusion = handoverConclusion;
    }

    public String getHandoverRemark() {
        return handoverRemark;
    }

    public void setHandoverRemark(String handoverRemark) {
        this.handoverRemark = handoverRemark;
    }

    public String getReceiverRemark() {
        return receiverRemark;
    }

    public void setReceiverRemark(String receiverRemark) {
        this.receiverRemark = receiverRemark;
    }

    public Long getHandoverSignatureFileId() {
        return handoverSignatureFileId;
    }

    public void setHandoverSignatureFileId(Long handoverSignatureFileId) {
        this.handoverSignatureFileId = handoverSignatureFileId;
    }

    public Long getReceiverSignatureFileId() {
        return receiverSignatureFileId;
    }

    public void setReceiverSignatureFileId(Long receiverSignatureFileId) {
        this.receiverSignatureFileId = receiverSignatureFileId;
    }

    public Date getHandoverSignedAt() {
        return handoverSignedAt;
    }

    public void setHandoverSignedAt(Date handoverSignedAt) {
        this.handoverSignedAt = handoverSignedAt;
    }

    public Date getReceiverSignedAt() {
        return receiverSignedAt;
    }

    public void setReceiverSignedAt(Date receiverSignedAt) {
        this.receiverSignedAt = receiverSignedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
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
