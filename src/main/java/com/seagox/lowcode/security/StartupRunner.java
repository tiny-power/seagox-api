package com.seagox.lowcode.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seagox.lowcode.entity.Job;
import com.seagox.lowcode.mapper.JobMapper;
import com.seagox.lowcode.util.SchedulerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private SchedulerUtils schedulerUtils;

    @Override
    public void run(String... args) throws Exception {
        //任务调度
        LambdaQueryWrapper<Job> qw = new LambdaQueryWrapper<>();
        qw.eq(Job::getStatus, 1);
        List<Job> jobList = jobMapper.selectList(qw);
        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            schedulerUtils.start(String.valueOf(job.getId()), "seagox", job.getCron(), job.getMark());
        }
    } 

}
