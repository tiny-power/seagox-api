package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysRole;
import com.seagox.lowcode.entity.UserRole;
import com.seagox.lowcode.mapper.RoleMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;
import com.seagox.lowcode.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class RoleService implements IRoleService {
	
	@Value(value = "${spring.datasource.url}")
    private String datasourceUrl;
	
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId) {
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> list = roleMapper.queryByCompanyId(companyId);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    /**
     * 新增角色
     */
    @Override
    public ResultData insert(SysRole role) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getName, role.getName())
                .eq(SysRole::getCompanyId, role.getCompanyId());
        Long count = roleMapper.selectCount(queryWrapper);
        if (count == 0) {
            roleMapper.insert(role);
            return ResultData.success(null);
        } else {
            return ResultData.warn(ResultCode.OTHER_ERROR, "角色名称已经存在");
        }
    }

    /**
     * 更新角色
     */
    @Override
    public ResultData update(SysRole role) {
        SysRole originalRole = roleMapper.selectById(role.getId());
        if (originalRole.getName().equals(role.getName())) {
            roleMapper.updateById(role);
            return ResultData.success(null);
        } else {
            LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRole::getName, role.getName())
                    .eq(SysRole::getCompanyId, role.getCompanyId());
            Long count = roleMapper.selectCount(queryWrapper);
            if (count == 0) {
                roleMapper.updateById(role);
                return ResultData.success(null);
            } else {
                return ResultData.warn(ResultCode.OTHER_ERROR, "角色名称已经存在");
            }
        }
    }

    /**
     * 删除角色
     */
    @Override
    public ResultData delete(Long id) {
    	LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
    	qw.eq(UserRole::getRoleId, id);
    	Long count = userRoleMapper.selectCount(qw);
        if (count != 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "角色已经被引用，不可删除");
        }
        roleMapper.deleteById(id);
        return ResultData.success(null);
    }

	/**
	 * 查询角色
	 */
	@Override
	public ResultData queryAll(Long companyId) {
		List<Map<String, Object>> list = roleMapper.queryByCompanyId(companyId);
		return ResultData.success(list);
	}
    
    @Transactional
    @Override
    public ResultData authorize(Long companyId, String userIds, Long roleId) {
    	LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
    	qw.eq(UserRole::getRoleId, roleId);
    	userRoleMapper.delete(qw);
    	if(!StringUtils.isEmpty(userIds)) {
    		String[] userArray = userIds.split(",");
        	for (int i = 0; i < userArray.length; i++) {
        		UserRole userRole = new UserRole();
        		userRole.setCompanyId(companyId);
        		userRole.setRoleId(roleId);
        		userRole.setUserId(Long.valueOf(userArray[i]));
        		userRoleMapper.insert(userRole);
        	}
    	}
        return ResultData.success(null);
    }

}
