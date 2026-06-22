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

    List<Map<String, Object>> queryIssueTickets(@Param("params") Map<String, Object> params);

    Map<String, Object> queryIssueTicketById(@Param("id") Long id);
}
