package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysRole;
import com.seagox.lowcode.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId) {
        return roleService.queryByPage(pageNo, pageSize, companyId);
    }

    /**
     * 新增角色
     */
    @PostMapping("/insert")
    @LogPoint("新增角色")
    public ResultData insert(@Valid SysRole role) {
        return roleService.insert(role);
    }

    /**
     * 更新角色
     */
    @PostMapping("/update")
    @LogPoint("更新角色")
    public ResultData update(@Valid SysRole role) {
        return roleService.update(role);
    }

    /**
     * 授权
     */
    @PostMapping("/authorize")
    @LogPoint("授权")
    public ResultData relateUsers(Long companyId, String userIds,Long roleId) {
        return roleService.authorize(companyId, userIds,roleId);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除角色")
    public ResultData delete(@PathVariable Long id) {
        return roleService.delete(id);
    }

    /**
     * 查询角色给下拉框
     *
     * @param companyId 公司id
     */
    @GetMapping("/queryAll")
    public ResultData queryAll(Long companyId) {
        return roleService.queryAll(companyId);
    }
}
