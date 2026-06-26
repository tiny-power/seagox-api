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

    /**
     * 查询维修列表
     */
    List<Map<String, Object>> queryRepairs(@Param("params") Map<String, Object> params);

    /**
     * 通过ID查询维修
     */
    Map<String, Object> queryRepairById(@Param("id") Long id);
}
