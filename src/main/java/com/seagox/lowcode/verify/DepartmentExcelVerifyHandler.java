package com.seagox.lowcode.verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DepartmentMapper;
import com.seagox.lowcode.template.DepartmentModel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 自定义验证
 */
@Component
public class DepartmentExcelVerifyHandler implements IExcelVerifyHandler<DepartmentModel> {

	@Autowired
	private DepartmentMapper departmentMapper;

	@Autowired
	private CompanyMapper companyMapper;

	/**
	 * ExcelVerifyHandlerResult suceess :代表验证成功还是失败 msg:失败的原因
	 */
	@Override
	public ExcelVerifyHandlerResult verifyHandler(DepartmentModel obj) {
		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

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
			if (deptCount > 0) {
				result.setSuccess(false);
				result.setMsg("部门名称已存在");
			}
		}
		return result;
	}

}
