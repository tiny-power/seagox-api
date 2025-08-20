package com.seagox.lowcode.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.UserRole;

/**
 * 用户角色
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
	
	/**
     * 查询角色下用户根据角色
     */
    public List<String> queryUserIdByRoleId(Long roleId);
    
    /**
     * 查询列表通过角色id
     */
    public List<Map<String, Object>> queryByRoleId(@Param("roleId") Long roleId);
    
}
