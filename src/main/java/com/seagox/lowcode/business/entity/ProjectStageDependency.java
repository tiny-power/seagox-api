package com.seagox.lowcode.business.entity;

/**
 * 项目阶段前置依赖表
 */
public class ProjectStageDependency {

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 当前阶段ID
     */
    private Long stageId;

    /**
     * 前置阶段ID
     */
    private Long predecessorStageId;

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

    public Long getPredecessorStageId() {
        return predecessorStageId;
    }

    public void setPredecessorStageId(Long predecessorStageId) {
        this.predecessorStageId = predecessorStageId;
    }

}
