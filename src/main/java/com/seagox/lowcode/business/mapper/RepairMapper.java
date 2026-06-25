package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.Repair;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 报修单
 */
public interface RepairMapper extends BaseMapper<Repair> {

    List<Map<String, Object>> queryRepairs(@Param("params") Map<String, Object> params);

    Map<String, Object> queryRepairById(@Param("id") Long id);
}
