package com.seagox.lowcode.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
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
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 项目阶段ID
     */
    private Long stageId;

    /**
     * 项目阶段条目
     */
    private String inspectionItems;

    /**
     * 计划验收时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planInspectionTime;

    /**
     * 验收现场总体照片
     */
    private String sitePhotos;

    /**
     * 参与人员
     */
    private String participants;

    /**
     * 签字信息
     */
    private String signatures;

    /**
     * 验收通过时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date passedAt;

    /**
     * 验收意见
     */
    private String acceptanceComments;

    /**
     * 备注
     */
    private String remark;

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

    public String getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(String inspectionItems) {
        this.inspectionItems = inspectionItems;
    }

    public Date getPlanInspectionTime() {
        return planInspectionTime;
    }

    public void setPlanInspectionTime(Date planInspectionTime) {
        this.planInspectionTime = planInspectionTime;
    }

    public String getSitePhotos() {
        return sitePhotos;
    }

    public void setSitePhotos(String sitePhotos) {
        this.sitePhotos = sitePhotos;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public Date getPassedAt() {
        return passedAt;
    }

    public void setPassedAt(Date passedAt) {
        this.passedAt = passedAt;
    }

    public String getAcceptanceComments() {
        return acceptanceComments;
    }

    public void setAcceptanceComments(String acceptanceComments) {
        this.acceptanceComments = acceptanceComments;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
