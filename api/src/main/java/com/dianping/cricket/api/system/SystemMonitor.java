package com.dianping.cricket.api.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dianping.cricket.api.Observable;
import com.dianping.cricket.api.Observer;
import com.dianping.cricket.api.event.Event;

public class SystemMonitor implements Observable {
	private static SystemMonitor monitor = new SystemMonitor();
	private List<Observer> observers = new ArrayList<Observer>();
	
	static {
		// Register a cpu usage obj.
		monitor.addObserver(new CpuUsage());
		
		// Register a memory usage obj.
		monitor.addObserver(new MemoryUsage());
		ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
		
		// Schedule check thread for cpu usage.
		service.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					String result = callProcess(CpuUsage.COMMANDS);
					monitor.notify(monitor.new MonitorEvent(CpuUsage.class.getSimpleName(), result));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}, 0, 3, TimeUnit.SECONDS);
		
		// Schedule check thread for memory usage.
		service.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				String result = callProcess(MemoryUsage.COMMANDS);
				monitor.notify(monitor.new MonitorEvent(MemoryUsage.class.getSimpleName(), result));	

			}
			
		}, 0, 3, TimeUnit.SECONDS);
	}
	
	private SystemMonitor(){}
	
	public static String callProcess(String[] commands) {
		// String builder for storing result of command.
		StringBuilder strBuilder = new StringBuilder();
		InputStream in = null;
		
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			
			// Kickoff the process.
			Process process = builder.start();
			
			int length = -1;
			byte[] bytes = new byte[1024];
			
			// Get process output as input stream.
			in = process.getInputStream();
			while ((length = in.read(bytes)) != -1) {
				strBuilder.append(new String(bytes, 0, length));
			}
			in.close();
			
			// Wait for the process to end.
			process.waitFor();
			
			return strBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return null;
		}
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void notify(Event event) {
		for (Observer observer : observers) {
			if (event.match(observer)) {
				observer.doAction(event);
			}
		}
	}
	
	private List<Observer> getObservers()
	{
		return this.observers;
	}
	
	public static <T> T getMonitor(Class<?> clz) {
		for (Observer observer : monitor.getObservers()) {
			if (clz == observer.getClass()) {
				return (T)observer;
			}
		}
		return null;
	}
	
	public static Map<String, Object> getUsages() {
		Map<String, Object> usages = new HashMap<String, Object>();
		for (Observer observer : monitor.getObservers()) {
			usages.put(observer.getClass().getSimpleName(), observer);
		}
		return usages;
	}
	
	
	private class MonitorEvent extends Event {
		public MonitorEvent(String eventName, Object data) {
			this.setEventName(eventName);
			this.setData(data);
		}

		@Override
		public boolean match(Object obj) {
			return obj.getClass().getSimpleName().equals(this.getEventName());
		}
	}
}
