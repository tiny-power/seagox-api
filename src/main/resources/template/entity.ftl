package com.seagox.lowcode.${mark}.entity;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
 
/**
* ${tableComment}
*/
public class ${tableUpper} {
	
	/**
     * id
     */
    private Long id;
<#list columns as column>
	
    /**
    * ${column.comment}
    */ 
	<#if column.type == 'date'>
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
	</#if>
	<#if column.type == 'timestamp'>
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	</#if>
    private ${column.javaType} ${column.field};
</#list>
	
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
<#list columns as column>

    public ${column.javaType} get${column.fieldUpper}() {
		return ${column.field};
	}

	public void set${column.fieldUpper}(${column.javaType} ${column.field}) {
		this.${column.field} = ${column.field};
	}
</#list>
	
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