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

    /**
     * 查询方案设计列表
     */
    List<Map<String, Object>> querySolutionDesigns(@Param("params") Map<String, Object> params);

    /**
     * 通过ID查询方案设计
     */
    Map<String, Object> querySolutionDesignById(@Param("id") Long id);

    /**
     * 查询方案设计版本明细
     */
    List<Map<String, Object>> querySolutionDesignDetails(@Param("solutionDesignId") Long solutionDesignId);

    /**
     * 查询方案设计最新版本明细
     */
    SolutionDesignDetail queryLatestDetail(@Param("solutionDesignId") Long solutionDesignId);

    /**
     * 新增方案设计版本明细
     */
    int insertDetail(SolutionDesignDetail detail);

    /**
     * 通过ID更新方案设计版本明细
     */
    int updateDetailById(SolutionDesignDetail detail);

    /**
     * 删除方案设计版本明细
     */
    int deleteDetailsBySolutionDesignId(@Param("solutionDesignId") Long solutionDesignId);
}
