package com.dianping.cricket.scheduler.job;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.quartz.JobExecutionException;

import com.dianping.cricket.scheduler.SchedulerConf;

public class Test {
	public Test() {}
	
	public static void main(String args[]) throws IOException, InterruptedException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, JobExecutionException {
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
//		SchedulerLoader loader = SchedulerLoader.getLoader();
//		for (int index = 0; index < Integer.MAX_VALUE; index++) {
//			loader.logStatus(new JobStatus(1));
//		}
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(Paths.get("").toAbsolutePath());
//		Path path = Paths.get("").toAbsolutePath();
//		path = path.resolve(Paths.get("src/main/webapp/bin/run.sh"));
//		System.out.println(path);
//		ProcessBuilder builder = new ProcessBuilder("/Users/tenglinxiao/Documents/cricket/scheduler/src/main/webapp/bin/run.sh","test.sh");
//		Process process = builder.start();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
//		}
//		System.out.println(process.waitFor());
//		reader.close();
		System.out.println(Test.class.getDeclaredConstructor());
		
		File jar = new File("/Users/tenglinxiao/Documents/cricket/scheduler/src/main/webapp/jobs/hive-test-1.0-SNAPSHOT.jar");
		ClassLoader loader = Test.class.getClassLoader();
		URLClassLoader l = new URLClassLoader(new URL[]{jar.toURL()}, loader);
		Class cls = l.loadClass("com.dianping.cricket.scheduler.job.TestJob");
		Job job = (Job)cls.newInstance();
		job.start(null);
		
		System.out.println(l.loadClass("com.dianping.cricket.scheduler.job.TestJob"));
		String mainEntry = "test";
		Path path = Paths.get("").toAbsolutePath();
		path = path.resolve(Paths.get(SchedulerConf.getConf().getJobJars()));
		path = path.resolve(Paths.get(mainEntry));
		System.out.println(path);
		JarFile file = new JarFile(jar);
		Enumeration<JarEntry> entries = file.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				System.out.println(entry.getName().replaceAll("/", "."));
			}
		}
		
	}

}
