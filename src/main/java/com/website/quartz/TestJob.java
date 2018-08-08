package com.website.quartz;

import com.website.quartz.base.AbstractQuartzJob;
import com.website.quartz.base.ScheduleJobBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务demo
 *
 * @author wangshuli
 */
@Component
public class TestJob extends AbstractQuartzJob {
	private static Logger logger = LoggerFactory.getLogger(TestJob.class);

	@Override
	public void run(ScheduleJobBean scheduleJob) {
		System.out.println("job");
	}

}
