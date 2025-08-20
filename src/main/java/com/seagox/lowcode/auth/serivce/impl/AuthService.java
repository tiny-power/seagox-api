package com.seagox.lowcode.auth.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.SysAccount;
import com.seagox.lowcode.entity.SysMenu;
import com.seagox.lowcode.entity.SysRole;
import com.seagox.lowcode.entity.UserRole;
import com.seagox.lowcode.auth.serivce.IAuthService;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.security.JwtTokenUtils;
import com.seagox.lowcode.entity.*;
import com.seagox.lowcode.mapper.*;
import com.seagox.lowcode.util.EncryptUtils;
import com.seagox.lowcode.util.TreeUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService implements IAuthService {
	
	@Autowired
	private AccountMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private DeptUserMapper deptUserMapper;

	@Override
	public ResultData login(String account, String password, String openid, String avatar) {
		LambdaQueryWrapper<SysAccount> qw = new LambdaQueryWrapper<SysAccount>();
		qw.eq(SysAccount::getAccount, account).eq(SysAccount::getStatus, 1);
		SysAccount queryUser = userMapper.selectOne(qw);
		if (queryUser == null) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
		} else {
			boolean result = EncryptUtils.checkpw(password, queryUser.getPassword());
			if(!result) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
			}

			// 更新openid
			if (!StringUtils.isEmpty(openid)) {
				queryUser.setOpenid(openid);
				queryUser.setAvatar(avatar);
				userMapper.updateById(queryUser);
			}

			Map<String, Object> claims = new HashMap<String, Object>();
			Map<String, Object> payload = new HashMap<String, Object>();
			claims.put("userId", queryUser.getId());
			payload.put("userId", queryUser.getId());
			LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
			deptUserQw.eq(DeptUser::getUserId, queryUser.getId())
			.orderByDesc(DeptUser::getUpdateTime);
			List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
			List<String> companyIds = new ArrayList<>();
			for(int i=0;i<deptUserList.size();i++) {
				DeptUser deptUser = deptUserList.get(i);
				companyIds.add(String.valueOf(deptUser.getCompanyId()));
				if(i == 0) {
					claims.put("departmentId", deptUser.getDepartmentId());
				}
			}
			List<Map<String, Object>> companyList = companyMapper.queryByIds(companyIds.toArray(new String[] {}),
					StringUtils.join(companyIds, ","), false);
			Map<String, Object> company = companyList.get(0);
			claims.put("companyId", company.get("id"));
			payload.put("companyId", company.get("id"));
			payload.put("mark", company.get("mark"));
			claims.put("mark", company.get("mark"));
			claims.put("company", TreeUtils.categoryTreeHandle(companyList, "parentId", 0L));
			claims.put("logo", company.get("logo"));
			claims.put("alias", company.get("alias"));
			
			LambdaQueryWrapper<UserRole> userRoleQw = new LambdaQueryWrapper<>();
			userRoleQw.eq(UserRole::getCompanyId, company.get("id")).eq(UserRole::getUserId, queryUser.getId());
			List<UserRole> userRelateList = userRoleMapper.selectList(userRoleQw);
			if (userRelateList.size() > 0) {
				String permissions = "";
				for (int i = 0; i < userRelateList.size(); i++) {
					SysRole role = roleMapper.selectById(userRelateList.get(i).getRoleId());
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
					permissions = StringUtils.join(menuMapper.queryUserMenuStr(permissions.split(",")), ",");
				}
				claims.put("routes", routes);
				claims.put("permissions", permissions);
			} else {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未绑定角色");
			}
			String accessToken = JwtTokenUtils.sign(payload, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
			claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);
			claims.put("account", account);
			claims.put("name", queryUser.getName());
			claims.put("avatar", queryUser.getAvatar());
			claims.put("phone", queryUser.getPhone());
			claims.put("position", queryUser.getPosition());
			claims.put("sex", queryUser.getSex());
			return ResultData.success(claims);
		}
	}
	
	@Override
	public ResultData loginConsole(String account, String password) {
		LambdaQueryWrapper<SysAccount> qw = new LambdaQueryWrapper<SysAccount>();
		qw.eq(SysAccount::getAccount, account)
		.eq(SysAccount::getStatus, 1)
		.eq(SysAccount::getType, 2);
		SysAccount queryUser = userMapper.selectOne(qw);
		if (queryUser == null) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
		} else {
			boolean result = EncryptUtils.checkpw(password, queryUser.getPassword());
			if(!result) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
			}
			Map<String, Object> claims = new HashMap<String, Object>();
			Map<String, Object> payload = new HashMap<String, Object>();
			claims.put("userId", queryUser.getId());
			payload.put("userId", queryUser.getId());
			
			LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
			deptUserQw.eq(DeptUser::getUserId, queryUser.getId())
			.orderByDesc(DeptUser::getUpdateTime);
			List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
			List<String> companyIds = new ArrayList<>();
			for(int i=0;i<deptUserList.size();i++) {
				DeptUser deptUser = deptUserList.get(i);
				companyIds.add(String.valueOf(deptUser.getCompanyId()));
				if(i == 0) {
					claims.put("departmentId", deptUser.getDepartmentId());
				}
			}
			List<Map<String, Object>> companyList = companyMapper.queryByIds(companyIds.toArray(new String[] {}),
					StringUtils.join(companyIds, ","), true);
			Map<String, Object> company = companyList.get(0);
			claims.put("companyId", company.get("id"));
			payload.put("companyId", company.get("id"));
			payload.put("mark", company.get("mark"));
			claims.put("company", companyList);
			claims.put("logo", company.get("logo"));
			claims.put("alias", company.get("alias"));
			
			String accessToken = JwtTokenUtils.sign(payload, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
			claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);
			claims.put("account", account);
			claims.put("name", queryUser.getName());
			claims.put("avatar", queryUser.getAvatar());
			return ResultData.success(claims);
		}
	}

	@Override
	public ResultData verifyLogin(String orgstr, String account, String noncestr, String timestamp, String sign) {
		String key = "yVwlsbIrY3q22EnoYYM4nR5zqTmqed05";
		String ratioSign = EncryptUtils.md5Encode("account=" + account + "&noncestr=" + noncestr + "&org=" + orgstr
				+ "&timestamp=" + timestamp + "&key=" + key).toUpperCase();
		if (ratioSign.equals(sign)) {
			SysAccount queryUser = userMapper.selectById(account);
			if (queryUser == null) {
				return ResultData.warn(ResultCode.SIGN_ERROR, "签名错误");
			} else {
				Map<String, Object> payload = new HashMap<String, Object>();
				Map<String, Object> claims = new HashMap<String, Object>();
				payload.put("userId", queryUser.getId());
				payload.put("companyId", orgstr);
				Company companyInfo = companyMapper.selectById(orgstr);
				payload.put("mark", companyInfo.getMark());
				String accessToken = JwtTokenUtils.sign(payload, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
				claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);
				claims.put("account", account);
				claims.put("name", queryUser.getName());
				claims.put("avatar", queryUser.getAvatar());
				claims.put("userId", queryUser.getId());
				LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
				deptUserQw.eq(DeptUser::getUserId, queryUser.getId())
				.orderByDesc(DeptUser::getUpdateTime);
				List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
				List<String> companyIds = new ArrayList<>();
				for(int i=0;i<deptUserList.size();i++) {
					DeptUser deptUser = deptUserList.get(i);
					companyIds.add(String.valueOf(deptUser.getCompanyId()));
					if(i == 0) {
						claims.put("departmentId", deptUser.getDepartmentId());
					}
				}
				List<Map<String, Object>> companyList = companyMapper.queryByIds(companyIds.toArray(new String[] {}),
						StringUtils.join(companyIds, ","), false);
				Map<String, Object> company = companyList.get(0);
				claims.put("companyId", company.get("id"));
				claims.put("mark", company.get("mark"));
				claims.put("company", companyList);
				claims.put("logo", company.get("logo"));
				claims.put("alias", company.get("alias"));
				
				LambdaQueryWrapper<UserRole> userRoleQw = new LambdaQueryWrapper<>();
				userRoleQw.eq(UserRole::getCompanyId, company.get("id")).eq(UserRole::getUserId, queryUser.getId());
				List<UserRole> userRelateList = userRoleMapper.selectList(userRoleQw);
				if (userRelateList.size() > 0) {
					String permissions = "";
					for (int i = 0; i < userRelateList.size(); i++) {
						SysRole role = roleMapper.selectById(userRelateList.get(i).getRoleId());
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
						permissions = StringUtils.join(menuMapper.queryUserMenuStr(permissions.split(",")), ",");
					}
					claims.put("routes", routes);
					claims.put("permissions", permissions);
				} else {
					return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未绑定角色");
				}
				return ResultData.success(claims);
			}
		} else {
			return ResultData.warn(ResultCode.SIGN_ERROR, "签名错误");
		}
	}

	@Override
	public ResultData verifyByOpenid(String openid) {
		LambdaQueryWrapper<SysAccount> qw = new LambdaQueryWrapper<>();
		qw.eq(SysAccount::getOpenid, openid).eq(SysAccount::getStatus, 1);
		SysAccount queryUser = userMapper.selectOne(qw);
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("openid", openid);
		if (queryUser == null) {
			claims.put("bindFlag", false);
		} else {
			claims.put("bindFlag", true);
		}
		return ResultData.success(claims);
	}

	@Override
	public ResultData loginByOpenid(String openid) {
		LambdaQueryWrapper<SysAccount> queryWrapper = new LambdaQueryWrapper<SysAccount>();
		queryWrapper.eq(SysAccount::getOpenid, openid).eq(SysAccount::getStatus, 1);
		SysAccount queryUser = userMapper.selectOne(queryWrapper);
		if (queryUser == null) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
		} else {
			Map<String, Object> claims = new HashMap<String, Object>();
			Map<String, Object> payload = new HashMap<String, Object>();
			claims.put("userId", queryUser.getId());
			payload.put("userId", queryUser.getId());
			
			LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
			deptUserQw.eq(DeptUser::getUserId, queryUser.getId())
			.orderByDesc(DeptUser::getUpdateTime);
			List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
			List<String> companyIds = new ArrayList<>();
			for(int i=0;i<deptUserList.size();i++) {
				DeptUser deptUser = deptUserList.get(i);
				companyIds.add(String.valueOf(deptUser.getCompanyId()));
				if(i == 0) {
					claims.put("departmentId", deptUser.getDepartmentId());
				}
			}
			List<Map<String, Object>> companyList = companyMapper.queryByIds(companyIds.toArray(new String[] {}),
					StringUtils.join(companyIds, ","), false);
			Map<String, Object> company = companyList.get(0);
			claims.put("companyId", company.get("id"));
			payload.put("companyId", company.get("id"));
			payload.put("mark", company.get("mark"));
			claims.put("company", companyList);
			claims.put("logo", company.get("logo"));
			claims.put("alias", company.get("alias"));
			
			String accessToken = JwtTokenUtils.sign(payload, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
			claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);
			claims.put("account", queryUser.getAccount());
			claims.put("name", queryUser.getName());
			claims.put("avatar", queryUser.getAvatar());
			claims.put("phone", queryUser.getPhone());
			claims.put("position", queryUser.getPosition());
			claims.put("sex", queryUser.getSex());
			return ResultData.success(claims);
		}
	}
}
