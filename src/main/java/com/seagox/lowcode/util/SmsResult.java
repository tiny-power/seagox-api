package com.seagox.lowcode.util;

public class SmsResult {

	private final boolean success;
	private final String code;
	private final String message;

	private SmsResult(boolean success, String code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public static SmsResult success(String code, String message) {
		return new SmsResult(true, code, message);
	}

	public static SmsResult fail(String code, String message) {
		return new SmsResult(false, code, message);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
