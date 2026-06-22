package com.seagox.lowcode.business.controller;

import com.alibaba.fastjson2.JSON;
import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.dto.ProjectSaveRequest;
import com.seagox.lowcode.business.service.IProjectService;
import com.seagox.lowcode.common.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  String code, String name, String status) {
        return projectService.queryByPage(pageNo, pageSize, code, name, status);
    }

    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return projectService.queryById(id);
    }

    @PostMapping("/insert")
    @LogPoint("新增项目")
    public ResultData insert(String projectData, Long userId) {
        return projectService.insert(JSON.parseObject(projectData, ProjectSaveRequest.class), userId);
    }

    @PostMapping("/update")
    @LogPoint("修改项目")
    public ResultData update(String projectData, Long userId) {
        return projectService.update(JSON.parseObject(projectData, ProjectSaveRequest.class), userId);
    }

    @PostMapping("/delete/{id}")
    @LogPoint("删除项目")
    public ResultData delete(@PathVariable Long id) {
        return projectService.delete(id);
    }
}
