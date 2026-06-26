package com.seagox.lowcode.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 施工图出图
 */
public class ConstructionDrawing {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属项目ID
     */
    private Long projectId;

    /**
     * 版本
     */
    @TableField(exist = false)
    private String version;

    /**
     * 建筑设计附件
     */
    @TableField(exist = false)
    private String architectureAttachments;

    /**
     * 结构设计附件
     */
    @TableField(exist = false)
    private String structureAttachments;

    /**
     * 精装图纸附件
     */
    @TableField(exist = false)
    private String decorationAttachments;

    /**
     * 主材及软装采购方案附件
     */
    @TableField(exist = false)
    private String procurementAttachments;

    /**
     * 修改说明
     */
    @TableField(exist = false)
    private String solutionExplanation;

    /**
     * 成员确认
     */
    @TableField(exist = false)
    private String confirmMembers;

    /**
     * 当前版本详情ID
     */
    @TableField(exist = false)
    private Long detailId;

    /**
     * 状态(1:待提交;2:待确认;3:已归档;)
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 更新人ID
     */
    private Long updatedBy;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * 更新时间
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
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

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
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
