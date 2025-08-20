package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.FormAthority;
import com.seagox.lowcode.service.IFormAthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 表单权限
 */
@RestController
@RequestMapping("/formAthority")
public class FormAthorityController {

    @Autowired
    private IFormAthorityService formAthorityService;

    /**
     * 分页查询
     *
     * @param pageNo   起始页
     * @param pageSize 每页大小
     * @param type     类型(1:提交状态;2:查看状态;)
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long formId,
                                  int type) {
        return formAthorityService.queryByPage(pageNo, pageSize, formId, type);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增表单权限")
    public ResultData insert(@Valid FormAthority formAthority) {
        return formAthorityService.insert(formAthority);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改表单权限")
    public ResultData update(@Valid FormAthority formAthority) {
        return formAthorityService.update(formAthority);
    }
   
    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除表单权限")
    public ResultData delete(@PathVariable Long id) {
        return formAthorityService.delete(id);
    }
    
}
