package com.seagox.lowcode.util;

import java.util.List;
import java.util.Map;

public class ImportResult {
	
	/**
     * 结果集
     */
    private List<Map<String, Object>>  list;
    /**
     * 失败数据
     */
    private List<String>  failList;

    /**
     * 是否存在校验失败
     */
    private boolean  verifyFail;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public List<String> getFailList() {
		return failList;
	}

	public void setFailList(List<String> failList) {
		this.failList = failList;
	}

	public boolean isVerifyFail() {
		return verifyFail;
	}

	public void setVerifyFail(boolean verifyFail) {
		this.verifyFail = verifyFail;
	}
    
}
