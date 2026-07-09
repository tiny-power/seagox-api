package com.seagox.lowcode.util;

import com.seagox.lowcode.common.SmsConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

public class SmsUtils {

	private static final String SUCCESS_CODE = "Ok";

	public static SmsResult sendSms(String phoneNumbers, String code, SmsConfig smsConfig) {
		try {
			Credential credential = new Credential(smsConfig.getSecretId(), smsConfig.getSecretKey());
			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint(smsConfig.getEndpoint());
			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);
			SmsClient client = new SmsClient(credential, smsConfig.getRegion(), clientProfile);

			SendSmsRequest request = new SendSmsRequest();
			request.setSmsSdkAppId(smsConfig.getSdkAppId());
			request.setSignName(smsConfig.getSignName());
			request.setTemplateId(smsConfig.getTemplateId());
			request.setTemplateParamSet(new String[] { code });
			request.setPhoneNumberSet(formatPhoneNumbers(phoneNumbers));

			SendSmsResponse response = client.SendSms(request);
			SendStatus[] sendStatusSet = response.getSendStatusSet();
			if (sendStatusSet != null && sendStatusSet.length > 0) {
				SendStatus sendStatus = sendStatusSet[0];
				if (SUCCESS_CODE.equalsIgnoreCase(sendStatus.getCode())) {
					return SmsResult.success(sendStatus.getCode(), sendStatus.getMessage());
				}
				return SmsResult.fail(sendStatus.getCode(), sendStatus.getMessage());
			}
			return SmsResult.fail("EMPTY_STATUS", "腾讯云短信未返回发送状态");
		} catch (Exception e) {
			return SmsResult.fail("EXCEPTION", e.getMessage());
		}
	}

	private static String[] formatPhoneNumbers(String phoneNumbers) {
		String[] phones = phoneNumbers.split(",");
		for (int i = 0; i < phones.length; i++) {
			String phone = phones[i].trim();
			if (phone.matches("^1\\d{10}$")) {
				phone = "+86" + phone;
			}
			phones[i] = phone;
		}
		return phones;
	}
		
}
