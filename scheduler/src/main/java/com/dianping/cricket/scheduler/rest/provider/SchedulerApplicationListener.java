package com.dianping.cricket.scheduler.rest.provider;

import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dianping.cricket.scheduler.Scheduler;
import com.dianping.cricket.scheduler.SchedulerConf;

@Provider
public class SchedulerApplicationListener implements ContainerLifecycleListener{
	private Logger logger = Logger.getLogger(SchedulerApplicationListener.class);
	private Scheduler scheduler;

	@Override
	public void onStartup(Container container) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(((ServletContainer)container).getServletContext());
		
		// Get scheduler bean from spring container.
		scheduler = context.getBean(Scheduler.class);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Run the recover process for the job.
				scheduler.recover();
			}
		});
		// Set this thread be daemon thread.
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void onReload(Container container) {
		logger.info("App reload is not allowed!");
		System.exit(1);
	}

	@Override
	public void onShutdown(Container container) {
		// Gracefully shutdown.
		scheduler.shutdown(true);
	}

}
