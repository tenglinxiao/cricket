package com.dianping.cricket.api.system;

import java.text.NumberFormat;
import java.util.StringTokenizer;

import com.dianping.cricket.api.Observer;
import com.dianping.cricket.api.event.Event;

public class CpuUsage implements Observer {
	private static final NumberFormat FORMAT = NumberFormat.getPercentInstance(); 
	public static final String[] COMMANDS = new String[]{"head", "-n 1", "/proc/stat"};
	private long total;
	private long idle;
	private long user;
	private long system;
	private double userPercentage;
	private double systemPercentage;
	private double percentage;
	
	static {
		FORMAT.setMaximumFractionDigits(2);
		FORMAT.setMinimumFractionDigits(2);
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getIdle() {
		return idle;
	}
	
	public void setIdle(long idle) {
		this.idle = idle;
	}
	
	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getSystem() {
		return system;
	}

	public void setSystem(long system) {
		this.system = system;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getUserPercentage() {
		return userPercentage;
	}

	public void setUserPercentage(double userPercentage) {
		this.userPercentage = userPercentage;
	}

	public double getSystemPercentage() {
		return systemPercentage;
	}

	public void setSystemPercentage(double systemPercentage) {
		this.systemPercentage = systemPercentage;
	}

	public String getUsagePercentage() {
		return FORMAT.format(percentage);
	}

	@Override
	public void doAction(Event event) {
		String result = (String)event.getData();
		if (result == null) {
			return;
		}
		
		long total = 0, idle = 0, user= 0, system = 0, temp = 0;
		StringTokenizer tokenizer = new StringTokenizer(result);
		String token = null;
		int index = 0;
		while (tokenizer.hasMoreElements()) {
			token = tokenizer.nextToken();
			if (token.matches("\\d+")) {
				// Parse the long value from token.
				temp = Long.parseLong(token);
				// Add all the values found.
				total += temp;
				switch(index) {
				// The 1th user is idle value.
				case 1: user = temp; break;
				// The 4th value is idle value.
				case 3: system = temp; break;
				// The 4th value is idle value.
				case 4: idle = temp; break;
				}
			}
			index++;
		}
		// Should always be true, coding here to avoid unexpected errors.
		if (total - this.total != 0) {
			this.percentage = 1 - (double)(idle - this.idle) / (total - this.total);
			this.userPercentage = 1 - (double)(user - this.user) / (total - this.total);
			this.systemPercentage = 1 - (double)(system - this.system) / (total - this.total);
			this.total = total;
			this.idle = idle;
			this.user = user;
			this.system = system;
		}
	}
}
