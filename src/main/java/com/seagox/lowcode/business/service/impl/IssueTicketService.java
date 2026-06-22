package com.seagox.lowcode.business.service.impl;

import com.seagox.lowcode.business.service.IIssueTicketService;
import com.seagox.lowcode.business.mapper.BusinessDocumentMapper;
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
    private BusinessDocumentMapper documentMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        return queryByPage(pageNo, pageSize, () -> documentMapper.queryIssueTickets(params));
    }

    @Override
    public ResultData queryById(Long id) {
        return queryById(documentMapper.queryIssueTicketById(id), "问题单");
    }
}
