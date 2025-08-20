package com.seagox.lowcode.assets.job;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import com.seagox.lowcode.strategy.job.JobHandler;
import com.seagox.lowcode.strategy.job.JobHandlerFactory;

@Service
public class FirstJobHandler extends JobHandler {

	@Override
	public void afterPropertiesSet() throws Exception {
		JobHandlerFactory.register("first", this);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("123");
		
	}
}
