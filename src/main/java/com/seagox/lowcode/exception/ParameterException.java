package com.seagox.lowcode.exception;

/**
 * 参数异常
 */
public class ParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public ParameterException(String message) {
        super(message);
    }
}
