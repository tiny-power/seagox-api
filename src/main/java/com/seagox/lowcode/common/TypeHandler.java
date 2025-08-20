package com.seagox.lowcode.common;

import org.apache.ibatis.type.ClobTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.Clob;

@MappedTypes(Clob.class)
@MappedJdbcTypes(JdbcType.CLOB)
public class TypeHandler extends ClobTypeHandler {
	
}
