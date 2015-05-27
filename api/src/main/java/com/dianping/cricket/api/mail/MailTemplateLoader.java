package com.dianping.cricket.api.mail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class MailTemplateLoader {
	private static Logger logger = Logger.getLogger(MailTemplateLoader.class);
	private static final String MAIL_TEMPLATE = "classpath*:/mail/*.ftl";
	private static MailTemplateLoader loader = new MailTemplateLoader();
	private static Configuration conf = new Configuration(Configuration.VERSION_2_3_22);
	
	static {
		loader.init();
	}
	
	private MailTemplateLoader() {}
	
	private void init() {
		try {
			StringTemplateLoader loader = new StringTemplateLoader();
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			
			// Find all the mail templates.
			Resource[] resources = resolver.getResources(MAIL_TEMPLATE);
			for (Resource resource : resources) {
				// Create reader to read the template contents.
				BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
				String line = null;
				StringBuilder builder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
				
				// Get pure file name without file extension.
				String fileName = resource.getFilename().split("\\.")[0];
				
				// Store the template in filename -> content pair.
				loader.putTemplate(fileName, builder.toString());
				
				logger.info("Load mail template: " + resource);
			}
			
			conf.setTemplateLoader(loader);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal("Mail tempalte loading error: [" + e.getMessage() + "]");
			System.exit(1);
		}
	}
	
	public static String applyTemplate(String id, Object data) {
		Template template = null;
		try {
			template = conf.getTemplate(id);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(out);
			template.process(data, writer);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed to apply data to template with id: [" + id + "]");
			return null;
		} 
	}
	
	
	public static Template findTemplate(String id) {
		try {
			return conf.getTemplate(id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Failed to find mail template with id: [" + id + "]");
			return null;
		}
	}

}
