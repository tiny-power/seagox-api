package com.seagox.lowcode.service;

import org.springframework.web.multipart.MultipartFile;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicClassify;

public interface IDicClassifyService {

    /**
     * 查询显示
     */
    public ResultData queryDisplay(Long companyId);

    /**
     * 新增
     */
    public ResultData insert(DicClassify dicClassify);

    /**
     * 更新
     */
    public ResultData update(DicClassify dicClassify);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 查询通过id
     */
    public ResultData queryById(Long id);
    
    /**
     * 查询通过名称
     */
    public ResultData queryByName(Long companyId, String name);
    
    /**
     * 导入
     */
    public ResultData importHandle(MultipartFile file, Long companyId);

}
