package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Serial;

public interface ISerialService {

    /**
     * 分页查询
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId);
    
    /**
     * 查询全部
     */
    public ResultData queryAll(Long companyId);
    
    /**
     * 添加
     */
    public ResultData insert(Serial serial);

    /**
     * 修改
     */
    public ResultData update(Serial serial);

    /**
     * 删除
     */
    public ResultData delete(Long id);
    
}
