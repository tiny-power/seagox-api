package com.seagox.lowcode.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 报修单
 */
public class Repair {

    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 报修类型
     */
    private String type;

    /**
     * 报修位置
     */
    private String location;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 维修前附件
     */
    private String beforeAttachments;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 状态(1:已提交;2:处理中;3:待确认;4:已完成;)
     */
    private Integer status;

    /**
     * 报修时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repairAt;

    /**
     * 维修后附件
     */
    private String afterAttachments;

    /**
     * 维修人员ID
     */
    private Long repairMemberId;

    /**
     * 完成时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeAt;

    private Long createdBy;

    private Long updatedBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeforeAttachments() {
        return beforeAttachments;
    }

    public void setBeforeAttachments(String beforeAttachments) {
        this.beforeAttachments = beforeAttachments;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRepairAt() {
        return repairAt;
    }

    public void setRepairAt(Date repairAt) {
        this.repairAt = repairAt;
    }

    public String getAfterAttachments() {
        return afterAttachments;
    }

    public void setAfterAttachments(String afterAttachments) {
        this.afterAttachments = afterAttachments;
    }

    public Long getRepairMemberId() {
        return repairMemberId;
    }

    public void setRepairMemberId(Long repairMemberId) {
        this.repairMemberId = repairMemberId;
    }

    public Date getCompleteAt() {
        return completeAt;
    }

    public void setCompleteAt(Date completeAt) {
        this.completeAt = completeAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
