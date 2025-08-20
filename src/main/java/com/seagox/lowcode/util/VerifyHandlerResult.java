package com.seagox.lowcode.util;

import java.util.List;

public class VerifyHandlerResult {
	
	/**
     * 是否正确
     */
    private boolean success;
    /**
     * 错误信息
     */
    private List<String>  msg;

    public VerifyHandlerResult() {

    }

    public VerifyHandlerResult(boolean success) {
        this.success = success;
    }

    public VerifyHandlerResult(boolean success, List<String> msg) {
        this.success = success;
        this.msg = msg;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getMsg() {
		return msg;
	}

	public void setMsg(List<String> msg) {
		this.msg = msg;
	}
    
}
