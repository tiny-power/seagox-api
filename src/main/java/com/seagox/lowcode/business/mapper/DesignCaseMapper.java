package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.DesignCase;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 案例库数据访问对象
 */
public interface DesignCaseMapper extends BaseMapper<DesignCase> {

    /**
     * 查询案例库列表
     *
     * @param params 查询条件
     * @return 案例库列表
     */
    List<Map<String, Object>> queryDesignCases(@Param("params") Map<String, Object> params);

    /**
     * 查询案例库详情
     *
     * @param id 案例ID
     * @return 案例详情
     */
    Map<String, Object> queryDesignCaseById(@Param("id") Long id);
}
