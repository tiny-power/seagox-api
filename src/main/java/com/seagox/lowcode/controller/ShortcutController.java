package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Shortcut;
import com.seagox.lowcode.service.IShortcutService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 快捷入口
 */
@RestController
@RequestMapping("/shortcut")
public class ShortcutController {

	@Autowired
	private IShortcutService shortcutService;
	
	@GetMapping("/queryListByUserId")
	public ResultData queryListByUserId(Long companyId, Long userId) {
		return shortcutService.queryListByUserId(companyId, userId);
	}
	
	/**
	 * 收藏
	 */
	@PostMapping("/insert")
	public ResultData insert(Shortcut shortcut) {
		return shortcutService.insert(shortcut);
	}
	
	/**
	 * 取消
	 */
	@GetMapping("/cancel/{menuId}")
	public ResultData cancel(@PathVariable Long menuId, Long companyId, Long userId) {
		return shortcutService.cancel(companyId, userId, menuId);
	}

}
