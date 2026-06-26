package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.LeaveMessage;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 留言表
 */
public interface ILeaveMessageService {

    /**
     * 分页查询留言
     */
    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    /**
     * 新增留言
     */
    ResultData insert(LeaveMessage leaveMessage, Long userId);

    /**
     * 删除留言
     */
    ResultData delete(Long id);
}
