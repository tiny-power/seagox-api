package com.seagox.lowcode.business.entity;

/**
 * 阶段验收条目
 */
public class StageInspectionItem {

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
     * 验收条目名称
     */
    private String name;

    /**
     * 验收条目状态
     */
    private Integer status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
