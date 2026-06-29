package com.seagox.lowcode.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.business.entity.Project;
import com.seagox.lowcode.business.entity.ProjectMember;
import com.seagox.lowcode.business.entity.ProjectStage;
import com.seagox.lowcode.business.entity.ProjectStageDependency;
import com.seagox.lowcode.business.entity.StageInspectionItem;
import com.seagox.lowcode.business.mapper.ProjectMapper;
import com.seagox.lowcode.business.mapper.ProjectMemberMapper;
import com.seagox.lowcode.business.mapper.ProjectStageDependencyMapper;
import com.seagox.lowcode.business.mapper.ProjectStageMapper;
import com.seagox.lowcode.business.mapper.StageInspectionItemMapper;
import com.seagox.lowcode.system.entity.SysAccount;
import com.seagox.lowcode.system.entity.SysMenu;
import com.seagox.lowcode.system.entity.SysRole;
import com.seagox.lowcode.system.entity.UserRole;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.security.JwtTokenUtils;
import com.seagox.lowcode.system.service.IAuthService;
import com.seagox.lowcode.system.entity.*;
import com.seagox.lowcode.system.mapper.*;
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

	@Autowired
	private PhoneCodeMapper phoneCodeMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ProjectMemberMapper projectMemberMapper;

	@Autowired
	private ProjectStageMapper projectStageMapper;

	@Autowired
	private ProjectStageDependencyMapper projectStageDependencyMapper;

	@Autowired
	private StageInspectionItemMapper stageInspectionItemMapper;

	private static final Map<Integer, String> PROJECT_ROLE_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(1, "设计师");
			put(2, "设计助理");
			put(3, "土建项目经理");
			put(4, "精装项目经理");
			put(5, "施工员");
			put(6, "质检员");
			put(7, "成控人员");
			put(8, "财务人员");
			put(9, "管理层");
			put(10, "业主");
			put(11, "业主家属");
		}
	};

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
					sysMenuQw.eq(SysMenu::getType, 2).in(SysMenu::getId, Arrays.asList(permissions.split(",")));
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
	public ResultData miniLogin(String phone, String credential, String loginMode, String openid, String avatar) {
		if (StringUtils.isEmpty(phone)) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "请输入手机号");
		}
		if (StringUtils.isEmpty(credential)) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "请输入登录凭证");
		}

		LambdaQueryWrapper<SysAccount> queryWrapper = new LambdaQueryWrapper<SysAccount>();
		queryWrapper.eq(SysAccount::getStatus, 1)
				.and(wrapper -> wrapper.eq(SysAccount::getPhone, phone).or().eq(SysAccount::getAccount, phone));
		SysAccount queryUser = userMapper.selectOne(queryWrapper);
		if (queryUser == null) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号不存在或已禁用");
		}

		List<ProjectMember> projectMembers = queryMiniProjectMembers(queryUser.getId());
		if (projectMembers.size() == 0) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未加入项目角色");
		}

		if ("password".equals(loginMode)) {
			boolean result = EncryptUtils.checkpw(credential, queryUser.getPassword());
			if (!result) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号或密码错误");
			}
		} else {
			PhoneCode phoneCode = phoneCodeMapper.queryLastPhone(phone);
			if (phoneCode == null || !credential.equals(phoneCode.getCode())) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "验证码错误");
			}
			if (phoneCode.getExpireTime() == null || phoneCode.getExpireTime().before(new Date())) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "验证码已过期");
			}
		}

		if (!StringUtils.isEmpty(openid)) {
			SysAccount openidUser = userMapper.selectOne(new LambdaQueryWrapper<SysAccount>()
					.eq(SysAccount::getOpenid, openid)
					.eq(SysAccount::getStatus, 1));
			if (openidUser != null && !openidUser.getId().equals(queryUser.getId())) {
				return ResultData.warn(ResultCode.PARAMETER_ERROR, "当前微信已绑定其他账号");
			}
			queryUser.setOpenid(openid);
			if (!StringUtils.isEmpty(avatar)) {
				queryUser.setAvatar(avatar);
			}
			userMapper.updateById(queryUser);
		}

		return buildMiniLoginClaims(queryUser, openid, projectMembers);
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
						sysMenuQw.eq(SysMenu::getType, 2).in(SysMenu::getId, Arrays.asList(permissions.split(",")));
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
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("openid", openid);
			claims.put("bindFlag", false);
			return ResultData.success(claims);
		} else {
			return buildMiniLoginClaims(queryUser, openid);
		}
	}

	/**
	 * 组装小程序登录返回数据
	 */
	private ResultData buildMiniLoginClaims(SysAccount queryUser, String openid) {
		return buildMiniLoginClaims(queryUser, openid, queryMiniProjectMembers(queryUser.getId()));
	}

	/**
	 * 查询小程序可登录的项目角色
	 */
	private List<ProjectMember> queryMiniProjectMembers(Long userId) {
		return projectMemberMapper.selectList(new LambdaQueryWrapper<ProjectMember>()
				.eq(ProjectMember::getUserId, userId)
				.eq(ProjectMember::getStatus, 1)
				.orderByAsc(ProjectMember::getProjectId)
				.orderByAsc(ProjectMember::getId));
	}

	/**
	 * 组装小程序登录返回数据
	 */
	private ResultData buildMiniLoginClaims(SysAccount queryUser, String openid, List<ProjectMember> projectMembers) {
		if (projectMembers.size() == 0) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未加入项目角色");
		}

		Map<String, Object> claims = new HashMap<String, Object>();
		Map<String, Object> payload = new HashMap<String, Object>();
		claims.put("bindFlag", true);
		claims.put("openid", openid);
		claims.put("userId", queryUser.getId());
		payload.put("userId", queryUser.getId());

		ResultData orgResult = fillOrganizationClaims(queryUser, claims, payload);
		if (orgResult != null) {
			return orgResult;
		}
		claims.remove("company");

		String accessToken = JwtTokenUtils.sign(payload, JwtTokenUtils.SECRET, JwtTokenUtils.EXPIRATION);
		claims.put("accessToken", JwtTokenUtils.TOKENHEAD + accessToken);
		claims.put("account", queryUser.getAccount());
		claims.put("name", queryUser.getName());
		claims.put("avatar", queryUser.getAvatar());
		claims.put("phone", queryUser.getPhone());
		claims.put("position", queryUser.getPosition());
		claims.put("sex", queryUser.getSex());
		claims.put("projects", buildProjectRows(projectMembers));
		return ResultData.success(claims);
	}

	/**
	 * 组装组织信息与token载荷
	 */
	private ResultData fillOrganizationClaims(SysAccount queryUser, Map<String, Object> claims,
			Map<String, Object> payload) {
		LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
		deptUserQw.eq(DeptUser::getUserId, queryUser.getId()).orderByDesc(DeptUser::getUpdateTime);
		List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
		if (deptUserList.size() == 0) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未绑定部门");
		}

		List<String> companyIds = new ArrayList<>();
		for (int i = 0; i < deptUserList.size(); i++) {
			DeptUser deptUser = deptUserList.get(i);
			companyIds.add(String.valueOf(deptUser.getCompanyId()));
			if (i == 0) {
				claims.put("departmentId", deptUser.getDepartmentId());
			}
		}
		List<Map<String, Object>> companyList = companyMapper.queryByIds(companyIds.toArray(new String[] {}),
				StringUtils.join(companyIds, ","), false);
		if (companyList.size() == 0) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "账号未绑定公司");
		}

		Map<String, Object> company = companyList.get(0);
		claims.put("companyId", company.get("id"));
		payload.put("companyId", company.get("id"));
		payload.put("mark", company.get("mark"));
		claims.put("mark", company.get("mark"));
		claims.put("company", companyList);
		claims.put("logo", company.get("logo"));
		claims.put("alias", company.get("alias"));
		return null;
	}

	/**
	 * 组装项目、项目角色和阶段信息
	 */
	private List<Map<String, Object>> buildProjectRows(List<ProjectMember> projectMembers) {
		List<Map<String, Object>> rows = new ArrayList<>();
		for (ProjectMember projectMember : projectMembers) {
			Project project = projectMapper.selectById(projectMember.getProjectId());
			if (project == null) {
				continue;
			}

			Map<String, Object> row = new HashMap<String, Object>();
			row.put("project", project);
			row.put("member", projectMember);
			row.put("roleCode", projectMember.getRoleCode());
			row.put("roleName", PROJECT_ROLE_MAP.get(projectMember.getRoleCode()));
			row.put("members", buildProjectMembers(project.getId()));
			row.put("stages", buildStageRows(project.getId()));
			rows.add(row);
		}
		return rows;
	}

	/**
	 * 组装项目下全部项目角色人员
	 */
	private List<Map<String, Object>> buildProjectMembers(Long projectId) {
		List<ProjectMember> projectMembers = projectMemberMapper.selectList(new LambdaQueryWrapper<ProjectMember>()
				.eq(ProjectMember::getProjectId, projectId)
				.eq(ProjectMember::getStatus, 1)
				.orderByAsc(ProjectMember::getId));
		List<Map<String, Object>> members = new ArrayList<>();
		for (ProjectMember projectMember : projectMembers) {
			SysAccount account = userMapper.selectById(projectMember.getUserId());
			if (account == null) {
				continue;
			}

			Map<String, Object> member = new HashMap<String, Object>();
			member.put("id", projectMember.getId());
			member.put("projectMemberId", projectMember.getId());
			member.put("projectId", projectMember.getProjectId());
			member.put("userId", projectMember.getUserId());
			member.put("name", account.getName());
			member.put("avatar", account.getAvatar());
			member.put("phone", account.getPhone());
			member.put("roleCode", projectMember.getRoleCode());
			member.put("role", PROJECT_ROLE_MAP.get(projectMember.getRoleCode()));
			members.add(member);
		}
		return members;
	}

	/**
	 * 组装项目阶段、前置依赖和验收项
	 */
	private List<Map<String, Object>> buildStageRows(Long projectId) {
		List<ProjectStage> stages = projectStageMapper.selectList(new LambdaQueryWrapper<ProjectStage>()
				.eq(ProjectStage::getProjectId, projectId)
				.orderByAsc(ProjectStage::getId));
		List<ProjectStageDependency> dependencies = projectStageDependencyMapper.selectList(
				new LambdaQueryWrapper<ProjectStageDependency>()
						.eq(ProjectStageDependency::getProjectId, projectId));
		List<Map<String, Object>> stageRows = new ArrayList<>();
		for (ProjectStage stage : stages) {
			Map<String, Object> stageRow = new HashMap<String, Object>();
			stageRow.put("stage", stage);
			List<Long> predecessorStageIds = new ArrayList<>();
			for (ProjectStageDependency dependency : dependencies) {
				if (stage.getId().equals(dependency.getStageId())) {
					predecessorStageIds.add(dependency.getPredecessorStageId());
				}
			}
			stageRow.put("predecessorStageIds", predecessorStageIds);
			stageRow.put("inspectionItems", stageInspectionItemMapper.selectList(
					new LambdaQueryWrapper<StageInspectionItem>()
							.eq(StageInspectionItem::getStageId, stage.getId())
							.orderByAsc(StageInspectionItem::getId)));
			stageRows.add(stageRow);
		}
		return stageRows;
	}
}
