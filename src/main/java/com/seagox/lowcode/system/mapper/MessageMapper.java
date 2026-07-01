package com.seagox.lowcode.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.system.entity.SysMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 消息
 */
public interface MessageMapper extends BaseMapper<SysMessage> {

    /**
     * 查询所有
     */
    public List<Map<String, Object>> queryAll(@Param("companyId") long companyId, @Param("toUserId") long toUserId, @Param("status") Integer status, @Param("title") String title, @Param("type") Integer type);

    /**
     * 查询所有数量
     */
    public int queryCount(@Param("companyId") long companyId, @Param("toUserId") long toUserId);

    /**
     * 分组查询未读数量
     */
    public List<Map<String, Object>> queryUnreadGroup(@Param("companyId") long companyId, @Param("toUserId") long toUserId);

    /**
     * 删除消息记录
     */
    public void deleteMessage(@Param("businessType") String businessType, @Param("businessKey") Long businessKey);

}
