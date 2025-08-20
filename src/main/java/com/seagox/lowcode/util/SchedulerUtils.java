package com.seagox.lowcode.util;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seagox.lowcode.strategy.job.JobHandler;
import com.seagox.lowcode.strategy.job.JobHandlerFactory;

import java.util.Date;

@Component
public class SchedulerUtils {
	
	/**
     * 任务调度
     */
    @Autowired
    private Scheduler scheduler;
    
	public void start(String name, String group, String cron, String className) {
		try {
			if(scheduler.checkExists(new JobKey(name,group))) {
				this.delete(name, group);
			}
            // JobDetail 是具体Job实例
			JobHandler eventService = JobHandlerFactory.getHandler(className);
			if(eventService != null) {
				JobDetail jobDetail = JobBuilder.newJob(eventService.getClass())
		            	.withIdentity(name, group)
		            	.build();
		            // 基于表达式构建触发器
		            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
		            // CronTrigger表达式触发器 继承于Trigger
		            // TriggerBuilder 用于构建触发器实例
		            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, "seagox")
		                    .withSchedule(cronScheduleBuilder).build();
		            scheduler.scheduleJob(jobDetail, cronTrigger);
		            scheduler.start();
			}
        } catch (Exception schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }
	}

	public boolean modify(String name, String group, String cron) {
		Date date = null;
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            String oldCron = cronTrigger.getCronExpression();
            if (!oldCron.equalsIgnoreCase(cron)) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                        .withSchedule(cronScheduleBuilder).build();
                date = scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }
        return date != null;
	}

	public void stop(String name, String group) {
		try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }		
	}

	public void stopAll() {
		try {
            scheduler.pauseAll();
        } catch (SchedulerException schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }
	}

	public void resume(String name, String group) {
		try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }
	}

	public void resumeAll() {
		try {
            scheduler.resumeAll();
        } catch (SchedulerException schedulerException) {
        	throw new RuntimeException(schedulerException.getMessage());
        }
	}

	public void delete(String name, String group) {
		try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null)
                return;
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException schedulerException) {
        	schedulerException.printStackTrace();
        }
	}

}
