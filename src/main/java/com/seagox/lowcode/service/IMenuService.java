package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysMenu;

public interface IMenuService {

    /**
     * 查询全部通过公司id
     */
    public ResultData queryByCompanyId(Long companyId, int classify, int status);

    /**
     * 新增
     */
    public ResultData insert(SysMenu menu);

    /**
     * 更新
     */
    public ResultData update(SysMenu menu);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 查询用户菜单权限
     *
     * @param companyId 公司id
     * @param userId    用户id
     * @param classify 类别(1:PC端;2:移动端;)
     */
    public ResultData queryUserMenu(Long companyId, Long userId, Integer classify);

    /**
     * 查询用户快捷入口权限
     *
     * @param companyId 公司id
     * @param userId    用户id
     */
    public ResultData queryQuickAccess(Long companyId, Long userId);

}
