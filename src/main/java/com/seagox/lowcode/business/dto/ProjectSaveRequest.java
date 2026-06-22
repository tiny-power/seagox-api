package com.seagox.lowcode.business.dto;

import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import java.util.List;

/**
 * 项目保存请求
 */
public class ProjectSaveRequest {

    /**
     * 项目基本信息
     */
    private Project project;
    /**
     * 项目成员
     */
    private List<ProjectMember> members;
    /**
     * 项目阶段
     */
    private List<ProjectStageSaveRequest> stages;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }

    public List<ProjectStageSaveRequest> getStages() {
        return stages;
    }

    public void setStages(List<ProjectStageSaveRequest> stages) {
        this.stages = stages;
    }
}
