package com.seagox.lowcode.service;

import java.util.List;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.template.DepartmentModel;

public interface IDepartmentService {

	/**
	 * 查询全部通过公司id
	 */
	public ResultData queryByCompanyId(Long companyId, String searchCompanyId);

	/**
	 * 新增
	 */
	public ResultData insert(Department department);

	/**
	 * 更新
	 */
	public ResultData update(Department department);

	/**
	 * 删除
	 */
	public ResultData delete(Long id);

	/**
	 * 导入处理
	 */
	public ResultData importHandle(List<DepartmentModel> list);
}
