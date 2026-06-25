package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.SolutionDesign;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 方案设计
 */
public interface SolutionDesignMapper extends BaseMapper<SolutionDesign> {

    List<Map<String, Object>> querySolutionDesigns(@Param("params") Map<String, Object> params);

    Map<String, Object> querySolutionDesignById(@Param("id") Long id);
}
