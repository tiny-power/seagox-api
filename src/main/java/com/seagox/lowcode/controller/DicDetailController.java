package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.DicDetail;
import com.seagox.lowcode.service.IDicDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 数据字典详情
 */
@RestController
@RequestMapping("/dicDetail")
public class DicDetailController {

    @Autowired
    private IDicDetailService dicDetailService;

    /**
     * 查询显示
     */
    @GetMapping("/queryDisplay")
    public ResultData queryDisplay(Long classifyId) {
        return dicDetailService.queryDisplay(classifyId);
    }

    /**
     * 查询显示（不封装树）
     */
    @GetMapping("/queryDisplayNoTree")
    public ResultData queryDisplayNoTree(Long classifyId) {
        return dicDetailService.queryDisplayNoTree(classifyId);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增字典")
    public ResultData insert(Long companyId, @Valid DicDetail dicDetail) {
        return dicDetailService.insert(companyId, dicDetail);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @LogPoint("更新字典")
    public ResultData update(Long companyId, @Valid DicDetail dicDetail) {
        return dicDetailService.update(companyId, dicDetail);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除字典")
    public ResultData delete(@PathVariable Long id, Long companyId) {
        return dicDetailService.delete(companyId, id);
    }

    /**
     * 字典分类详情
     *
     * @param classifyId 字典分类id
     */
    @GetMapping("/queryByClassifyId/{classifyId}")
    public ResultData queryByClassifyId(@PathVariable Long classifyId) {
        return dicDetailService.queryByClassifyId(classifyId);
    }
    
    /**
     * 批量添加
     */
    @PostMapping("/batch")
    @LogPoint("批量添加")
    public ResultData batch(Long companyId, Long classifyId, String remark) {
        return dicDetailService.batch(companyId, classifyId, remark);
    }

}
