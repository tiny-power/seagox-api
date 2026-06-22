package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.service.IIssueTicketService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 问题单
 */
@Service
public class IssueTicketService extends AbstractReadOnlyBusinessService implements IIssueTicketService {

    @Autowired
    private IssueTicketMapper issueTicketMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> issueTicketMapper.queryIssueTickets(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(issueTicketMapper.queryIssueTicketById(id), "问题单");
    }
}
