package com.seagox.lowcode.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Company;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.entity.DeptUser;
import com.seagox.lowcode.mapper.CompanyMapper;
import com.seagox.lowcode.mapper.DepartmentMapper;
import com.seagox.lowcode.mapper.DeptUserMapper;
import com.seagox.lowcode.service.IDepartmentService;
import com.seagox.lowcode.template.DepartmentModel;
import com.seagox.lowcode.util.TreeUtils;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private DeptUserMapper deptUserMapper;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 查询全部通过公司id
     */
    @Override
    public ResultData queryByCompanyId(Long companyId, String searchCompanyId) {
        List<Map<String, Object>> list = departmentMapper.queryByCompanyId(Long.valueOf(searchCompanyId));
        return ResultData.success(TreeUtils.categoryTreeHandle(list, "parentId", 0L));
    }

    @Override
    public ResultData insert(Department department) {
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getCompanyId, department.getCompanyId())
                .eq(!StringUtils.isEmpty(department.getParentId()), Department::getParentId, department.getParentId())
                .eq(Department::getName, department.getName());
        Long count = departmentMapper.selectCount(queryWrapper);
        if (count != 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "部门已经存在");
        }
        Department parentDepartment = departmentMapper.selectById(department.getParentId());
		String maxCode = "";
		if (parentDepartment != null) {
			maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), parentDepartment.getCode(),
					parentDepartment.getCode().length() + 3);
			if (StringUtils.isEmpty(maxCode)) {
				maxCode = parentDepartment.getCode() + "000";
			}
		} else {
			maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), "", 3);
			if (StringUtils.isEmpty(maxCode)) {
				maxCode = "100";
			}
		}
		BigInteger bigInteger = new BigInteger(maxCode);
		bigInteger = bigInteger.add(BigInteger.valueOf(1));
		department.setCode(bigInteger.toString());
		departmentMapper.insert(department);
		return ResultData.success(null);
    }
    
    @Override
	public ResultData update(Department department) {
		Department originalDepartment = departmentMapper.selectById(department.getId());
		if (originalDepartment.getName().equals(department.getName())) {
			if ((originalDepartment.getParentId() == null && department.getParentId() != null)
					|| (department.getParentId() == null && originalDepartment.getParentId() != null)
					|| (department.getParentId() != null
							&& !originalDepartment.getParentId().equals(department.getParentId()))) {
				Department parentDepartment = departmentMapper.selectById(department.getParentId());
				String maxCode = "";
				if (parentDepartment != null) {
					maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), parentDepartment.getCode(),
							parentDepartment.getCode().length() + 3);
					if (StringUtils.isEmpty(maxCode)) {
						maxCode = parentDepartment.getCode() + "000";
					}
				} else {
					maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), "", 3);
					if (StringUtils.isEmpty(maxCode)) {
						maxCode = "100";
					}
				}
				BigInteger bigInteger = new BigInteger(maxCode);
				bigInteger = bigInteger.add(BigInteger.valueOf(1));
				department.setCode(bigInteger.toString());
			}
			departmentMapper.updateById(department);
			return ResultData.success(null);
		} else {
			LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(Department::getCompanyId, department.getCompanyId())
					.eq(!StringUtils.isEmpty(department.getParentId()), Department::getParentId,
							department.getParentId())
					.eq(Department::getName, department.getName());
			Long count = departmentMapper.selectCount(queryWrapper);
			if (count == 0) {
				if ((originalDepartment.getParentId() == null && department.getParentId() != null)
						|| (department.getParentId() == null && originalDepartment.getParentId() != null)
						|| (department.getParentId() != null
								&& !originalDepartment.getParentId().equals(department.getParentId()))) {
					Department parentDepartment = departmentMapper.selectById(department.getParentId());
					String maxCode = "";
					if (parentDepartment != null) {
						maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), parentDepartment.getCode(),
								parentDepartment.getCode().length() + 3);
						if (StringUtils.isEmpty(maxCode)) {
							maxCode = parentDepartment.getCode() + "000";
						}
					} else {
						maxCode = departmentMapper.queryMaxCode(department.getCompanyId(), "", 3);
						if (StringUtils.isEmpty(maxCode)) {
							maxCode = "100";
						}
					}
					BigInteger bigInteger = new BigInteger(maxCode);
					bigInteger = bigInteger.add(BigInteger.valueOf(1));
					department.setCode(bigInteger.toString());
				}
				departmentMapper.updateById(department);
				return ResultData.success(null);
			} else {
				return ResultData.warn(ResultCode.OTHER_ERROR, "部门已经存在");
			}
		}
	}

    @Override
    public ResultData delete(Long id) {
        LambdaQueryWrapper<Department> departmentQueryWrapper = new LambdaQueryWrapper<>();
        departmentQueryWrapper.eq(Department::getParentId, id);
        Long departmentCount = departmentMapper.selectCount(departmentQueryWrapper);
        if (departmentCount != 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "存在子部门，不可删除");
        }

        LambdaQueryWrapper<DeptUser> deptUserQw = new LambdaQueryWrapper<>();
        deptUserQw.eq(DeptUser::getDepartmentId, id);
        Long count = deptUserMapper.selectCount(deptUserQw);
        if (count != 0) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "部门存在人员，不可删除");
        }
        departmentMapper.deleteById(id);
        return ResultData.success(null);
    }

    /**
     * 导入处理
     */
    @Transactional
    @Override
    public ResultData importHandle(List<DepartmentModel> list) {
        for (DepartmentModel departmentModel : list) {
            LambdaQueryWrapper<Company> qwCompany = new LambdaQueryWrapper<>();
            qwCompany.eq(Company::getName, departmentModel.getCompanyName());
            Company company = companyMapper.selectOne(qwCompany);
            Department insertDepartment = new Department();
            if (departmentModel.getpNmae() != null) {
                LambdaQueryWrapper<Department> qwDept = new LambdaQueryWrapper<>();
                qwDept.eq(Department::getCompanyId, company.getId()).eq(Department::getName,
                        departmentModel.getpNmae());
                Long deptCount = departmentMapper.selectCount(qwDept);
                if (deptCount <= 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultData.warn(ResultCode.OTHER_ERROR, "上级部门:" + departmentModel.getpNmae() + "不存在!");
                } else {
                    Department pDept = departmentMapper.selectOne(qwDept);
                    insertDepartment.setParentId(pDept.getId());
                    insertDepartment.setSort(departmentModel.getSort());
                    insertDepartment.setCompanyId(company.getId());
                    insertDepartment.setName(departmentModel.getDeptName());
                    insertDepartment.setCode(departmentModel.getCode());
                }
            } else {
                insertDepartment.setSort(departmentModel.getSort());
                insertDepartment.setCompanyId(company.getId());
                insertDepartment.setName(departmentModel.getDeptName());
                insertDepartment.setCode(departmentModel.getCode());
            }
            insert(insertDepartment);
        }
        return ResultData.success(null);
    }

}
