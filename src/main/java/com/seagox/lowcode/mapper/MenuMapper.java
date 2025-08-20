package com.seagox.lowcode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seagox.lowcode.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜单
 */
public interface MenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询全部通过公司id
     *
     * @param companyId 公司id
     */
    public List<Map<String, Object>> queryByCompanyId(@Param("companyId") Long companyId, @Param("classify") int classify, @Param("status") int status);

    /**
     * 查询用户权限
     */
    public List<Map<String, Object>> queryUserMenu(@Param("ids")String[] ids, @Param("classify")Integer classify);

    /**
     * 查询用户权限
     */
    public List<String> queryUserMenuStr(String[] array);

    /**
     * 查询用户快捷入口权限
     */
    public List<Map<String, Object>> queryQuickAccess(String[] array);

}
