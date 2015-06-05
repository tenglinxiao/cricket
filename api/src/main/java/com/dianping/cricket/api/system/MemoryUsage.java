package com.dianping.cricket.api.system;

import java.text.NumberFormat;
import java.util.StringTokenizer;

import com.dianping.cricket.api.Observer;
import com.dianping.cricket.api.event.Event;

public class MemoryUsage implements Observer {
	private static final NumberFormat FORMAT = NumberFormat.getPercentInstance(); 
	public static final String[] COMMANDS = new String[]{"sh", "-c", "free | grep Mem"};
	private long total;
	private long free;
	private long used;
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

	public long getFree() {
		return free;
	}

	public void setFree(long free) {
		this.free = free;
	}

	public long getUsed() {
		return used;
	}

	public void setUsed(long used) {
		this.used = used;
	}
	
	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
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
		StringTokenizer tokenizer = new StringTokenizer(result);
		String token = null;
		int index = 0;
		while (tokenizer.hasMoreElements()) {
			token = tokenizer.nextToken();
			if (token.matches("\\d+")) {
				switch (index) {
				case 1: total = Long.parseLong(token); break;
				case 2: used = Long.parseLong(token); break;
				case 3: free = Long.parseLong(token); break;
				}
			}
			index++;
		}
		if (total != 0) {
			percentage = (double)used / total;
		}
	}
}
