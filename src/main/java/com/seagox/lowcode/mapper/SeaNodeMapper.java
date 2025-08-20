package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.SeaNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 流程节点
 */
public interface SeaNodeMapper extends BaseMapper<SeaNode> {

    /**
     * 查询当前节点
     */
    public List<Map<String, Object>> queryCurrentNodeDetail(@Param("defId") Long defId, @Param("assignee") String assignee);
}
