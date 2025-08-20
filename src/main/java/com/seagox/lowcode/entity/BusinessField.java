package com.seagox.lowcode.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 业务字段
 */
public class BusinessField {
	
	/**
     * id
     */
    private Long id;
    
    /**
     * 业务表id
     */
    private Long businessTableId;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 注释
     */
    private String remark;
    
    /**
     * 类型
     */
    private String type;
    
    /**
     * 种类
     */
    
    private String kind;
    
    /**
     * 长度
     */
    private Integer length;
    
    /**
     * 小数
     */
    private Integer decimals;
    
    /**
     * 不为空(0:否;1:是;)
     */
    private Integer notNull;
    
    /**
     * 默认值
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String defaultValue;
    
    /**
     * 目标模型
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long targetTableId;
    
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessTableId() {
		return businessTableId;
	}

	public void setBusinessTableId(Long businessTableId) {
		this.businessTableId = businessTableId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public Integer getNotNull() {
		return notNull;
	}

	public void setNotNull(Integer notNull) {
		this.notNull = notNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getTargetTableId() {
		return targetTableId;
	}

	public void setTargetTableId(Long targetTableId) {
		this.targetTableId = targetTableId;
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

}
