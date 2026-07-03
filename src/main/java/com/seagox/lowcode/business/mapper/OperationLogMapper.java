package com.seagox.lowcode.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.business.entity.OperationLog;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 操作记录
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 查询对象操作记录
     *
     * @param objectType 对象类型
     * @param objectId 对象ID
     * @return 操作记录列表
     */
    List<Map<String, Object>> queryByObject(@Param("objectType") String objectType, @Param("objectId") Long objectId);
}
