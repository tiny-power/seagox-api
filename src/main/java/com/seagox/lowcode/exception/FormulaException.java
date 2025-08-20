package com.seagox.lowcode.exception;

/**
 * 公式异常
 */
public class FormulaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public FormulaException(String message) {
        super(message);
    }
}
