package com.seagox.lowcode.business.service;

import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 问题单
 */
public interface IIssueTicketService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData queryById(Long id);

}
