package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.LeaveRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 请假单
 */
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {

    /**
     * 分页查询
     */
    public List<Map<String, Object>> queryByPage(@Param("companyId") Long companyId,
                                                 @Param("applicantId") Long applicantId,
                                                 @Param("applicantName") String applicantName,
                                                 @Param("leaveType") Integer leaveType,
                                                 @Param("status") Integer status,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

}
