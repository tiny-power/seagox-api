package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicClassify;
import com.seagox.lowcode.service.IDicClassifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 数据字典分类
 */
@RestController
@RequestMapping("/dicClassify")
public class DicClassifyController {

    @Autowired
    private IDicClassifyService dicClassifyService;

    /**
     * 查询显示
     */
    @GetMapping("/queryDisplay")
    public ResultData queryDisplay(Long companyId) {
        return dicClassifyService.queryDisplay(companyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增字典分类")
    public ResultData insert(@Valid DicClassify dicClassify) {
        return dicClassifyService.insert(dicClassify);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @LogPoint("更新字典分类")
    public ResultData update(@Valid DicClassify dicClassify) {
        return dicClassifyService.update(dicClassify);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除字典分类")
    public ResultData delete(@PathVariable Long id) {
        return dicClassifyService.delete(id);
    }

    /**
     * 查询通过id
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return dicClassifyService.queryById(id);
    }
    
    /**
     * 查询通过名称
     */
    @GetMapping("/queryByName")
    public ResultData queryByName(Long companyId, String name) {
        return dicClassifyService.queryByName(companyId, name);
    }
    
    /**
     * 导入
     */
    @PostMapping("/import")
    public ResultData importHandle(@RequestParam("file") MultipartFile file, Long companyId) {
    	return dicClassifyService.importHandle(file, companyId);
    }

}
