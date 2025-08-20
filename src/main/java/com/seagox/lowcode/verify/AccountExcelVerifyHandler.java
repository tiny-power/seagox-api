package com.seagox.lowcode.verify;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.SysAccount;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DepartmentMapper;
import com.seagox.lowcode.mapper.RoleMapper;
import com.seagox.lowcode.mapper.AccountMapper;
import com.seagox.lowcode.template.AccountModel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 自定义验证
 */
@Component
public class AccountExcelVerifyHandler implements IExcelVerifyHandler<AccountModel> {

	@Autowired
	private AccountMapper sysAccountMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	/**
	 * ExcelVerifyHandlerResult suceess :代表验证成功还是失败 msg:失败的原因
	 */
	@Override
	public ExcelVerifyHandlerResult verifyHandler(AccountModel obj) {
		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

		LambdaQueryWrapper<SysAccount> qwAccount = new LambdaQueryWrapper<>();
		qwAccount.eq(SysAccount::getAccount, obj.getAccount());
		Long accountCount = sysAccountMapper.selectCount(qwAccount);
		if (accountCount != 0) {
			result.setSuccess(false);
			result.setMsg("账号已存在");
		}
		LambdaQueryWrapper<Company> qwCompany = new LambdaQueryWrapper<>();
		qwCompany.eq(Company::getName, obj.getCompanyName());
		Company company = companyMapper.selectOne(qwCompany);
		if (company == null) {
			result.setSuccess(false);
			result.setMsg("所在单位不存在");
		} else {
			LambdaQueryWrapper<Department> qwDept = new LambdaQueryWrapper<>();
			qwDept.eq(Department::getCompanyId, company.getId()).eq(Department::getName, obj.getDeptName());
			Long deptCount = departmentMapper.selectCount(qwDept);
			if (deptCount == 0) {
				result.setSuccess(false);
				result.setMsg("部门名称不存在");
			}
			if(!StringUtils.isEmpty(obj.getRoleStr())) {
				String[] roleStr = obj.getRoleStr().split(",");
				List<String> roleIds = roleMapper.queryRoleIds(company.getId(), roleStr);
				if(roleIds.size() != roleStr.length) {
					result.setSuccess(false);
					result.setMsg("角色名称不存在或者错误");
				}
			}
		}
		
		return result;
	}

}
