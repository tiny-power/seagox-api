package com.seagox.lowcode.security;

import com.alibaba.fastjson.JSONArray;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultData defaultErrorHandler(Exception e) {
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            return ResultData.error(ResultCode.NOT_FOUND);
        } else if (e instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            return ResultData.error(ResultCode.NOT_SUPPORTED);
        } else if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
            return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
        } else if (e instanceof org.springframework.jdbc.BadSqlGrammarException) {
            return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
        } else if (e instanceof com.seagox.lowcode.exception.FormulaException) {
            return ResultData.warn(ResultCode.FORMULA_ERROR, e.getMessage());
        } else if (e instanceof java.lang.IllegalArgumentException) {
            return ResultData.warn(ResultCode.OTHER_ERROR, e.getMessage());
        } else if (e instanceof com.seagox.lowcode.exception.GrammarException) {
            return ResultData.warn(ResultCode.GRAMMAR_ERROR, e.getMessage());
        } else if (e instanceof org.springframework.validation.BindException) {
            BindingResult bindingResult = ((org.springframework.validation.BindException) e).getBindingResult();
            return ResultData.warn(ResultCode.PARAMETER_ERROR, bindingResult.getFieldError().getDefaultMessage());
        } else if (e instanceof com.seagox.lowcode.exception.FlowManualException) {
            return ResultData.warn(ResultCode.FLOW_MANUAL_ERROR, "请手动选择", JSONArray.parse(e.getMessage()));
        } else if (e instanceof com.seagox.lowcode.exception.ConfirmException) {
            return ResultData.warn(ResultCode.CONFIRM, e.getMessage());
        } else {
            return ResultData.error(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }
}
