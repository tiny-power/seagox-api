package com.seagox.lowcode.business.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.business.entity.SolutionDesign;
import com.seagox.lowcode.business.service.ISolutionDesignService;
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
 * 方案设计
 */
@RestController
@RequestMapping("/solutionDesign")
public class SolutionDesignController {

    @Autowired
    private ISolutionDesignService solutionDesignService;

    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(defaultValue = "1") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                  @RequestParam Map<String, Object> params) {
        return solutionDesignService.queryByPage(pageNo, pageSize, params);
    }

    @GetMapping("/queryById/{id}")
    public ResultData queryById(@PathVariable Long id) {
        return solutionDesignService.queryById(id);
    }

    @GetMapping("/queryByProjectId/{projectId}")
    public ResultData queryByProjectId(@PathVariable Long projectId) {
        return solutionDesignService.queryByProjectId(projectId);
    }

    @PostMapping("/save")
    @LogPoint("保存方案设计")
    public ResultData save(SolutionDesign solutionDesign, Long userId) {
        return solutionDesignService.save(solutionDesign, userId);
    }

    @PostMapping("/submit/{id}")
    @LogPoint("提交方案设计")
    public ResultData submit(@PathVariable Long id, Long userId) {
        return solutionDesignService.submit(id, userId);
    }

    @PostMapping("/confirm/{id}")
    @LogPoint("确认方案设计")
    public ResultData confirm(@PathVariable Long id, Long userId) {
        return solutionDesignService.confirm(id, userId);
    }

    @PostMapping("/freeze/{id}")
    @LogPoint("冻结方案设计")
    public ResultData freeze(@PathVariable Long id, String signatureUrl, Long userId) {
        return solutionDesignService.freeze(id, signatureUrl, userId);
    }

    @PostMapping("/applyDefrost/{id}")
    @LogPoint("申请方案解冻")
    public ResultData applyDefrost(@PathVariable Long id, String defrostExplanation, Long userId) {
        return solutionDesignService.applyDefrost(id, defrostExplanation, userId);
    }

    @PostMapping("/approveDefrost/{id}")
    @LogPoint("同意方案解冻")
    public ResultData approveDefrost(@PathVariable Long id, Long userId) {
        return solutionDesignService.approveDefrost(id, userId);
    }

    @PostMapping("/rejectDefrost/{id}")
    @LogPoint("拒绝方案解冻")
    public ResultData rejectDefrost(@PathVariable Long id, Long userId) {
        return solutionDesignService.rejectDefrost(id, userId);
    }

    @PostMapping("/complete/{id}")
    @LogPoint("完成方案设计")
    public ResultData complete(@PathVariable Long id, Long userId) {
        return solutionDesignService.complete(id, userId);
    }

    @PostMapping("/delete/{id}")
    @LogPoint("删除方案设计")
    public ResultData delete(@PathVariable Long id) {
        return solutionDesignService.delete(id);
    }
}
