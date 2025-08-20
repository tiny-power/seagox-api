package com.seagox.lowcode.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 流程定义
 */
public class SeaDefinition {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 名称
     */
    private String name;

    /**
     * 流程文件(json)
     */
    private String resources;
    
    /**
     * 授权
     */
    private String empower;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 表单操作权限集合
     */
    @TableField(exist = false)
    private List<Map<String, Object>> operationAuthority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getEmpower() {
		return empower;
	}

	public void setEmpower(String empower) {
		this.empower = empower;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Map<String, Object>> getOperationAuthority() {
        return operationAuthority;
    }

    public void setOperationAuthority(List<Map<String, Object>> operationAuthority) {
        this.operationAuthority = operationAuthority;
    }

}
