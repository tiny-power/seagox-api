package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 操作日记
 */
public interface LogMapper extends BaseMapper<SysLog> {

    public List<Map<String, Object>> queryList(@Param("type") String type, @Param("account") String account, @Param("name") String name, @Param("uri")String uri, @Param("status")Integer status);

}
