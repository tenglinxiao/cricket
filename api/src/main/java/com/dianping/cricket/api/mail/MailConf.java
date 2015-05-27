package com.dianping.cricket.api.mail;

import com.dianping.cricket.api.conf.GlobalConfiguration;

public class MailConf {
	private static MailConf conf = new MailConf();
	private String host;
	private int port;
	private String username;
	private String passwd;
	private boolean ssl;
	private String sender;
	static {
		// Make sure the global conf is initialized.
		GlobalConfiguration.getConf();
	}
	private MailConf() {}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public boolean isSsl() {
		return ssl;
	}
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public static MailConf getConf() {
		return conf;
	}
}
