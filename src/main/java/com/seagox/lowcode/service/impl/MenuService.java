package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Form;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.FormMapper;
import com.seagox.lowcode.entity.SysMenu;
import com.seagox.lowcode.entity.SysRole;
import com.seagox.lowcode.entity.UserRole;
import com.seagox.lowcode.mapper.MenuMapper;
import com.seagox.lowcode.mapper.RoleMapper;
import com.seagox.lowcode.mapper.ShortcutMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;
import com.seagox.lowcode.service.IMenuService;
import com.seagox.lowcode.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
    private FormMapper formMapper;
    
    @Autowired
    private ShortcutMapper shortcutMapper;
    
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
        if(menu.getType().equals(1)) {
        	if(menu.getClassify().equals(1)) {
        		Form form = formMapper.selectById(menu.getPath());
            	SysMenu addMenu = new SysMenu();
            	addMenu.setCompanyId(menu.getCompanyId());
            	addMenu.setClassify(1);
            	addMenu.setIcon(menu.getIcon());
            	addMenu.setName("新增");
            	addMenu.setParentId(menu.getId());
            	addMenu.setPath(form.getMark() + ":add");
            	addMenu.setType(2);
            	addMenu.setSort(1);
            	menuMapper.insert(addMenu);
            	
            	SysMenu editMenu = new SysMenu();
            	editMenu.setCompanyId(menu.getCompanyId());
            	editMenu.setClassify(1);
            	editMenu.setIcon(menu.getIcon());
            	editMenu.setName("编辑");
            	editMenu.setParentId(menu.getId());
            	editMenu.setPath(form.getMark() + ":edit");
            	editMenu.setType(2);
            	editMenu.setSort(2);
            	menuMapper.insert(editMenu);
            	
            	SysMenu deleteMenu = new SysMenu();
            	deleteMenu.setCompanyId(menu.getCompanyId());
            	deleteMenu.setClassify(1);
            	deleteMenu.setIcon(menu.getIcon());
            	deleteMenu.setName("删除");
            	deleteMenu.setParentId(menu.getId());
            	deleteMenu.setPath(form.getMark() + ":delete");
            	deleteMenu.setType(2);
            	deleteMenu.setSort(3);
            	menuMapper.insert(deleteMenu);
            	
            	SysMenu viewMenu = new SysMenu();
            	viewMenu.setCompanyId(menu.getCompanyId());
            	viewMenu.setClassify(1);
            	viewMenu.setIcon(menu.getIcon());
            	viewMenu.setName("查看");
            	viewMenu.setParentId(menu.getId());
            	viewMenu.setPath(form.getMark() + ":view");
            	viewMenu.setType(2);
            	viewMenu.setSort(4);
            	menuMapper.insert(viewMenu);
            	
            	SysMenu exportMenu = new SysMenu();
            	exportMenu.setCompanyId(menu.getCompanyId());
            	exportMenu.setClassify(1);
            	exportMenu.setIcon(menu.getIcon());
            	exportMenu.setName("导出");
            	exportMenu.setParentId(menu.getId());
            	exportMenu.setPath(form.getMark() + ":export");
            	exportMenu.setType(2);
            	exportMenu.setSort(5);
            	menuMapper.insert(exportMenu);
            	
            	SysMenu importMenu = new SysMenu();
            	importMenu.setCompanyId(menu.getCompanyId());
            	importMenu.setClassify(1);
            	importMenu.setIcon(menu.getIcon());
            	importMenu.setName("导入");
            	importMenu.setParentId(menu.getId());
            	importMenu.setPath(form.getMark() + ":import");
            	importMenu.setType(2);
            	importMenu.setSort(6);
            	menuMapper.insert(importMenu);
        	}
        }
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
        if(classify.equals(1)) {
        	Map<String, Object> shortcut = new HashMap<>();
            shortcut.put("icon", "iconfont icon-xihuan");
            shortcut.put("name", "我的收藏");
            shortcut.put("type", 5);
            shortcut.put("path", "shortcut");
            shortcut.put("children", shortcutMapper.queryListByUserId(companyId, userId));
        	list.add(0, shortcut);
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
