package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SeaDefinition;

import org.springframework.web.bind.annotation.PathVariable;

public interface ISeaDefinitionService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, String businessType, String name);

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
     * 通过业务类型查询流程
     */
    public ResultData queryByBusinessType(String businessType);

}
