package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.security.JwtTokenUtils;
import com.seagox.lowcode.entity.*;
import com.seagox.lowcode.mapper.*;
import com.seagox.lowcode.service.ICompanyService;
import com.seagox.lowcode.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;

@Service
public class CompanyService implements ICompanyService {

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private DeptUserMapper deptUserMapper;

	@Override
	public ResultData queryAll(Long companyId) {
		Company company = companyMapper.selectById(companyId);
		String prefix = company.getCode().substring(0, 4);
		List<Map<String, Object>> list = companyMapper.queryByPrefix(prefix);
		return ResultData.success(TreeUtils.categoryTreeHandle(list, "parentId", 0L));
	}

	@Transactional
	@Override
	public ResultData insert(Long companyId, Long userId, Company company) {
		LambdaQueryWrapper<Company> qw = new LambdaQueryWrapper<>();
		qw.eq(Company::getName, company.getName());
		Long count = companyMapper.selectCount(qw);
		if (count == 0) {
			Company parentCompany = companyMapper.selectById(company.getParentId());
			String maxCode = "";
			if (parentCompany != null) {
				maxCode = companyMapper.queryMaxCode(parentCompany.getCode(), parentCompany.getCode().length() + 4);
				if (StringUtils.isEmpty(maxCode)) {
					maxCode = parentCompany.getCode() + "0000";
				}
				company.setMark(parentCompany.getMark());
			} else {
				maxCode = companyMapper.queryMaxCode("", 4);
				if (StringUtils.isEmpty(maxCode)) {
					maxCode = "1000";
				}
			}
			BigInteger bigInteger = new BigInteger(maxCode);
			bigInteger = bigInteger.add(BigInteger.valueOf(1));
			company.setCode(bigInteger.toString());
			companyMapper.insert(company);
			List<String> pathList = new ArrayList<>();
			if (company.getParentId() == null) {
				SysMenu orgMenu = new SysMenu();
				orgMenu.setCompanyId(company.getId());
				orgMenu.setParentId(null);
				orgMenu.setType(5);
				orgMenu.setName("组织架构");
				orgMenu.setIcon("iconfont icon-xihuan");
				orgMenu.setPath("organization");
				orgMenu.setSort(1);
				menuMapper.insert(orgMenu);
				pathList.add(String.valueOf(orgMenu.getId()));

				SysMenu contactMenu = new SysMenu();
				contactMenu.setCompanyId(company.getId());
				contactMenu.setParentId(orgMenu.getId());
				contactMenu.setType(4);
				contactMenu.setName("通讯录");
				contactMenu.setIcon("iconfont icon-xihuan");
				contactMenu.setPath("contact");
				contactMenu.setSort(1);
				menuMapper.insert(contactMenu);
				pathList.add(String.valueOf(contactMenu.getId()));

				SysMenu roleMenu = new SysMenu();
				roleMenu.setCompanyId(company.getId());
				roleMenu.setParentId(orgMenu.getId());
				roleMenu.setType(4);
				roleMenu.setName("角色管理");
				roleMenu.setIcon("iconfont icon-xihuan");
				roleMenu.setPath("role");
				roleMenu.setSort(2);
				menuMapper.insert(roleMenu);
				pathList.add(String.valueOf(roleMenu.getId()));

				SysMenu userExportMenu = new SysMenu();
				userExportMenu.setCompanyId(company.getId());
				userExportMenu.setParentId(contactMenu.getId());
				userExportMenu.setType(2);
				userExportMenu.setName("导出用户");
				userExportMenu.setIcon("iconfont icon-xihuan");
				userExportMenu.setPath("user:export");
				userExportMenu.setSort(1);
				menuMapper.insert(userExportMenu);
				pathList.add(String.valueOf(userExportMenu.getId()));

				SysMenu userDownloadMenu = new SysMenu();
				userDownloadMenu.setCompanyId(company.getId());
				userDownloadMenu.setParentId(contactMenu.getId());
				userDownloadMenu.setType(2);
				userDownloadMenu.setName("导出用户模板");
				userDownloadMenu.setIcon("iconfont icon-xihuan");
				userDownloadMenu.setPath("user:download");
				userDownloadMenu.setSort(1);
				menuMapper.insert(userDownloadMenu);
				pathList.add(String.valueOf(userDownloadMenu.getId()));

				SysMenu deptDownloadMenu = new SysMenu();
				deptDownloadMenu.setCompanyId(company.getId());
				deptDownloadMenu.setParentId(contactMenu.getId());
				deptDownloadMenu.setType(2);
				deptDownloadMenu.setName("导出部门模板");
				deptDownloadMenu.setIcon("iconfont icon-xihuan");
				deptDownloadMenu.setPath("dept:download");
				deptDownloadMenu.setSort(1);
				menuMapper.insert(deptDownloadMenu);
				pathList.add(String.valueOf(deptDownloadMenu.getId()));

				SysMenu userAddMenu = new SysMenu();
				userAddMenu.setCompanyId(company.getId());
				userAddMenu.setParentId(contactMenu.getId());
				userAddMenu.setType(2);
				userAddMenu.setName("新增用户");
				userAddMenu.setIcon("iconfont icon-xihuan");
				userAddMenu.setPath("user:add");
				userAddMenu.setSort(1);
				menuMapper.insert(userAddMenu);
				pathList.add(String.valueOf(userAddMenu.getId()));

				SysMenu userEditMenu = new SysMenu();
				userEditMenu.setCompanyId(company.getId());
				userEditMenu.setParentId(contactMenu.getId());
				userEditMenu.setType(2);
				userEditMenu.setName("编辑用户");
				userEditMenu.setIcon("iconfont icon-xihuan");
				userEditMenu.setPath("user:edit");
				userEditMenu.setSort(1);
				menuMapper.insert(userEditMenu);
				pathList.add(String.valueOf(userEditMenu.getId()));

				SysMenu userDeleteMenu = new SysMenu();
				userDeleteMenu.setCompanyId(company.getId());
				userDeleteMenu.setParentId(contactMenu.getId());
				userDeleteMenu.setType(2);
				userDeleteMenu.setName("删除用户");
				userDeleteMenu.setIcon("iconfont icon-xihuan");
				userDeleteMenu.setPath("user:delete");
				userDeleteMenu.setSort(1);
				menuMapper.insert(userDeleteMenu);
				pathList.add(String.valueOf(userDeleteMenu.getId()));

				SysMenu userResetMenu = new SysMenu();
				userResetMenu.setCompanyId(company.getId());
				userResetMenu.setParentId(contactMenu.getId());
				userResetMenu.setType(2);
				userResetMenu.setName("密码重置");
				userResetMenu.setIcon("iconfont icon-xihuan");
				userResetMenu.setPath("user:reset");
				userResetMenu.setSort(1);
				menuMapper.insert(userResetMenu);
				pathList.add(String.valueOf(userResetMenu.getId()));

				SysMenu userImportMenu = new SysMenu();
				userImportMenu.setCompanyId(company.getId());
				userImportMenu.setParentId(contactMenu.getId());
				userImportMenu.setType(2);
				userImportMenu.setName("导入用户");
				userImportMenu.setIcon("iconfont icon-xihuan");
				userImportMenu.setPath("user:import");
				userImportMenu.setSort(1);
				menuMapper.insert(userImportMenu);
				pathList.add(String.valueOf(userImportMenu.getId()));

				SysMenu deptAddMenu = new SysMenu();
				deptAddMenu.setCompanyId(company.getId());
				deptAddMenu.setParentId(contactMenu.getId());
				deptAddMenu.setType(2);
				deptAddMenu.setName("新增部门");
				deptAddMenu.setIcon("iconfont icon-xihuan");
				deptAddMenu.setPath("dept:add");
				deptAddMenu.setSort(1);
				menuMapper.insert(deptAddMenu);
				pathList.add(String.valueOf(deptAddMenu.getId()));

				SysMenu deptEditMenu = new SysMenu();
				deptEditMenu.setCompanyId(company.getId());
				deptEditMenu.setParentId(contactMenu.getId());
				deptEditMenu.setType(2);
				deptEditMenu.setName("编辑部门");
				deptEditMenu.setIcon("iconfont icon-xihuan");
				deptEditMenu.setPath("dept:edit");
				deptEditMenu.setSort(1);
				menuMapper.insert(deptEditMenu);
				pathList.add(String.valueOf(deptEditMenu.getId()));

				SysMenu deptDeleteMenu = new SysMenu();
				deptDeleteMenu.setCompanyId(company.getId());
				deptDeleteMenu.setParentId(contactMenu.getId());
				deptDeleteMenu.setType(2);
				deptDeleteMenu.setName("删除部门");
				deptDeleteMenu.setIcon("iconfont icon-xihuan");
				deptDeleteMenu.setPath("dept:delete");
				deptDeleteMenu.setSort(1);
				menuMapper.insert(deptDeleteMenu);
				pathList.add(String.valueOf(deptDeleteMenu.getId()));

				SysMenu deptImportMenu = new SysMenu();
				deptImportMenu.setCompanyId(company.getId());
				deptImportMenu.setParentId(contactMenu.getId());
				deptImportMenu.setType(2);
				deptImportMenu.setName("导入部门");
				deptImportMenu.setIcon("iconfont icon-xihuan");
				deptImportMenu.setPath("dept:import");
				deptImportMenu.setSort(1);
				menuMapper.insert(deptImportMenu);
				pathList.add(String.valueOf(deptImportMenu.getId()));

				SysMenu roleAddMenu = new SysMenu();
				roleAddMenu.setCompanyId(company.getId());
				roleAddMenu.setParentId(roleMenu.getId());
				roleAddMenu.setType(2);
				roleAddMenu.setName("新增");
				roleAddMenu.setIcon("iconfont icon-xihuan");
				roleAddMenu.setPath("role:add");
				roleAddMenu.setSort(1);
				menuMapper.insert(roleAddMenu);
				pathList.add(String.valueOf(roleAddMenu.getId()));

				SysMenu roleEditMenu = new SysMenu();
				roleEditMenu.setCompanyId(company.getId());
				roleEditMenu.setParentId(roleMenu.getId());
				roleEditMenu.setType(2);
				roleEditMenu.setName("编辑");
				roleEditMenu.setIcon("iconfont icon-xihuan");
				roleEditMenu.setPath("role:edit");
				roleEditMenu.setSort(1);
				menuMapper.insert(roleEditMenu);
				pathList.add(String.valueOf(roleEditMenu.getId()));

				SysMenu roleDeleteMenu = new SysMenu();
				roleDeleteMenu.setCompanyId(company.getId());
				roleDeleteMenu.setParentId(roleMenu.getId());
				roleDeleteMenu.setType(2);
				roleDeleteMenu.setName("删除");
				roleDeleteMenu.setIcon("iconfont icon-xihuan");
				roleDeleteMenu.setPath("role:delete");
				roleDeleteMenu.setSort(1);
				menuMapper.insert(roleDeleteMenu);
				pathList.add(String.valueOf(roleDeleteMenu.getId()));

				SysMenu roleAuthorizeMenu = new SysMenu();
				roleAuthorizeMenu.setCompanyId(company.getId());
				roleAuthorizeMenu.setParentId(roleMenu.getId());
				roleAuthorizeMenu.setType(2);
				roleAuthorizeMenu.setName("授权");
				roleAuthorizeMenu.setIcon("iconfont icon-xihuan");
				roleAuthorizeMenu.setPath("role:authorize");
				roleAuthorizeMenu.setSort(1);
				menuMapper.insert(roleAuthorizeMenu);
				pathList.add(String.valueOf(roleAuthorizeMenu.getId()));
			} else {
				LambdaQueryWrapper<SysMenu> qwMenu = new LambdaQueryWrapper<>();
				qwMenu.eq(SysMenu::getCompanyId, companyId);
				List<SysMenu> menuList = menuMapper.selectList(qwMenu);
				for (int i = 0; i < menuList.size(); i++) {
					pathList.add(String.valueOf(menuList.get(i).getId()));
				}
			}
			Department department = new Department();
			department.setCompanyId(company.getId());
			department.setCode("101");
			department.setName("默认部门");
			departmentMapper.insert(department);

			DeptUser deptUser = new DeptUser();
			deptUser.setCompanyId(company.getId());
			deptUser.setUserId(userId);
			deptUser.setDepartmentId(department.getId());
			deptUserMapper.insert(deptUser);

			SysRole role = new SysRole();
			role.setCompanyId(company.getId());
			role.setName("默认角色");
			role.setPath(org.apache.commons.lang3.StringUtils.join(pathList, ","));
			roleMapper.insert(role);

			UserRole userRole = new UserRole();
			userRole.setCompanyId(company.getId());
			userRole.setUserId(userId);
			userRole.setRoleId(role.getId());
			userRoleMapper.insert(userRole);

			return ResultData.success(null);
		} else {
			return ResultData.warn(ResultCode.OTHER_ERROR, "单位名称已经存在");
		}
	}

	@Override
	public ResultData update(Company company) {
		Company originalCompany = companyMapper.selectById(company.getId());
		if (originalCompany.getName().equals(company.getName())) {
			if ((originalCompany.getParentId() == null && company.getParentId() != null)
					|| (company.getParentId() == null && originalCompany.getParentId() != null)
					|| (company.getParentId() != null
							&& !originalCompany.getParentId().equals(company.getParentId()))) {
				Company parentCompany = companyMapper.selectById(company.getParentId());
				String maxCode = "";
				if (parentCompany != null) {
					maxCode = companyMapper.queryMaxCode(parentCompany.getCode(), parentCompany.getCode().length() + 4);
					if (StringUtils.isEmpty(maxCode)) {
						maxCode = parentCompany.getCode() + "0000";
					}
				} else {
					maxCode = companyMapper.queryMaxCode("", 4);
					if (StringUtils.isEmpty(maxCode)) {
						maxCode = "1000";
					}
				}
				BigInteger bigInteger = new BigInteger(maxCode);
				bigInteger = bigInteger.add(BigInteger.valueOf(1));
				company.setCode(bigInteger.toString());
			}
			companyMapper.updateById(company);
			return ResultData.success(null);
		} else {
			LambdaQueryWrapper<Company> qw = new LambdaQueryWrapper<>();
			qw.eq(Company::getName, company.getName());
			Long count = companyMapper.selectCount(qw);
			if (count == 0) {
				if ((originalCompany.getParentId() == null && company.getParentId() != null)
						|| (company.getParentId() == null && originalCompany.getParentId() != null)
						|| (company.getParentId() != null
								&& !originalCompany.getParentId().equals(company.getParentId()))) {
					Company parentCompany = companyMapper.selectById(company.getParentId());
					String maxCode = "";
					if (parentCompany != null) {
						maxCode = companyMapper.queryMaxCode(parentCompany.getCode(),
								parentCompany.getCode().length() + 4);
						if (StringUtils.isEmpty(maxCode)) {
							maxCode = parentCompany.getCode() + "0000";
						}
					} else {
						maxCode = companyMapper.queryMaxCode("", 4);
						if (StringUtils.isEmpty(maxCode)) {
							maxCode = "1000";
						}
					}
					BigInteger bigInteger = new BigInteger(maxCode);
					bigInteger = bigInteger.add(BigInteger.valueOf(1));
					company.setCode(bigInteger.toString());
				}
				companyMapper.updateById(company);
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "单位名称已经存在");
			}
		}
	}

	@Override
	public ResultData delete(Long id) {
		LambdaQueryWrapper<Department> deptQw = new LambdaQueryWrapper<>();
		deptQw.eq(Department::getCompanyId, id);
		Long departmentCount = departmentMapper.selectCount(deptQw);
		if (departmentCount != 0) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "存在部门，不可删除");
		}
		companyMapper.deleteById(id);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData change(Long changeCompanyId, Long userId) {
		Company company = companyMapper.selectById(changeCompanyId);
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("userId", userId);
		claims.put("companyId", changeCompanyId);
		claims.put("mark", company.getMark());
		String accessToken = JwtTokenUtils.sign(claims, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
		claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);

		claims.put("logo", company.getLogo());
		claims.put("alias", company.getAlias());

		LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
		deptUserQw.eq(DeptUser::getUserId, userId).eq(DeptUser::getCompanyId, changeCompanyId);
		DeptUser deptUser = deptUserMapper.selectOne(deptUserQw);
		deptUser.setUpdateTime(new Date());
		deptUserMapper.updateById(deptUser);
		claims.put("departmentId", deptUser.getDepartmentId());

		LambdaQueryWrapper<UserRole> qw = new LambdaQueryWrapper<>();
		qw.eq(UserRole::getCompanyId, changeCompanyId).eq(UserRole::getUserId, userId).eq(UserRole::getCompanyId,
				changeCompanyId);
		List<UserRole> userRoleList = userRoleMapper.selectList(qw);
		String permissions = "";
		for (int i = 0; i < userRoleList.size(); i++) {
			SysRole role = roleMapper.selectById(userRoleList.get(i).getRoleId());
			if (StringUtils.isEmpty(permissions)) {
				permissions = permissions + role.getPath();
			} else {
				permissions = permissions + "," + role.getPath();
			}
		}
		List<SysMenu> routes = new ArrayList<>();
		if (!org.springframework.util.StringUtils.isEmpty(permissions)) {
			LambdaQueryWrapper<SysMenu> sysMenuQw = new LambdaQueryWrapper<>();
			sysMenuQw.in(SysMenu::getType, Arrays.asList("4,7".split(","))).in(SysMenu::getId,
					Arrays.asList(permissions.split(",")));
			routes = menuMapper.selectList(sysMenuQw);
			permissions = org.apache.commons.lang3.StringUtils.join(menuMapper.queryUserMenuStr(permissions.split(",")),
					",");
		}
		claims.put("routes", routes);
		claims.put("permissions", permissions);
		return ResultData.success(claims);
	}

	@Override
	public ResultData queryAllAndDept(Long companyId, boolean isAll) {
		List<Map<String, Object>> companyList = new ArrayList<>();
		if(isAll) {
			Company company = companyMapper.selectById(companyId);
			String prefix = company.getCode().substring(0, 4);
			companyList = companyMapper.queryByPrefix(prefix);
		} else {
			Company company = companyMapper.selectById(companyId);
			Map<String, Object> item = new HashMap<>();
			item.put("id", company.getId());
			item.put("code", company.getCode());
			item.put("value", company.getId());
			item.put("name", company.getName());
			item.put("label", company.getName());
			companyList.add(item);
		}
		for (Map<String, Object> map : companyList) {
			List<Map<String, Object>> deptList = departmentMapper.queryByCompanyId(Long.valueOf(map.get("id").toString()));
			List<Map<String, Object>> deptTree = TreeUtils.categoryTreeHandle(deptList, "parentId", 0L);
			map.put("type", "company");
			map.put("children", deptTree);
		}
		return ResultData.success(TreeUtils.categoryTreeHandle(companyList, "parentId", 0L));
	}

	@Override
	public ResultData queryAllAndRole(Long companyId) {
		Company company = companyMapper.selectById(companyId);
		List<Map<String, Object>> companyList = companyMapper.queryByPrefix(company.getCode().substring(0, 4));
		for (Map<String, Object> map : companyList) {
			List<Map<String, Object>> roleList = roleMapper.queryByCompanyId((Long) map.get("id"));
			map.put("children", roleList);
			map.put("type", "company");
		}
		return ResultData.success(TreeUtils.categoryTreeHandle(companyList, "parentId", 0L));
	}
}
