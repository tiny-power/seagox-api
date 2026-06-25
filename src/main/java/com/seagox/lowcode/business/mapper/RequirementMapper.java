package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.Requirement;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 需求沟通表
 */
public interface RequirementMapper extends BaseMapper<Requirement> {

    /**
     * 查询需求沟通列表
     *
     * @param params 查询条件
     * @return 需求沟通列表
     */
    List<Map<String, Object>> queryRequirements(@Param("params") Map<String, Object> params);

    /**
     * 查询需求沟通详情
     *
     * @param id 需求沟通ID
     * @return 需求沟通详情
     */
    Map<String, Object> queryRequirementById(@Param("id") Long id);
}
