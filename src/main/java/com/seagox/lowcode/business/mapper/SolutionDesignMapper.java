package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.SolutionDesign;
import com.seagox.lowcode.business.entity.SolutionDesignDetail;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 方案设计
 */
public interface SolutionDesignMapper extends BaseMapper<SolutionDesign> {

    List<Map<String, Object>> querySolutionDesigns(@Param("params") Map<String, Object> params);

    Map<String, Object> querySolutionDesignById(@Param("id") Long id);

    List<Map<String, Object>> querySolutionDesignDetails(@Param("solutionDesignId") Long solutionDesignId);

    SolutionDesignDetail queryLatestDetail(@Param("solutionDesignId") Long solutionDesignId);

    int insertDetail(SolutionDesignDetail detail);

    int updateDetailById(SolutionDesignDetail detail);

    int deleteDetailsBySolutionDesignId(@Param("solutionDesignId") Long solutionDesignId);
}
