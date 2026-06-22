package com.seagox.lowcode.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.business.mapper.IssueTicketMapper;
import com.seagox.lowcode.business.service.IIssueTicketService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 问题单
 */
@Service
public class IssueTicketService implements IIssueTicketService {

    /**
     * 问题单数据访问对象
     */
    @Autowired
    private IssueTicketMapper issueTicketMapper;

    /**
     * 分页查询问题单
     */
    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = issueTicketMapper.queryIssueTickets(params);
        return ResultData.success(new PageInfo<>(list));
    }

    /**
     * 查询问题单详情
     */
    @Override
    public ResultData queryById(Long id) {
        Map<String, Object> data = issueTicketMapper.queryIssueTicketById(id);
        if (data == null) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "问题单不存在");
        }
        return ResultData.success(data);
    }
}
