package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.ConstructionDrawing;
import com.seagox.lowcode.business.service.IConstructionDrawingService;
import com.seagox.lowcode.common.ResultData;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 施工图出图
 */
@RestController
@RequestMapping("/constructionDrawing")
public class ConstructionDrawingController {

    @Autowired
    private IConstructionDrawingService constructionDrawingService;

    /**
     * 分页查询施工图出图
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return constructionDrawingService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 通过ID查询施工图出图
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return constructionDrawingService.queryById(id);
    }

    /**
     * 通过项目ID查询施工图出图
     */
    @GetMapping("/queryByProjectId/{projectId}")
    public ResultData queryByProjectId(@PathVariable Long projectId) {
        return constructionDrawingService.queryByProjectId(projectId);
    }

    /**
     * 保存施工图出图
     */
    @PostMapping("/save")
    @LogPoint("保存施工图出图")
    public ResultData save(ConstructionDrawing constructionDrawing, Long userId) {
        return constructionDrawingService.save(constructionDrawing, userId);
    }

    /**
     * 提交施工图出图
     */
    @PostMapping("/submit/{id}")
    @LogPoint("提交施工图出图")
    public ResultData submit(@PathVariable Long id, Long userId) {
        return constructionDrawingService.submit(id, userId);
    }

    /**
     * 确认阅读施工图出图
     */
    @PostMapping("/confirmRead/{id}")
    @LogPoint("确认阅读施工图")
    public ResultData confirmRead(@PathVariable Long id, String roleKey, Long userId) {
        return constructionDrawingService.confirmRead(id, roleKey, userId);
    }

    /**
     * 取消归档施工图出图
     */
    @PostMapping("/cancelArchive/{id}")
    @LogPoint("取消施工图归档")
    public ResultData cancelArchive(@PathVariable Long id, Long userId) {
        return constructionDrawingService.cancelArchive(id, userId);
    }

    /**
     * 删除施工图出图
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除施工图出图")
    public ResultData delete(@PathVariable Long id) {
        return constructionDrawingService.delete(id);
    }
}
