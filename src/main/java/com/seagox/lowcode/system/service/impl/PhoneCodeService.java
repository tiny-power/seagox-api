package com.seagox.lowcode.system.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.service.IPhoneCodeService;
import com.seagox.lowcode.system.entity.*;
import com.seagox.lowcode.system.mapper.*;
import com.seagox.lowcode.util.SmsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhoneCodeService implements IPhoneCodeService {
	@Autowired
	private PhoneCodeMapper phoneCodeMapper;
	
	@Override
	public ResultData sendTextCode(String phone) {
		String code = String.valueOf((int)((Math.random()*9+1)*1000));
		SendSmsResponse sendSmsResponse = SmsUtils.sendSms(phone, code);
		if (sendSmsResponse  == null) {
			return ResultData.error(ResultCode.INTERNAL_SERVER_ERROR);
		} else {
			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				PhoneCode phoneCode = new PhoneCode();
				phoneCode.setCode(code);
				phoneCode.setPhone(phone);
				Date now = new Date();
				phoneCode.setExpireTime(new Date(now .getTime() + 1000 * 60 * 5));
				phoneCodeMapper.insert(phoneCode);
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "请求太频繁，请稍后再试!");
			}
		}
	}
}