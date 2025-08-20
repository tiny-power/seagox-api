package com.seagox.lowcode.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.Door;

/**
 * 门户
 */
public interface DoorMapper extends BaseMapper<Door> {
	
	/**
     * 查询用户数据
     */
    public Door queryUserData(@Param("parentCompanyId")Long parentCompanyId, @Param("companyId")Long companyId, @Param("userId")Long userId);
    
}
