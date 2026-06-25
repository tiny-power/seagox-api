package com.seagox.lowcode.business.service;

import com.seagox.lowcode.business.entity.LeaveMessage;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;

/**
 * 留言表
 */
public interface ILeaveMessageService {

    ResultData queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    ResultData insert(LeaveMessage leaveMessage, Long userId);

    ResultData delete(Long id);
}
