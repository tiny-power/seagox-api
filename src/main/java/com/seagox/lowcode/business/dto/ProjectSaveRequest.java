package com.seagox.lowcode.business.dto;

import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import java.util.List;

public class ProjectSaveRequest {

    private Project project;
    private List<ProjectMember> members;
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
