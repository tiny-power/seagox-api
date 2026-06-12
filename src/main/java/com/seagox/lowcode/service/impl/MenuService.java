package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.entity.SysMenu;
import com.seagox.lowcode.entity.SysRole;
import com.seagox.lowcode.entity.UserRole;
import com.seagox.lowcode.mapper.MenuMapper;
import com.seagox.lowcode.mapper.RoleMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;
import com.seagox.lowcode.service.IMenuService;
import com.seagox.lowcode.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuService implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRelateMapper;

    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private CompanyMapper companyMapper;
    
    /**
     * 查询全部通过公司id
     */
    @Override
    public ResultData queryByCompanyId(Long companyId, int classify, int status) {
    	Company company = companyMapper.selectById(companyId);
    	if(company.getParentId() != null) {
    		String prefix = company.getCode().substring(0, 4);
        	LambdaQueryWrapper<Company> qw = new LambdaQueryWrapper<>();
            qw.eq(Company::getCode, prefix);
            companyId = companyMapper.selectOne(qw).getId();
    	}
        List<Map<String, Object>> list = menuMapper.queryByCompanyId(companyId, classify, status);
        return ResultData.success(TreeUtils.categoryTreeHandle(list, "parentId", 0L));
    }
    
    @Transactional
    @Override
    public ResultData insert(SysMenu menu) {
        menuMapper.insert(menu);
        return ResultData.success(null);
    }


    @Override
    public ResultData update(SysMenu menu) {
        menuMapper.updateById(menu);
        return ResultData.success(null);
    }


    @Override
    public ResultData delete(Long id) {
        LambdaQueryWrapper<SysMenu> qw = new LambdaQueryWrapper<>();
        qw.eq(SysMenu::getParentId, id);
        Long count = menuMapper.selectCount(qw);
        if (count != 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "存在子菜单，不可删除");
        }
        menuMapper.deleteById(id);
        return ResultData.success(null);
    }

    @Override
    public ResultData queryUserMenu(Long companyId, Long userId, Integer classify) {
        LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
        qw.eq(UserRole::getCompanyId, companyId)
        	.eq(UserRole::getUserId, userId);
        List<UserRole> userRelateList = userRelateMapper.selectList(qw);
        String roleStr= "";
        for(int i=0;i<userRelateList.size();i++) {
        	SysRole role = roleMapper.selectById(userRelateList.get(i).getRoleId());
            if (StringUtils.isEmpty(roleStr)) {
                roleStr = roleStr + role.getPath();
            } else {
                roleStr = roleStr + "," + role.getPath();
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
        if (!StringUtils.isEmpty(roleStr)) {
        	list = TreeUtils.categoryTreeHandle(menuMapper.queryUserMenu(roleStr.split(","), classify), "parentId", 0L);
        }
        return ResultData.success(list);
    }

    @Override
    public ResultData queryQuickAccess(Long companyId, Long userId) {
    	LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
        qw.eq(UserRole::getCompanyId, companyId)
        	.eq(UserRole::getUserId, userId);
        List<UserRole> userRelateList = userRelateMapper.selectList(qw);
        String roleStr= "";
        for(int i=0;i<userRelateList.size();i++) {
        	SysRole role = roleMapper.selectById(userRelateList.get(i).getRoleId());
            if (StringUtils.isEmpty(roleStr)) {
                roleStr = roleStr + role.getPath();
            } else {
                roleStr = roleStr + "," + role.getPath();
            }
        }
        if (!StringUtils.isEmpty(roleStr)) {
        	return ResultData.success(menuMapper.queryQuickAccess(roleStr.split(",")));
        }
        return ResultData.success(null);
    }

}
