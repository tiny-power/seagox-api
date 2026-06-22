package com.seagox.lowcode.business.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import java.util.List;

/**
 * 项目阶段保存请求
 */
public class ProjectStageSaveRequest extends ProjectStage {

    /**
     * 前端阶段临时标识
     */
    @TableField(exist = false)
    private String key;

    /**
     * 前置阶段临时标识列表
     */
    @TableField(exist = false)
    private List<String> predecessorKeys;

    /**
     * 阶段验收条目
     */
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
