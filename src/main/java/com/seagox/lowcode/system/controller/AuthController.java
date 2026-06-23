package com.seagox.lowcode.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.mapper.DicDetailMapper;
import com.seagox.lowcode.system.service.IAuthService;
import com.seagox.lowcode.system.service.IPhoneCodeService;
import com.seagox.lowcode.system.service.IUploadService;
import com.seagox.lowcode.util.ValidatorUtils;
import com.seagox.lowcode.util.WeiChatUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 认证
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private IAuthService authService;

	@Autowired
	private WeiChatUtils weiChatUtils;

	@Autowired
    private IUploadService uploadService;
	
	@Autowired
    private DicDetailMapper dicDetailMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IPhoneCodeService phoneCodeService;
	
	
	/**
	 * 登陆
	 *
	 * @param account  用户名
	 * @param password 密码
	 * @param openid   openid
	 * @param avatar   头像地址
	 */
	@PostMapping("/login")
	@LogPoint("登陆")
	public ResultData login(String account, String password, String openid, String avatar) {
		return authService.login(account, password, openid, avatar);
	}

	/**
	 * 控制台
	 *
	 * @param account  用户名
	 * @param password 密码
	 */
	@PostMapping("/console")
	@LogPoint("控制台")
	public ResultData loginConsole(String account, String password) {
		return authService.loginConsole(account, password);
	}

	/**
	 * 验证登陆
	 *
	 * @param org          组织
	 * @param account      用户名
	 * @param noncestr     随机数
	 * @param timestampStr 时间戳
	 * @param sign         签名
	 */
	@PostMapping("/verifyLogin")
	public ResultData verifyLogin(String org, String account, String noncestr, String timestampStr, String sign) {
		return authService.verifyLogin(org, account, noncestr, timestampStr, sign);
	}

	/**
	 * 小程序登录
	 *
	 * @param code 编码
	 */
	@GetMapping("/loginByCode/{code}")
	public ResultData loginByCode(@PathVariable String code) {
		JSONObject jsonObject = weiChatUtils.getAppletsLoginCertificate(code);
		if (jsonObject != null) {
			if (jsonObject.containsKey("openid")) {
				return authService.verifyByOpenid(jsonObject.getString("openid"));
			} else {
				return ResultData.warn(ResultCode.INVALID_CODE);
			}
		} else {
			return ResultData.warn(ResultCode.INVALID_CODE);
		}
	}

	/**
	 * 验证小程序登录
	 *
	 * @param openid openid
	 */
	@GetMapping("/loginByOpenid/{openid}")
	public ResultData loginByOpenid(@PathVariable String openid) {
		return authService.loginByOpenid(openid);
	}

	/**
     * 在线预览
     */
    @GetMapping("/preview")
    public void preview(String url, String fileName, HttpServletResponse response) {
        uploadService.preview(url, fileName, response);
    }
    
    /**
     * 发送手机验证码
     */
    @GetMapping("/sendTextCode/{phone}")
    public ResultData sendTextCode(@PathVariable String phone) {
    	if (!ValidatorUtils.isMobile(phone)) {
    		return ResultData.warn(ResultCode.PARAMETER_ERROR, "手机号格式不对");
    	} else {
    		return phoneCodeService.sendTextCode(phone);
    	}
    }

}
