package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.Project;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 项目
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目列表，包含已使用预算金额
     */
    List<Map<String, Object>> queryFinanceProjects(@Param("params") Map<String, Object> params);

}
