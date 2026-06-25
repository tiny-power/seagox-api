package com.seagox.lowcode.system.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.system.entity.Disk;

/**
 * 网盘
 */
public interface IDiskService {

    /**
     * 查询当前目录内容
     */
    public ResultData queryChildren(Long companyId, Long parentId, String keyword);

    /**
     * 新增文件夹
     */
    public ResultData insertFolder(Long companyId, Long userId, Disk disk);

    /**
     * 新增文件
     */
    public ResultData insertFile(Long companyId, Long userId, Disk disk);

    /**
     * 更新
     */
    public ResultData update(Long companyId, Long userId, Disk disk);

    /**
     * 删除
     */
    public ResultData delete(Long companyId, Long id);

}
