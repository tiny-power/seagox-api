package com.seagox.lowcode.exception;

/**
 * 提示框异常
 */
public class ConfirmException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public ConfirmException(String message) {
        super(message);
    }
}
