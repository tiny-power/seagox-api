package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaDefinition;

import org.springframework.web.bind.annotation.PathVariable;

public interface ISeaDefinitionService {

    /**
     * 添加
     */
    public ResultData insert(SeaDefinition seaDefinition);

    /**
     * 修改
     */
    public ResultData update(SeaDefinition seaDefinition);

    /**
     * 详情
     */
    public ResultData queryById(@PathVariable Long id, String dataSource);

    /**
     * 删除
     */
    public ResultData delete(Long id);
    
    /**
     * 通过表单id查询流程
     */
    public ResultData queryByFormId(Long formId);

}
