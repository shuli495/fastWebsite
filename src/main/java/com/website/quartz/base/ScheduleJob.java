package com.website.quartz.base;

import com.fastjavaframework.listener.base.AbstractContextListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 计划任务信息列表
 *
 * @author wangshuli
 */
@ConfigurationProperties(prefix="quartz")
public class ScheduleJob extends AbstractContextListener {
	private static Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

	/**
	 * 任务列表
	 */
	private List<ScheduleJobBean> jobs;

	/**
	 * 启动延时
	 */
	private int startDelayed;

	/**
	 * 启动日志
	 */
	private boolean logEnabled;

	@Override
	public void springContext(ApplicationContext applicationContext) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if(logEnabled) {
			logger.info("构建任务开始 "+sdf.format(new Date()));
		}

		Scheduler scheduler = null;
		try {
			if(null == scheduler) {
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.startDelayed(startDelayed);
			}
		} catch (Exception e) {
			if(logEnabled) {
				logger.info("初始化任务失败 " + e.getMessage());
			}
		}

		if(null == jobs || jobs.size() == 0) {
			if(logEnabled) {
				logger.info("无任务！");
			}
			return;
		}

		//构建xml中的计划任务
		for(ScheduleJobBean job : jobs) {
			try {
				//未启动
				if("0".equals(job.getStatus())) {
					continue;
				}

				JobDetail jobDetail = JobBuilder.newJob(job.getJobClass()).withIdentity(job.getName(), job.getGroup()).build();

				//设置任务是否启动日志
				if(!logEnabled) {
					job.setLog(false);
				}

				//是否启动日志
				jobDetail.getJobDataMap().put("log", logEnabled);
				//job信息
				jobDetail.getJobDataMap().put("scheduleJob", job);
				//spring上下文
				jobDetail.getJobDataMap().put("applicationContext", applicationContext);

				//表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
				//按新的cron表达式构建一个新的trigger
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(scheduleBuilder).build();

				scheduler.scheduleJob(jobDetail, trigger);
			} catch (SchedulerException e) {
				String message = "构建任务 " + job.getGroup() +" - " + job.getName() + " 失败：\n" + e.getMessage();
				logger.error(message);
				if(logEnabled) {
					logger.info(message);
				}
			}

			if(logEnabled) {
				logger.info("构建任务 " + job.getGroup() +" - " + job.getName() + " 成功");
			}
		}

		if(logEnabled) {
			logger.info("构建任务完成 " + sdf.format(new Date()) + " 共构建" + jobs.size() + "条任务");
		}
	}

	public void setJobs(List<ScheduleJobBean> jobs) {
		this.jobs = jobs;
	}

	public void setStartDelayed(int startDelayed) {
		this.startDelayed = startDelayed;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}
}
