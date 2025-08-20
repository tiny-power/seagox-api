package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;

public interface ICompanyService {

    /**
     * 查询全部
     */
    public ResultData queryAll(Long companyId);
    
    /**
     * 查询全部单位及部门树结构
     */
    public ResultData queryAllAndDept(Long companyId, boolean isAll);

    /**
     * 查询单位角色树
     */
    public ResultData queryAllAndRole(Long companyId);

    /**
     * 新增
     */
    public ResultData insert(Long companyId, Long userId, Company company);

    /**
     * 更新
     */
    public ResultData update(Company company);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 切换
     */
    public ResultData change(Long changeCompanyId, Long userId);
    
}
