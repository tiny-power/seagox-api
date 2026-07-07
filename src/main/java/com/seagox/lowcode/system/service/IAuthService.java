package com.seagox.lowcode.system.service;

import com.seagox.lowcode.common.ResultData;

/**
 * 权限服务接口
 */
public interface IAuthService {

	/**
	 * 登陆
	 *
	 * @param name     用户名
	 * @param password 密码
	 * @param openid   openid
	 * @param avatar   头像
	 */
	public ResultData login(String account, String password, String openid, String avatar);
	
	/**
	 * 登陆(控制台)
	 *
	 * @param name     用户名
	 * @param password 密码
	 */
	public ResultData loginConsole(String account, String password);

	/**
	 * 登录（小程序）
	 *
	 * @param phone      手机号或账号
	 * @param credential 密码或验证码
	 * @param loginMode  登录方式
	 * @param openid     小程序openid
	 * @param avatar     头像
	 * @param unionid    开放平台unionid
	 */
	public ResultData miniLogin(String phone, String credential, String loginMode, String openid, String avatar, String unionid);

	/**
	 * 校验手机号是否可发送小程序验证码
	 *
	 * @param phone 手机号
	 */
	public ResultData validateMiniTextCodePhone(String phone);

	/**
	 * 验证登陆
	 *
	 * @param org       组织
	 * @param account   用户名
	 * @param noncestr  随机数
	 * @param timestamp 时间戳
	 * @param sign      签名
	 */
	public ResultData verifyLogin(String org, String account, String noncestr, String timestamp, String sign);

	/**
	 * 登录（小程序）
	 *
	 * @param openid openid
	 */
	public ResultData verifyByOpenid(String openid);

	/**
	 * 登录（小程序）
	 *
	 * @param openid openid
	 */
	public ResultData loginByOpenid(String openid);

}
