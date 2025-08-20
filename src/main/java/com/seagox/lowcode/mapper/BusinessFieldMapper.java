package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.BusinessField;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业务字段
 */
public interface BusinessFieldMapper extends BaseMapper<BusinessField> {

    /**
     * 查询全部通过表名
     *
     * @param tableName 表名
     */
    public List<Map<String, Object>> queryByTableName(@Param("tableName") String tableName);

    /**
     * 查询全部通过表ids
     */
    public List<Map<String, Object>> queryByTableIds(String[] tableIds);

    /**
     * 查询必填字段
     */
    public List<Map<String, Object>> queryRequiredByTableId(Long tableId);
    
    /**
     * 查询外键
     *
     * @param tableName 表名
     */
    public String queryForeignKey(@Param("tableName") String tableName, @Param("targetTableId") Long targetTableId);

}
