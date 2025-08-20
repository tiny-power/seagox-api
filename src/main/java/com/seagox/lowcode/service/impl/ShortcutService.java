package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Shortcut;
import com.seagox.lowcode.mapper.ShortcutMapper;
import com.seagox.lowcode.service.IShortcutService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortcutService implements IShortcutService {

    @Autowired
    private ShortcutMapper shortcutMapper;
    
    @Override
	public ResultData queryListByUserId(Long companyId, Long userId) {
		return ResultData.success(shortcutMapper.queryListByUserId(companyId, userId));
	}
	
	@Transactional
	@Override
	public ResultData insert(Shortcut shortcut) {
		LambdaQueryWrapper<Shortcut> qw = new LambdaQueryWrapper<>();
		qw.eq(Shortcut::getCompanyId, shortcut.getCompanyId())
		.eq(Shortcut::getUserId, shortcut.getUserId())
		.eq(Shortcut::getMenuId, shortcut.getMenuId());
		shortcutMapper.delete(qw);
		shortcutMapper.insert(shortcut);
		return ResultData.success(null);
	}

	@Override
	public ResultData cancel(Long companyId, Long userId, Long menuId) {
		LambdaQueryWrapper<Shortcut> qw = new LambdaQueryWrapper<>();
		qw.eq(Shortcut::getCompanyId, companyId)
		.eq(Shortcut::getUserId, userId)
		.eq(Shortcut::getMenuId, menuId);
		shortcutMapper.delete(qw);
		return ResultData.success(null);
	}

}
