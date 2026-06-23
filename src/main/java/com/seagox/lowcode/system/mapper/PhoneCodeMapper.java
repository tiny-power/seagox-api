package com.seagox.lowcode.system.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.system.entity.PhoneCode;

/**
 * 手机验证码
 */
public interface PhoneCodeMapper extends BaseMapper<PhoneCode> {
	/**
     * 通过手机号查找最近一条手机验证码信息
     */
	public PhoneCode queryLastPhone(@Param("phone")String phone);
}
