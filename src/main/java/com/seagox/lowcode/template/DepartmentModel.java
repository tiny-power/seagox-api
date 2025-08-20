package com.seagox.lowcode.template;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DepartmentModel extends ImportModel {

	/**
	 * 单位名称
	 */
	@Excel(name = "单位名称")
	@NotNull(message = "不能为空或有误")
	private String companyName;

	/**
	 * 上级部门
	 */
	@Excel(name = "上级部门")
	private String pNmae;

	/**
	 * 部门名称
	 */
	@Excel(name = "部门名称")
	@NotNull(message = "不能为空或有误")
	private String deptName;

	/**
	 * 编码
	 */
	@Excel(name = "编码")
	private String code;

	/**
	 * 排序
	 */
	@Excel(name = "排序")
	private Integer sort;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getpNmae() {
		return pNmae;
	}

	public void setpNmae(String pNmae) {
		this.pNmae = pNmae;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
