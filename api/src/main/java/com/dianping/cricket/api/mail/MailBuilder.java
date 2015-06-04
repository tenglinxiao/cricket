package com.dianping.cricket.api.mail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.dianping.cricket.api.Builder;

public class MailBuilder implements Builder<Email> {
	private Email email = new HtmlEmail();
	private MailBuilder() {}
	public MailBuilder subject(String subject) {
		email.setSubject(subject);
		return this;
	}
	
	public MailBuilder recipient(String recipient) {
		List<InternetAddress> addresses = email.getToAddresses();
		try {
			if (addresses == null) {
				addresses = new ArrayList<InternetAddress>();
				addresses.add(new InternetAddress(recipient));
				email.setTo(addresses);
			} else {
				addresses.add(new InternetAddress(recipient));
			}
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public MailBuilder body(String id, Object data) {
		try {
			email.setMsg(MailTemplateLoader.applyTemplate(id, data));
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Email build() {
		try {
			MailConf conf = MailConf.getConf();
			email.setHostName(conf.getHost());
			email.setSmtpPort(conf.getPort());
			email.setAuthenticator(new DefaultAuthenticator(conf.getUsername(), conf.getPasswd()));
			email.setSSLOnConnect(conf.isSsl());
			email.setFrom(conf.getSender());
			return email;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static MailBuilder newBuilder() {
		return new MailBuilder();
	}
	
	
	public static void main(String args[]) throws EmailException {
		MailBuilder.newBuilder().subject("test").recipient("tenglinxiao@hotmail.com").body("job_success", null).build().send();
	}
}
