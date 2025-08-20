package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 组织架构
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    /**
     * 查询(组织架构)
     */
    @GetMapping("/queryAll")
    public ResultData queryAll(Long companyId) {
        return companyService.queryAll(companyId);
    }

    /**
     * 查询全部单位及部门树结构
     */
    @GetMapping("/queryAllAndDept")
    public ResultData queryAllAndDept(Long companyId, boolean isAll) {
        return companyService.queryAllAndDept(companyId, isAll);
    }

    /**
     * 查询单位角色树
     */
    @GetMapping("/queryAllAndRole")
    public ResultData queryAllAndRole(Long companyId) {
        return companyService.queryAllAndRole(companyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增单位")
    public ResultData insert(@Valid Company company, Long companyId, Long userId) {
        return companyService.insert(companyId, userId, company);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @LogPoint("更新单位")
    public ResultData update(@Valid Company company) {
        return companyService.update(company);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除单位")
    public ResultData delete(@PathVariable Long id) {
        return companyService.delete(id);
    }

    /**
     * 切换
     */
    @PostMapping("/change/{changeCompanyId}")
    @LogPoint("切换单位")
    public ResultData change(@PathVariable Long changeCompanyId, Long userId) {
        return companyService.change(changeCompanyId, userId);
    }

}
