package com.seagox.lowcode.system.service.impl;

import com.seagox.lowcode.common.SmsConfig;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.service.IPhoneCodeService;
import com.seagox.lowcode.system.entity.*;
import com.seagox.lowcode.system.mapper.*;
import com.seagox.lowcode.util.SmsResult;
import com.seagox.lowcode.util.SmsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PhoneCodeService implements IPhoneCodeService {
	@Autowired
	private PhoneCodeMapper phoneCodeMapper;

	@Autowired
	private SmsConfig smsConfig;
		
	@Override
	public ResultData sendTextCode(String phone) {
		String code = String.valueOf((int)((Math.random()*9+1)*1000));
		SmsResult smsResult = SmsUtils.sendSms(phone, code, smsConfig);
		if (smsResult  == null) {
			return ResultData.error(ResultCode.INTERNAL_SERVER_ERROR);
		} else {
			if(smsResult.isSuccess()) {
				PhoneCode phoneCode = new PhoneCode();
				phoneCode.setCode(code);
				phoneCode.setPhone(phone);
				Date now = new Date();
				phoneCode.setExpireAt(new Date(now .getTime() + 1000 * 60 * 5));
				phoneCodeMapper.insert(phoneCode);
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, smsResult.getMessage() == null ? "请求太频繁，请稍后再试!" : smsResult.getMessage());
			}
		}
	}
}
