package com.seagox.lowcode.common;

import java.io.Serializable;

/**
 * 响应数据信息
 */
public class ResultData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private Object data;

    public ResultData() {

    }

    public ResultData(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public ResultData(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultData success(Object data) {
        return new ResultData(ResultCode.SUCCESS, data);
    }

    public static ResultData warn(ResultCode resultCode) {
        return new ResultData(resultCode);
    }

    public static ResultData warn(ResultCode resultCode, String message) {
        ResultData result = new ResultData(resultCode);
        result.setMessage(message);
        return result;
    }
    
    public static ResultData warn(ResultCode resultCode, String message, Object data) {
        ResultData result = new ResultData(resultCode);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static ResultData error(ResultCode resultCode) {
        return new ResultData(resultCode);
    }
}
