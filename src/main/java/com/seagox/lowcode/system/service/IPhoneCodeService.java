package com.seagox.lowcode.system.service;

import com.seagox.lowcode.common.ResultData;

/**
 * 短信服务接口
 */
public interface IPhoneCodeService {
	/**
     * 发送验证码
     */
	public ResultData sendTextCode(String phone);
}
