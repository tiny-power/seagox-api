package com.seagox.lowcode.business.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import java.util.List;

public class ProjectStageSaveRequest extends ProjectStage {

    @TableField(exist = false)
    private String key;

    @TableField(exist = false)
    private List<String> predecessorKeys;

    @TableField(exist = false)
    private List<StageInspectionItem> inspectionItems;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getPredecessorKeys() {
        return predecessorKeys;
    }

    public void setPredecessorKeys(List<String> predecessorKeys) {
        this.predecessorKeys = predecessorKeys;
    }

    public List<StageInspectionItem> getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(List<StageInspectionItem> inspectionItems) {
        this.inspectionItems = inspectionItems;
    }
}
