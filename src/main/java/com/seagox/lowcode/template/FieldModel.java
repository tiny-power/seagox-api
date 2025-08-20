package com.seagox.lowcode.template;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class FieldModel extends ImportModel {
	
	/**
     * 字段名
     */
    @Excel(name = "字段名")
    @NotNull(message = "不能为空或有误")
    @Pattern(regexp = "^[a-z_]{1,}$", message = "只能输入小写英文字母或下划线")
    private String name;

    /**
     * 注释
     */
    @Excel(name = "注释")
    @NotNull(message = "不能为空或有误")
    private String remark;
    
    /**
     * 类型
     */
    @Excel(name = "类型")
    @NotNull(message = "不能为空或有误")
    private String type;
    
    /**
     * 种类
     */
    @Excel(name = "种类")
    @NotNull(message = "不能为空或有误")
    private String kind;

    /**
     * 是否为空(0:否;1:是;)
     */
    @Excel(name = "是否为空", replace = {"是_0", "否_1"})
    private Integer notNull;
    
    /**
     * 长度
     */
    @Excel(name = "长度")
    @Min(value = 0, message = "不能低于0")
    @Max(value = 4000, message = "不能超过4000")
    private Integer length;
    
    /**
     * 小数
     */
    @Excel(name = "小数点")
    @Min(value = 0, message = "不能低于0")
    @Max(value = 30, message = "不能超过30")
    private Integer decimals;
    
    /**
     * 默认值
     */
    @Excel(name = "默认值")
    private String defaultValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Integer getNotNull() {
		return notNull;
	}

	public void setNotNull(Integer notNull) {
		this.notNull = notNull;
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

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
