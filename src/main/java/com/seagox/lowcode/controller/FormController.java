package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Form;
import com.seagox.lowcode.service.IFormService;
import com.seagox.lowcode.util.DocumentConverterUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 表单管理
 */
@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private IFormService formService;
    
    @Autowired
    private DocumentConverterUtils documentConverterUtils;

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param name     名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId,
                                  String name) {
        return formService.queryByPage(pageNo, pageSize, companyId, name);
    }

    /**
     * 查询全部通过公司id
     */
    @GetMapping("/queryByCompanyId")
    public ResultData queryByCompanyId(Long companyId) {
        return formService.queryByCompanyId(companyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增表单")
    public ResultData insert(@Valid Form form) {
        return formService.insert(form);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改表单")
    public ResultData update(@Valid Form form) {
        return formService.update(form);
    }
   
    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除表单")
    public ResultData delete(@PathVariable Long id) {
        return formService.delete(id);
    }

    /**
     * 表单详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id, Long userId) {
        return formService.queryById(userId, id);
    }

    /**
     * 新增(自定义)
     */
    @PostMapping("/insertCustom")
    @LogPoint("表单保存")
    public ResultData insertCustom(@RequestParam Map<String, Object> request) {
        return formService.insertCustom(request);
    }

    /**
     * 编辑(自定义)
     */
    @PostMapping("/updateCustom")
    @LogPoint("表单编辑")
    public ResultData updateCustom(@RequestParam Map<String, Object> request) {
        return formService.updateCustom(request);
    }

    /**
     * 查询列表数据
     *
     * @param userId   用户id
     * @param id       表单id
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param search   搜索条件
     */
    @PostMapping("/queryListById")
    public ResultData queryListById(Long companyId, Long userId, Long id,
                                    @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, String search) {
        return formService.queryListById(companyId, userId, id, pageNo, pageSize, search);
    }

    /**
     * 删除(自定义)
     */
    @PostMapping("/deleteCustom")
    @LogPoint("删除(自定义)")
    public ResultData deleteCustom(String businessType, String businessKeys) {
        return formService.deleteCustom(businessType, businessKeys);
    }
    
    /**
     * 获取动态业务数据
     *
     * @param path 路径
     * @param type 类型(tree:属性;list:列表;)
     */
    @GetMapping("/queryDynamic")
    public ResultData queryDynamic(Long companyId, String path, String type) {
        return formService.queryDynamic(companyId, path, type);
    }
    
    /**
     * 导出
     */
    @PostMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response) {
        formService.export(request, response);
    }
    
    /**
     * 表单详情(自定义)
     */
    @GetMapping("/queryCustomDetail")
    public ResultData queryCustomDetail(Long companyId, Long userId, Long formId, Long id) {
        return formService.queryCustomDetail(companyId, userId, formId, id);
    }
    
    /**
     * 查询流程类型
     *
     * @param companyId 单位id
     * @return
     */
    @GetMapping("/queryBusinessTypes")
    public ResultData queryBusinessTypes(Long companyId) {
        return formService.queryBusinessTypes(companyId);
    }
    
    /**
     * 获取业务应用数据
     */
    @PostMapping("/queryOptions")
    public ResultData queryOptions(String formId, String value) {
        return formService.queryOptions(formId, value);
    }
    
    /**
     * 获取单据
     */
    @PostMapping("/queryBill")
    public ResultData queryBill(String formId, String field, String serial) {
        return formService.queryBill(formId, field, serial);
    }
    
    /**
     * 导入
     */
    @PostMapping("/import")
    public ResultData importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return formService.importExcel(file, request);
    }
    
    /**
     * 下载模板
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, Long companyId, Long id) {
    	formService.download(response, companyId, id);
    }
    
    /**
     * 打印预览
     */
    @GetMapping("/print")
    public void print(Long businessType, Long businessKey, HttpServletResponse response) {
    	OutputStream outputStream = null;
    	InputStream inputStream = null;
    	try {
			String path = formService.print(businessType, businessKey);
			response.setHeader("Content-Disposition", "filename=" + new String("preview.pdf"));
			response.setContentType("application/pdf");
			outputStream = response.getOutputStream();
			inputStream = new FileInputStream(path);
			documentConverterUtils.convert("docx", inputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
}
