package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.Shortcut;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 快捷入口
 */
public interface ShortcutMapper extends BaseMapper<Shortcut> {

	/**
	 * 查询全部通过用户id
	 *
	 * @param companyId    公司id
	 * @param userId 用户id
	 */
	public List<Map<String, Object>> queryListByUserId(@Param("companyId") Long compamyId,
			@Param("userId") Long userId);
}
