package com.seagox.lowcode.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("sms.tencent")
public class SmsConfig {

	private String secretId;

	private String secretKey;

	private String endpoint = "sms.tencentcloudapi.com";

	private String region = "ap-guangzhou";

	private String sdkAppId;

	private String signName;

	private String templateId;

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSdkAppId() {
		return sdkAppId;
	}

	public void setSdkAppId(String sdkAppId) {
		this.sdkAppId = sdkAppId;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
