package com.seagox.lowcode.business.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 交接单
 */
public class ProjectHandover {

    private Long id;

    private Long projectId;

    private Integer handoverType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handoverTime;

    private String handoverContent;

    private Long handoverUserId;

    private Long receiverUserId;

    private String handoverSignatureUrl;

    private String receiverSignatureUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handoverSignedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiverSignedAt;

    private String attachment;

    private Long createdBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Long updatedBy;

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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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
