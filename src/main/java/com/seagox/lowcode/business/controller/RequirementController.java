package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.Requirement;
import com.seagox.lowcode.business.service.IRequirementService;
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
 * 需求沟通表
 */
@RestController
@RequestMapping("/requirement")
public class RequirementController {

    /**
     * 需求沟通服务
     */
    @Autowired
    private IRequirementService requirementService;

    /**
     * 分页查询需求沟通
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return requirementService.queryByPage(pageNo, pageSize, params);
    }

    /**
     * 查询需求沟通详情
     */
    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return requirementService.queryById(id);
    }

    /**
     * 查询项目需求沟通
     */
    @GetMapping("/queryByProjectId/{projectId}")
    public ResultData queryByProjectId(@PathVariable Long projectId) {
        return requirementService.queryByProjectId(projectId);
    }

    /**
     * 保存需求沟通
     */
    @PostMapping("/save")
    @LogPoint("保存需求沟通")
    public ResultData save(Requirement requirement, Long userId) {
        return requirementService.save(requirement, userId);
    }

    /**
     * 提交业主审核
     */
    @PostMapping("/submit/{id}")
    @LogPoint("提交需求沟通")
    public ResultData submit(@PathVariable Long id, Long userId) {
        return requirementService.submit(id, userId);
    }

    /**
     * 签字确认
     */
    @PostMapping("/sign/{id}")
    @LogPoint("确认需求沟通")
    public ResultData sign(@PathVariable Long id, String signatureUrl, Long userId) {
        return requirementService.sign(id, signatureUrl, userId);
    }

    /**
     * 删除需求沟通
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除需求沟通")
    public ResultData delete(@PathVariable Long id) {
        return requirementService.delete(id);
    }
}
