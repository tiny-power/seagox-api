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
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 交接类型
     */
    private Integer handoverType;

    /**
     * 交接时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handoverTime;

    /**
     * 交接内容
     */
    private String handoverContent;

    /**
     * 移交负责人用户ID
     */
    private Long handoverUserId;

    /**
     * 接收负责人用户ID
     */
    private Long receiverUserId;

    /**
     * 移交方签字文件地址
     */
    private String handoverSignatureUrl;

    /**
     * 接收方签字文件地址
     */
    private String receiverSignatureUrl;

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
     * 附件
     */
    private String attachments;

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

    public Integer getHandoverType() {
        return handoverType;
    }

    public void setHandoverType(Integer handoverType) {
        this.handoverType = handoverType;
    }

    public Date getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(Date handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getHandoverContent() {
        return handoverContent;
    }

    public void setHandoverContent(String handoverContent) {
        this.handoverContent = handoverContent;
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

    public String getHandoverSignatureUrl() {
        return handoverSignatureUrl;
    }

    public void setHandoverSignatureUrl(String handoverSignatureUrl) {
        this.handoverSignatureUrl = handoverSignatureUrl;
    }

    public String getReceiverSignatureUrl() {
        return receiverSignatureUrl;
    }

    public void setReceiverSignatureUrl(String receiverSignatureUrl) {
        this.receiverSignatureUrl = receiverSignatureUrl;
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

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
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
