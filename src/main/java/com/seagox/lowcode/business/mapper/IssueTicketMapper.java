package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.IssueTicket;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 问题单
 */
public interface IssueTicketMapper extends BaseMapper<IssueTicket> {

    /**
     * 查询问题单列表
     *
     * @param params 查询条件
     * @return 问题单列表
     */
    List<Map<String, Object>> queryIssueTickets(@Param("params") Map<String, Object> params);

    /**
     * 查询问题单详情
     *
     * @param id 问题单ID
     * @return 问题单详情
     */
    Map<String, Object> queryIssueTicketById(@Param("id") Long id);
}
