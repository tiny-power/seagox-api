package com.seagox.lowcode.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 施工图出图详情
 */
public class ConstructionDrawingDetail {

    private Long id;

    /**
     * 施工图出图ID
     */
    private Long constructionDrawingId;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 建筑设计附件
     */
    private String architectureAttachments;

    /**
     * 结构设计附件
     */
    private String structureAttachments;

    /**
     * 精装图纸附件
     */
    private String decorationAttachments;

    /**
     * 主材及软装采购方案附件
     */
    private String procurementAttachments;

    /**
     * 修改说明
     */
    private String solutionExplanation;

    /**
     * 成员确认
     */
    private String confirmMembers;

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

    public Long getConstructionDrawingId() {
        return constructionDrawingId;
    }

    public void setConstructionDrawingId(Long constructionDrawingId) {
        this.constructionDrawingId = constructionDrawingId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getArchitectureAttachments() {
        return architectureAttachments;
    }

    public void setArchitectureAttachments(String architectureAttachments) {
        this.architectureAttachments = architectureAttachments;
    }

    public String getStructureAttachments() {
        return structureAttachments;
    }

    public void setStructureAttachments(String structureAttachments) {
        this.structureAttachments = structureAttachments;
    }

    public String getDecorationAttachments() {
        return decorationAttachments;
    }

    public void setDecorationAttachments(String decorationAttachments) {
        this.decorationAttachments = decorationAttachments;
    }

    public String getProcurementAttachments() {
        return procurementAttachments;
    }

    public void setProcurementAttachments(String procurementAttachments) {
        this.procurementAttachments = procurementAttachments;
    }

    public String getSolutionExplanation() {
        return solutionExplanation;
    }

    public void setSolutionExplanation(String solutionExplanation) {
        this.solutionExplanation = solutionExplanation;
    }

    public String getConfirmMembers() {
        return confirmMembers;
    }

    public void setConfirmMembers(String confirmMembers) {
        this.confirmMembers = confirmMembers;
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
