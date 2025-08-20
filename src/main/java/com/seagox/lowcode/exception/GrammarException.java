package com.seagox.lowcode.exception;

/**
 * 语法异常
 */
public class GrammarException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public GrammarException(String message) {
        super(message);
    }
}
