package com.seagox.lowcode.template;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

public class ImportModel implements IExcelModel, IExcelDataModel {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String errorMsg;

    public Integer getRowNum() {
        return rowNum + 1;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
