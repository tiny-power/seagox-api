package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Shortcut;

public interface IShortcutService {
	
	public ResultData queryListByUserId(Long companyId, Long userId);
	
	/**
	 * 收藏
	 */
	public ResultData insert(Shortcut shortcut);
	
	/**
	 * 取消
	 */
	public ResultData cancel(Long companyId, Long userId, Long menuId);

}
