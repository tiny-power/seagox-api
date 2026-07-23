package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.Knowledge;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 科普内容数据访问对象
 */
public interface KnowledgeMapper extends BaseMapper<Knowledge> {

    /**
     * 查询科普内容列表
     *
     * @param params 查询条件
     * @return 科普内容列表
     */
    List<Map<String, Object>> queryKnowledgeList(@Param("params") Map<String, Object> params);

    /**
     * 查询科普内容详情
     *
     * @param id 科普内容ID
     * @return 科普内容详情
     */
    Map<String, Object> queryKnowledgeById(@Param("id") Long id);
}
