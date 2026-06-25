package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.LeaveMessage;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 留言表
 */
public interface LeaveMessageMapper extends BaseMapper<LeaveMessage> {

    List<Map<String, Object>> queryLeaveMessages(@Param("params") Map<String, Object> params);
}
