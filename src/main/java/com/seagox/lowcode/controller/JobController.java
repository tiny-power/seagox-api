package com.seagox.lowcode.controller;

import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Job;
import com.seagox.lowcode.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 任务调度
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private IJobService jobService;

    /**
     * 启动
     */
    @PostMapping("/startJob/{id}")
    @LogPoint("启动任务调度")
    public ResultData startJob(@PathVariable Long id) {
        jobService.startJob(id);
        return ResultData.success(null);
    }

    /**
     * 暂停
     */
    @PostMapping("/stopJob/{id}")
    @LogPoint("暂停任务调度")
    public ResultData stopJob(@PathVariable Long id) {
        jobService.stopJob(id);
        return ResultData.success(null);
    }

    /**
     * 分页查询
     *
     * @param pageNo    起始页
     * @param pageSize  每页大小
     * @param companyId 公司id
     * @param name 名称
     */
    @GetMapping("/queryByPage")
    public ResultData queryByPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, Long companyId, String name) {
        return jobService.queryByPage(pageNo, pageSize, companyId, name);
    }

    /**
     * 新增
     */
    @PostMapping("/insert")
    @LogPoint("新增任务调度")
    public ResultData insert(@Valid Job job) {
        return jobService.insert(job);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @LogPoint("修改任务调度")
    public ResultData update(@Valid Job job) {
        return jobService.update(job);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @LogPoint("删除任务调度")
    public ResultData delete(@PathVariable Long id) {
        return jobService.delete(id);
    }
    
    /**
     * 验证cron
     */
    @PostMapping("/valid")
    @LogPoint("验证cron")
    public ResultData valid(String cron) {
        return jobService.valid(cron);
    }

}
