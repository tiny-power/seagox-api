package com.seagox.lowcode.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.DicDetail;

/**
 * 字典详情
 */
public interface DicDetailMapper extends BaseMapper<DicDetail> {
	
	public List<Map<String, Object>> queryMaps(Long classifyId);
	
}
