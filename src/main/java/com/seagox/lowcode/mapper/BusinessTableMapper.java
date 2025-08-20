package com.seagox.lowcode.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.BusinessTable;

/**
 * 业务表
 */
public interface BusinessTableMapper extends BaseMapper<BusinessTable> {
	
	/**
	 * 通过表名查询表和字段数据
	 */
	public BusinessTable queryTableByName(@Param("tableName")String tableName);
	
}
