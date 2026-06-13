package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysMenu;
import com.seagox.lowcode.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 菜单
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    /**
     * 查询全部通过公司id
     *
     * @param companyId 公司id
     */
    @GetMapping("/queryByCompanyId")
    public ResultData queryByPage(Long companyId, @RequestParam(value = "status", defaultValue = "0") Integer status) {
        return menuService.queryByCompanyId(companyId, status);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增菜单")
    public ResultData insert(@Valid SysMenu menu) {
        return menuService.insert(menu);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @LogPoint("更新菜单")
    public ResultData update(@Valid SysMenu menu) {
        return menuService.update(menu);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除菜单")
    public ResultData delete(@PathVariable Long id) {
        return menuService.delete(id);
    }

    /**
     * 查询用户菜单权限
     *
     * @param companyId 公司id
     * @param userId    用户id
     */
    @GetMapping("/queryUserMenu")
    public ResultData queryUserMenu(Long companyId, Long userId) {
        return menuService.queryUserMenu(companyId, userId);
    }

    /**
     * 查询用户快捷入口权限
     *
     * @param companyId 公司id
     * @param userId    用户id
     */
    @GetMapping("/queryQuickAccess")
    public ResultData queryQuickAccess(Long companyId, Long userId) {
        return menuService.queryQuickAccess(companyId, userId);
    }

}
