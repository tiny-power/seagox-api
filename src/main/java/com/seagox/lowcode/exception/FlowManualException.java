package com.seagox.lowcode.exception;

/**
 * 流程手动选择异常
 */
public class FlowManualException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public FlowManualException(String message) {
        super(message);
    }
}
