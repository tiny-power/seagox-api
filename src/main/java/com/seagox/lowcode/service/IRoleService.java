package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysRole;

public interface IRoleService {

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId);

    /**
     * 新增角色
     */
    public ResultData insert(SysRole role);

    /**
     * 更新角色
     */
    public ResultData update(SysRole role);

    /**
     * 删除角色
     */
    public ResultData delete(Long id);

    /**
     * 查询所有角色
     */
    public ResultData queryAll(Long companyId);

    /**
     * 授权
     */
    public ResultData authorize(Long companyId, String userIds, Long roleId);
}
