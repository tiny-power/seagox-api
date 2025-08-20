package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.FormAthority;

public interface IFormAthorityService {

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param type     类型(1:提交状态;2:查看状态;)
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long formId, int type);

    /**
     * 添加
     */
    public ResultData insert(FormAthority formAthority);

    /**
     * 修改
     */
    public ResultData update(FormAthority formAthority);

    /**
     * 删除
     */
    public ResultData delete(Long id);

}
