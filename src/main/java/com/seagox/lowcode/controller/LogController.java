package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日记
 */
@RestController
@RequestMapping("/log")
public class LogController {

	@Autowired
	private ILogService logService;

	/**
	 * 分页查询
	 *
	 * @param pageNo   起始页
	 * @param pageSize 每页大小
	 * @param account  操作人
	 * @param name     名称
	 * @param uri      地址
	 * @param status   状态
	 */
	@GetMapping("/queryByPage")
	public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String type, String account,
			String name, String uri, Integer status) {
		return logService.queryByPage(pageNo, pageSize, type, account, name, uri, status);
	}

}
