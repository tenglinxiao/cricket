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
		
		long total = 0, idle = 0, temp = 0;
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
				if (index == 4) {
					// The 4th value is idle value.
					idle = temp;
				}
			}
			index++;
		}
		// Should always be true, coding here to avoid unexpected errors.
		if (total - this.total != 0) {
			this.percentage = (double)(idle - this.idle) / (total - this.total);
			this.total = total;
			this.idle = idle;
		}
	}
}
