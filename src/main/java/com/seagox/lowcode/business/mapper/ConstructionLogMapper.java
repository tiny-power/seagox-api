package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ConstructionLog;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 施工日志
 */
public interface ConstructionLogMapper extends BaseMapper<ConstructionLog> {

    List<Map<String, Object>> queryConstructionLogs(@Param("params") Map<String, Object> params);

    Map<String, Object> queryConstructionLogById(@Param("id") Long id);
}
