package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统角色
 */
public interface RoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询列表通过公司id
     */
    public List<Map<String, Object>> queryByCompanyId(@Param("companyId") Long companyId);
    
    /**
     * 查询用户ids通过角色名称字符串
     */
    public List<String> queryRoleIds(@Param("companyId") Long companyId, @Param("roleStr")String[] roleStr);
    
    
}
