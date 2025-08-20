package com.seagox.lowcode.common;

public enum ResultCode {

    SUCCESS(200, "OK"),
    UNAUTHORIZED(401, "Unauthorized"),
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    NOT_SUPPORTED(405, "Not Supported"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    SIGN_ERROR(10001, "Sign Error"),
    PARAMETER_ERROR(10002, "Parameter Error"),
    OTHER_ERROR(10003, "Other Error"),
    FLOW_MANUAL_ERROR(10004, "Flow ManualSelection Error"),
   
    FORMULA_ERROR(10005, "Formula Error"),
    GRAMMAR_ERROR(10006, "Grammar Error"),
    INVALID_CODE(10008, "invalid code"),
    CONFIRM(10011, "Confirm"),
    INVALID_ERROR(10012, "invalid error");

    private int code;

    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
