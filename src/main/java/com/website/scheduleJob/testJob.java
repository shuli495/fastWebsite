package com.website.scheduleJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fastjavaframework.quartz.QuartzJob;
import com.fastjavaframework.quartz.ScheduleJobBean;

@Component
public class testJob extends QuartzJob {
	private static Logger logger = LoggerFactory.getLogger(testJob.class);


@Transactional
	@Override
	public void run(ScheduleJobBean scheduleJob) {
		System.out.println("job");
	}

}
