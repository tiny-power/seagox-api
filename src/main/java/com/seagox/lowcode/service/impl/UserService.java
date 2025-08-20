package com.seagox.lowcode.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysAccount;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.entity.DeptUser;
import com.seagox.lowcode.entity.UserRole;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DepartmentMapper;
import com.seagox.lowcode.mapper.DeptUserMapper;
import com.seagox.lowcode.mapper.RoleMapper;
import com.seagox.lowcode.mapper.AccountMapper;
import com.seagox.lowcode.mapper.UserRoleMapper;
import com.seagox.lowcode.service.IUserService;
import com.seagox.lowcode.template.AccountModel;
import com.seagox.lowcode.util.EncryptUtils;
import com.seagox.lowcode.util.ExportUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	private AccountMapper userMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private DeptUserMapper deptUserMapper;

	/**
	 * 分页查询
	 */
	@Override
	public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, Long searchCompanyId,
			Long departmentId, String name) {
//		String prefix = "";
//		Company company = companyMapper.selectById(companyId);
//		prefix = company.getCode().substring(0, 4);
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> list = userMapper.queryByPage(null, searchCompanyId, departmentId, name);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return ResultData.success(pageInfo);
	}

	@Transactional
	@Override
	public ResultData insert(SysAccount user) {
		LambdaQueryWrapper<SysAccount> qw = new LambdaQueryWrapper<>();
		qw.eq(SysAccount::getAccount, user.getAccount());
		Long count = userMapper.selectCount(qw);
		if (count != 0) {
			return ResultData.warn(ResultCode.OTHER_ERROR, "账号已存在");
		}
		user.setPassword(EncryptUtils.hashpw(user.getPassword()));
		userMapper.insert(user);
		
		DeptUser deptUser = new DeptUser();
		deptUser.setCompanyId(user.getCompanyId());
		deptUser.setDepartmentId(user.getDepartmentId());
		deptUser.setUserId(user.getId());
		deptUserMapper.insert(deptUser);
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData update(SysAccount user) {
		SysAccount originalUser = userMapper.selectById(user.getId());
		if (originalUser.getAccount().equals(user.getAccount())) {
			userMapper.updateById(user);
		} else {
			LambdaQueryWrapper<SysAccount> qw = new LambdaQueryWrapper<>();
			qw.eq(SysAccount::getAccount, user.getAccount());
			Long count = userMapper.selectCount(qw);
			if (count == 0) {
				userMapper.updateById(user);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "账号已存在");
			}
		}
		
		LambdaQueryWrapper<DeptUser> deptUserQq = new LambdaQueryWrapper<>();
		deptUserQq.eq(DeptUser::getUserId, user.getId());
		DeptUser deptUser = deptUserMapper.selectOne(deptUserQq);
		deptUser.setDepartmentId(user.getDepartmentId());
		deptUserMapper.updateById(deptUser);
		
		return ResultData.success(null);
	}

	@Transactional
	@Override
	public ResultData delete(Long id) {
		userMapper.deleteById(id);
		LambdaQueryWrapper<UserRole> userRoleDw = new LambdaQueryWrapper<>();
		userRoleDw.eq(UserRole::getUserId, id);
		userRoleMapper.delete(userRoleDw);
		
		LambdaQueryWrapper<DeptUser> deptUserDw = new LambdaQueryWrapper<>();
		deptUserDw.eq(DeptUser::getUserId, id);
		deptUserMapper.delete(deptUserDw);
		
		return ResultData.success(null);
	}

	@Override
	public ResultData updatePassword(Long userId, String oldPassword, String newPassword) {
		SysAccount user = userMapper.selectById(userId);
		if (!EncryptUtils.checkpw(oldPassword, user.getPassword())) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "旧密码输入有误");
		} else if (EncryptUtils.checkpw(newPassword, user.getPassword())) {
			return ResultData.warn(ResultCode.PARAMETER_ERROR, "新密码不可以跟旧密码一样");
		} else {
			user.setPassword(EncryptUtils.hashpw(newPassword));
			user.setUpdateTime(new Date());
			userMapper.updateById(user);
			return ResultData.success(null);
		}
	}

	@Override
	public ResultData resetPassword(Long id, String password) {
		SysAccount user = new SysAccount();
		user.setId(id);
		user.setPassword(EncryptUtils.hashpw(password));
		user.setUpdateTime(new Date());
		userMapper.updateById(user);
		return ResultData.success(null);
	}

	@Override
	public ResultData queryByDeptId(Long deptId) {
		return ResultData.success(userMapper.queryByDeptId(deptId));
	}

	@Override
	public ResultData queryByIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return ResultData.success(null);
		} else {
			return ResultData.success(userMapper.queryByIds(ids.split(",")));
		}
	}

	@Override
	public List<Map<String, Object>> queryList(Long companyId, Long userId, String userName) {
		return userMapper.queryList(companyId, userId, userName);
	}

	@Override
	public List<Map<String, Object>> queryAll(Long companyId) {
        //Company company = companyMapper.selectById(companyId);
		return userMapper.queryByCompanyId(companyId);
	}

	@Override
	public List<Map<String, Object>> queryUserByCompanyId(Long companyId) {
		return userMapper.queryByCompanyId(companyId);
	}

	@Transactional
	@Override
	public void importHandle(List<AccountModel> resultList) {
		String password = "Test_1234";
		for (AccountModel sysAccountModel : resultList) {
			SysAccount sysAccount = new SysAccount();
			sysAccount.setAccount(sysAccountModel.getAccount());
			sysAccount.setEmail(sysAccountModel.getEmail());
			sysAccount.setPhone(sysAccountModel.getPhone());
			sysAccount.setName(sysAccountModel.getName());
			sysAccount.setSex(sysAccountModel.getSex());
			sysAccount.setPosition(sysAccountModel.getPosition());
			sysAccount.setPassword(EncryptUtils.hashpw(password));
			sysAccount.setSort(sysAccountModel.getSort());
			userMapper.insert(sysAccount);
			LambdaQueryWrapper<Company> qwCompany = new LambdaQueryWrapper<>();
			qwCompany.eq(Company::getName, sysAccountModel.getCompanyName());
			Company company = companyMapper.selectOne(qwCompany);
			LambdaQueryWrapper<Department> qwDept = new LambdaQueryWrapper<>();
			qwDept.eq(Department::getCompanyId, company.getId()).eq(Department::getName,
					sysAccountModel.getDeptName());
			Department department = departmentMapper.selectOne(qwDept);
			DeptUser deptUser = new DeptUser();
			deptUser.setCompanyId(company.getId());
			deptUser.setDepartmentId(department.getId());
			deptUser.setUserId(sysAccount.getId());
			deptUserMapper.insert(deptUser);
			if(!StringUtils.isEmpty(sysAccountModel.getRoleStr())) {
				List<String> roleIds = roleMapper.queryRoleIds(company.getId(), sysAccountModel.getRoleStr().split(","));
				UserRole userRole = new UserRole();
				userRole.setUserId(sysAccount.getId());
				userRole.setCompanyId(company.getId());
				for(int j = 0; j < roleIds.size(); j++) {
					userRole.setRoleId(Long.valueOf(roleIds.get(j)));
					userRoleMapper.insert(userRole);
				}
			}
		}
	}

	@Override
	public ResultData updateAvatar(Long userId, String avatar) {
		SysAccount sysAccount = new SysAccount();
		sysAccount.setId(userId);
		sysAccount.setAvatar(avatar);
		userMapper.updateById(sysAccount);
		return ResultData.success(null);
	}

	@Override
	public ResultData queryById(Long id) {
		Map<String, Object> result = new HashMap<>();
		
		SysAccount sysAccount = userMapper.selectById(id);
		result.put("id", sysAccount.getId());
		result.put("account", sysAccount.getAccount());
		result.put("name", sysAccount.getName());
		result.put("sex", sysAccount.getSex());
		result.put("position", sysAccount.getPosition());
		result.put("sort", sysAccount.getSort());
		result.put("phone", sysAccount.getPhone());
		result.put("email", sysAccount.getEmail());
		LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
		deptUserQw.eq(DeptUser::getUserId, id);
		List<DeptUser> deptUserList = deptUserMapper.selectList(deptUserQw);
		result.put("companyId", deptUserList.get(0).getCompanyId());
		result.put("departmentId", deptUserList.get(0).getDepartmentId());
		return ResultData.success(result);
	}

	@Override
	public void export(HttpServletRequest request, HttpServletResponse response ,Long exportCompanyId, Long exportDepartmentId) {
		List<Map<String, Object>> list = userMapper.exportByCompanyId(exportCompanyId, exportDepartmentId);
		Map<String, Object> resultData = new HashMap<>();
		resultData.put("list", list);
		ExportUtils.exportExcelTemplate("userExport.xlsx", "用户列表", resultData, request, response);
	}

    @Override
    public ResultData queryUserIdByRoleId(Long roleId) {
        return ResultData.success(userRoleMapper.queryUserIdByRoleId(roleId));
    }
    
}
