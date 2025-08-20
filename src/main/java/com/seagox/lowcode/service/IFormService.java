package com.seagox.lowcode.service;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Form;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface IFormService {

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param name     名称
     */
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name);

    /**
     * 查询全部通过公司id
     */
    public ResultData queryByCompanyId(Long companyId);

    /**
     * 添加
     */
    public ResultData insert(Form form);

    /**
     * 修改
     */
    public ResultData update(Form form);

    /**
     * 表单详情
     */
    public ResultData queryById(Long userId, Long id);

    /**
     * 删除
     */
    public ResultData delete(Long id);

    /**
     * 新增(自定义)
     */
    public ResultData insertCustom(Map<String, Object> request);

    /**
     * 编辑(自定义)
     */
    public ResultData updateCustom(Map<String, Object> request);

    /**
     * 查询列表数据
     *
     * @param companyId 公司id
     * @param userId    用户id
     * @param id        表单id
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param search    搜索条件
     */
    public ResultData queryListById(Long companyId, Long userId, Long id, Integer pageNo, Integer pageSize, String search);

    /**
     * 删除(自定义)
     */
    public ResultData deleteCustom(String businessType, String businessKeys);
    
    /**
     * 获取动态业务数据
     *
     * @param path 路径
     * @param type 类型(tree:属性;list:列表;)
     */
    public ResultData queryDynamic(Long companyId, String path, String type);
    
    /**
     * 导出
     */
    public void export(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 导入
     */
    public ResultData importExcel(MultipartFile file, HttpServletRequest request);
    
    /**
     * 获取表单详情(自定义)
     */
    public ResultData queryCustomDetail(Long companyId, Long userId, Long formId, Long id);
    
    /**
     * 查询流程类型
     *
     * @param companyId 单位id
     * @return
     */
    public ResultData queryBusinessTypes(Long companyId);
    
    /**
     * 获取业务应用数据
     */
    public ResultData queryOptions(String formId, String value);
    
    /**
     * 获取单据
     */
    public ResultData queryBill(String formId, String field, String serial);
    
    /**
     * 下载模板
     */
    public void download(HttpServletResponse response, Long companyId, Long id);
    
    /**
     * 打印模版
     */
    public String print(Long businessType, Long businessKey);

}
