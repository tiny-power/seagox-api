package com.seagox.lowcode.controller;

import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Serial;
import com.seagox.lowcode.service.ISerialService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 编号
 */
@RestController
@RequestMapping("/serial")
public class SerialController {

    @Autowired
    private ISerialService serialService;

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
        return serialService.queryByPage(pageNo, pageSize, companyId);
    }
    
    /**
     * 查询全部
     * 
     * @param companyId 公司id
     */
    @GetMapping("/queryAll")
    public ResultData queryAll(Long companyId) {
        return serialService.queryAll(companyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    public ResultData insert(@Valid Serial serial) {
        return serialService.insert(serial);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public ResultData update(@Valid Serial serial) {
        return serialService.update(serial);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    public ResultData delete(@PathVariable Long id) {
        return serialService.delete(id);
    }

}
