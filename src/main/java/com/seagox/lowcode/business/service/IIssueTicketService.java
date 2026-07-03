package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.IssueTicket;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 问题单
 */
public interface IIssueTicketService {

    /**
     * 分页查询问题单
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param params 查询条件
     * @return 查询结果
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 查询问题单详情
     *
     * @param id 问题单ID
     * @return 查询结果
     */
    ResultData queryById(Long id);

    /**
     * 新增问题单
     *
     * @param issueTicket 问题单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData insert(IssueTicket issueTicket, Long userId);

    /**
     * 修改问题单
     *
     * @param issueTicket 问题单
     * @param userId 当前用户ID
     * @return 操作结果
     */
    ResultData update(IssueTicket issueTicket, Long userId);

    /**
     * 删除问题单
     *
     * @param id 问题单ID
     * @return 操作结果
     */
    ResultData delete(Long id);

}
