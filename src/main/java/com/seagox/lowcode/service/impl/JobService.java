package com.seagox.lowcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.Job;
import com.seagox.lowcode.mapper.JobMapper;

import com.seagox.lowcode.service.IJobService;
import com.seagox.lowcode.util.SchedulerUtils;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JobService implements IJobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private SchedulerUtils schedulerUtils;

    @Transactional
    @Override
    public void startJob(Long id) {
        Job job = jobMapper.selectById(id);
        schedulerUtils.start(String.valueOf(id), "seagox", job.getCron(), job.getMark());
        job.setStatus(1);
        jobMapper.updateById(job);
    }

    @Transactional
    @Override
    public void stopJob(Long id) {
        Job job = jobMapper.selectById(id);
        schedulerUtils.delete(String.valueOf(id), "seagox");
        job.setStatus(0);
        jobMapper.updateById(job);
    }

    @Override
    public ResultData queryByPage(Integer pageNo, Integer pageSize, Long companyId, String name) {
        PageHelper.startPage(pageNo, pageSize);
        LambdaQueryWrapper<Job> qw = new LambdaQueryWrapper<>();
        qw.eq(Job::getCompanyId, companyId)
        .eq(!StringUtils.isEmpty(name), Job::getName, name);
        List<Job> list = jobMapper.selectList(qw);
        PageInfo<Job> pageInfo = new PageInfo<>(list);
        return ResultData.success(pageInfo);
    }

    @Override
    public ResultData insert(Job job) {
        boolean cronExpressionFlag = CronExpression.isValidExpression(job.getCron());
        if (!cronExpressionFlag) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "cron错误");
        }
        jobMapper.insert(job);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData update(Job job) {
        boolean cronExpressionFlag = CronExpression.isValidExpression(job.getCron());
        if (!cronExpressionFlag) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "cron错误");
        }
        Job originalJob = jobMapper.selectById(job.getId());
        if (!job.getCron().equals(originalJob.getCron())
                || !job.getMark().equals(originalJob.getMark())) {
            schedulerUtils.delete(String.valueOf(job.getId()), "seagox");
            schedulerUtils.start(String.valueOf(job.getId()), "seagox", job.getCron(), job.getMark());
        }
        jobMapper.updateById(job);
        return ResultData.success(null);
    }

    @Transactional
    @Override
    public ResultData delete(Long id) {
        schedulerUtils.delete(String.valueOf(id), "seagox");
        jobMapper.deleteById(id);
        return ResultData.success(null);
    }
    
    /**
     * 验证cron
     */
    @Override
    public ResultData valid(String cron) {
    	boolean cronExpressionFlag = CronExpression.isValidExpression(cron);
        if (!cronExpressionFlag) {
            return ResultData.warn(ResultCode.OTHER_ERROR, "cron错误");
        } else {
        	List<String> result = new ArrayList<>();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        	Date timePoint = new Date();
            for(int i=0; i< 5; i++) {
            	timePoint = cronSequenceGenerator.next(timePoint);
            	result.add(sdf.format(timePoint));
            }
            return ResultData.success(result);
        }
    }

}
