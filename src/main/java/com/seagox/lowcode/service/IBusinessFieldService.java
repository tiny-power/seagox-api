package com.seagox.lowcode.service;

import java.util.List;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.BusinessField;
import com.seagox.lowcode.template.FieldModel;

public interface IBusinessFieldService {

    /**
     * 查询全部通过名称
     */
    public ResultData queryByTableName(String tableName);

    /**
     * 查询全部通过表id
     */
    public ResultData queryByTableId(Long businessTableId, String name, String remark);

    /**
     * 添加
     */
    public ResultData insert(BusinessField businessField);

    /**
     * 修改
     */
    public ResultData update(BusinessField businessField);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 根据业务表id查询
     */
    public ResultData queryByTableId(Long tableId);
    
    /**
     * 导入处理
     */
    public ResultData importHandle(Long tableId, List<FieldModel> resultList);

    /**
     * 根据业务表ids查询表字段
     */
    public ResultData queryByTableIds(String tableIds);
    
    /**
     * 批量添加
     */
    public ResultData batch(String data);
    
}
