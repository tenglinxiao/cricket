package com.dianping.cricket.scheduler.job;
import org.quartz.SchedulerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dianping.cricket.scheduler.SchedulerLoader;
import com.dianping.cricket.scheduler.pojo.Job;
public class Scheduler {
	public static void main(String args[]) throws SchedulerException, ClassNotFoundException {
//		org.quartz.Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//		scheduler.start();
//		Class<?> cls = Class.forName("com.dianping.cricket.scheduler.job.DaemonJob");
//		for (int index = 0; index < 10; index++) {
//		JobDetail job = newJob((Class<? extends Job>)cls).withIdentity("test" + index, "test").build();
//		Trigger trigger = newTrigger().startNow().withSchedule(simpleSchedule()
//		          .withIntervalInSeconds(3)
//		          .repeatForever()).build();
//		scheduler.scheduleJob(job, trigger);
//		}
//
//		System.out.println(Thread.currentThread().getId());
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
    	//new com.dianping.cricket.scheduler.Scheduler().init();
//    	Job job = new Job();
//    	job.setName("test");
//    	job.setGroup("test");
//    	job.setOwner("tenglinxiao");
//    	job.setSchedule("* * * * * *");
//    	job.setMainEntry("test");
//
//    	SchedulerLoader.getLoader().createJob(job);
    	//new com.dianping.cricket.scheduler.Scheduler().init();
		
		
	}

}
