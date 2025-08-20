package com.seagox.lowcode.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.DeptUser;

/**
 * 部门用户
 */
public interface DeptUserMapper extends BaseMapper<DeptUser> {
	
    /**
     * 查询用户通过部门ids
     */
    public List<String> queryByDepartmentIds(@Param("deptList") List<String> deptList);
    
    /**
     * 查询用户通过公司ids
     */
    public List<String> queryByCompanyIds(@Param("companyList") List<String> companyList);
}
