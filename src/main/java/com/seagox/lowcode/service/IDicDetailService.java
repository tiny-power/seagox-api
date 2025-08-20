package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicDetail;

public interface IDicDetailService {

    /**
     * 查询显示
     */
    public ResultData queryDisplay(Long classifyId);

    /**
     * 查询显示（不封装树）
     */
    public ResultData queryDisplayNoTree(Long classifyId);

    /**
     * 新增
     */
    public ResultData insert(Long companyId, DicDetail dicDetail);

    /**
     * 更新
     */
    public ResultData update(Long companyId, DicDetail dicDetail);

    /**
     * 删除
     */
    public ResultData delete(Long companyId, Long id);

    /**
     * 字典分类详情
     *
     * @param classifyId 字典分类id
     */
    public ResultData queryByClassifyId(Long classifyId);
    
    /**
     * 批量添加
     */
    public ResultData batch(Long companyId, Long classifyId, String remark);
}
