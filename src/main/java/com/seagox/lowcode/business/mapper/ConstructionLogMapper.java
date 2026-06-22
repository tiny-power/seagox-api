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

    /**
     * 查询施工日志列表
     *
     * @param params 查询条件
     * @return 施工日志列表
     */
    List<Map<String, Object>> queryConstructionLogs(@Param("params") Map<String, Object> params);

    /**
     * 查询施工日志详情
     *
     * @param id 施工日志ID
     * @return 施工日志详情
     */
    Map<String, Object> queryConstructionLogById(@Param("id") Long id);
}
