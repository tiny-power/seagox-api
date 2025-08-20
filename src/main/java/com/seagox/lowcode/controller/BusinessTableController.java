package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessTable;
import com.seagox.lowcode.service.IBusinessTableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 业务表
 */
@RestController
@RequestMapping("/businessTable")
public class BusinessTableController {

	@Autowired
	private IBusinessTableService businessTableService;

	/**
	 * 查询全部
	 */
	@GetMapping("/queryAll")
	public ResultData queryAll(Long companyId, String name, String remark) {
		return businessTableService.queryAll(companyId, name, remark);
	}

	/**
	 * 新增
	 */
	@PostMapping("/insert")
	@LogPoint("新增业务表")
	public ResultData insert(@Valid BusinessTable businessTable) {
		return businessTableService.insert(businessTable);
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@LogPoint("修改业务表")
	public ResultData update(@Valid BusinessTable businessTable) {
		return businessTableService.update(businessTable);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete/{id}")
	@LogPoint("删除业务表")
	public ResultData delete(@PathVariable Long id) {
		return businessTableService.delete(id);
	}

	/**
	 * 查询通过id
	 */
	@GetMapping("/queryById/{id}")
	public ResultData queryById(@PathVariable Long id) {
		return businessTableService.queryById(id);
	}

	/**
	 * 根据级联数据
	 */
	@GetMapping("/queryCascader/{id}")
	public ResultData queryCascader(@PathVariable("id") Long id, String rule) {
		return businessTableService.queryCascader(id, rule);
	}

}
