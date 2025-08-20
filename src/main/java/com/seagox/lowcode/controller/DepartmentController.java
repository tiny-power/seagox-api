package com.seagox.lowcode.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Department;
import com.seagox.lowcode.service.IDepartmentService;
import com.seagox.lowcode.verify.DepartmentExcelVerifyHandler;
import com.seagox.lowcode.template.DepartmentModel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

/**
 * 部门
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private IDepartmentService departmentService;

	@Autowired
	private DepartmentExcelVerifyHandler departmentExcelVerifyHandler;

	/**
	 * 查询全部通过公司id
	 *
	 * @param companyId 公司id
	 */
	@GetMapping("/queryByCompanyId")
	public ResultData queryByPage(Long companyId, String searchCompanyId) {
		return departmentService.queryByCompanyId(companyId, searchCompanyId);
	}

	/**
	 * 新增
	 */
	@PostMapping("/insert")
	@LogPoint("新增部门")
	public ResultData insert(@Valid Department department) {
		return departmentService.insert(department);
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	@LogPoint("更新部门")
	public ResultData update(@Valid Department department) {
		return departmentService.update(department);
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete/{id}")
	@LogPoint("删除部门")
	public ResultData delete(@PathVariable Long id) {
		return departmentService.delete(id);
	}

	/**
	 * 导入
	 */
	@PostMapping("/import")
	public ResultData importHandle(@RequestParam("file") MultipartFile file) {
		ImportParams params = new ImportParams();
		params.setHeadRows(1);
		params.setNeedVerify(true);
		params.setVerifyHandler(departmentExcelVerifyHandler);// 设置一个验证处理器
		try {
			ExcelImportResult<DepartmentModel> result = ExcelImportUtil.importExcelMore(file.getInputStream(),
					DepartmentModel.class, params);
			Map<String, Object> repeatMap = new HashMap<>();
			List<DepartmentModel> resultList = result.getList();
			for (int i = 0; i < resultList.size(); i++) {
				if (repeatMap.containsKey(resultList.get(i).getDeptName())) {
					return ResultData.warn(ResultCode.OTHER_ERROR, "部门名称重复,请检查:" + resultList.get(i).getDeptName());
				} else {
					repeatMap.put(resultList.get(i).getDeptName(), 1);
				}
			}
			// 判断是否有错误
			if (result.isVerifyFail()) {
				for (DepartmentModel entity : result.getFailList()) {
					return ResultData.warn(ResultCode.OTHER_ERROR,
							"第" + entity.getRowNum() + "行的错误是：" + entity.getErrorMsg());
				}
			} else {
				// 获到正确的数据
				return departmentService.importHandle(result.getList());
			}
			return ResultData.success(null);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultData.warn(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}
}
