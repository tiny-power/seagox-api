package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.service.IBusinessFieldService;
import com.seagox.lowcode.template.FieldModel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * 业务字段
 */
@RestController
@RequestMapping("/businessField")
public class BusinessFieldController {

    @Autowired
    private IBusinessFieldService businessFieldService;

    /**
     * 查询全部通过名称
     */
    @GetMapping("/queryByTableName")
    public ResultData queryByTableName(String tableName) {
        return businessFieldService.queryByTableName(tableName);
    }

    /**
     * 查询全部通过表id
     */
    @GetMapping("/queryByTableId")
    public ResultData queryByTableId(
                                  Long businessTableId, String name, String remark) {
        return businessFieldService.queryByTableId(businessTableId, name, remark);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增业务字段")
    public ResultData insert(@Valid BusinessField businessField) {
        return businessFieldService.insert(businessField);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改业务字段")
    public ResultData update(@Valid BusinessField businessField) {
        return businessFieldService.update(businessField);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除业务字段")
    public ResultData delete(@PathVariable Long id) {
        return businessFieldService.delete(id);
    }

    /**
     * 查询全部
     */
    @GetMapping("/queryByTableId/{tableId}")
    public ResultData queryByTableId(@PathVariable("tableId") Long tableId) {
        return businessFieldService.queryByTableId(tableId);
    }
    
    /**
     * 导入
     */
    @PostMapping("/import")
    public ResultData importHandle(@RequestParam("file") MultipartFile file, Long tableId) {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
        params.setNeedVerify(true);
        try {
            ExcelImportResult<FieldModel> result = ExcelImportUtil.importExcelMore(
                    file.getInputStream(),
                    FieldModel.class,
                    params);
            Map<String, Object> repeatMap = new HashMap<>();
            List<FieldModel> resultList = result.getList();
            for (int i = 0; i < resultList.size(); i++) {
                if (repeatMap.containsKey(resultList.get(i).getName())) {
                    return ResultData.warn(ResultCode.OTHER_ERROR, "字段名重复,请检查:" + resultList.get(i).getName());
                } else {
                    repeatMap.put(resultList.get(i).getName(), 1);
                }
            }
            //判断是否有错误
            if (result.isVerifyFail()) {
                for (FieldModel entity : result.getFailList()) {
                    return ResultData.warn(ResultCode.OTHER_ERROR, "第" + entity.getRowNum() + "行的错误是：" + entity.getErrorMsg());
                }
            } else {
                //获到正确的数据
            	return businessFieldService.importHandle(tableId, resultList);
            }
            return ResultData.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.warn(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    /**
     * 查询全部通过表ids查询表字段
     */
    @GetMapping("/queryByTableIds")
    public ResultData queryByTableIds(String tableIds) {
        return businessFieldService.queryByTableIds(tableIds);
    }
    
    /**
     * 批量添加
     */
    @PostMapping("/batch")
    @LogPoint("批量添加")
    public ResultData batch(String data) {
        return businessFieldService.batch(data);
    }

}
