package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.ProjectHandover;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 交接单
 */
public interface ProjectHandoverMapper extends BaseMapper<ProjectHandover> {

    List<Map<String, Object>> queryProjectHandovers(@Param("params") Map<String, Object> params);

    Map<String, Object> queryProjectHandoverById(@Param("id") Long id);
}
