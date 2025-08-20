package com.seagox.lowcode.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.seagox.lowcode.util.ValidatorUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AccountModel extends ImportModel {

	/**
	 * 单位名称
	 */
	@Excel(name = "单位名称")
	@NotNull(message = "不能为空或有误")
	private String companyName;

	/**
	 * 排序
	 */
	@Excel(name = "排序")
	@NotNull(message = "不能为空或有误")
	private Integer sort;

	/**
	 * 部门名称
	 */
	@Excel(name = "部门名称")
	@NotNull(message = "不能为空或有误")
	private String deptName;

	/**
	 * 帐号
	 */
	@Excel(name = "帐号")
	@NotNull(message = "不能为空或有误")
	@Size(min = 2, message = "长度至少2位")
	private String account;

	/**
	 * 姓名
	 */
	@Excel(name = "姓名")
	@NotNull(message = "不能为空或有误")
	private String name;
	
	/**
	 * 角色
	 */
	@Excel(name = "角色名称(多个用,隔开)")
	private String roleStr;

	/**
	 * 性别(1:男;2:女;)
	 */
	@Excel(name = "性别", replace = { "男_1", "女_2" })
	@NotNull(message = "不能为空或有误")
	private Integer sex;

	/**
	 * 手机号
	 */
	@Excel(name = "手机号")
	@Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "不规范")
	private String phone;

	/**
	 * 邮箱
	 */
	@Excel(name = "邮箱")
	@Pattern(regexp = ValidatorUtils.REGEX_EMAIL, message = "不规范")
	private String email;

	/**
	 * 职位
	 */
	@Excel(name = "职位")
	private String position;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoleStr() {
		return roleStr;
	}

	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
