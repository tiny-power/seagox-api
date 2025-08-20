package com.seagox.lowcode.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.FormAthority;

/**
 * 表单权限
 */
public interface FormAthorityMapper extends BaseMapper<FormAthority> {
	
	/**
     * 查询用户数据
     */
    public List<String> queryUserField(Long formId, int type, Long companyId, Long userId);
    
    /**
     * 查询用户数据
     */
    public List<String> queryUserScope(Long formId, Long companyId, Long userId);
    
}
