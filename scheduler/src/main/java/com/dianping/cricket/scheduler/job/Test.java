package com.dianping.cricket.scheduler.job;

import java.io.IOException;
import java.nio.file.Paths;

import javax.ws.rs.core.MediaType;

import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.dianping.cricket.scheduler.rest.util.JobUtil;

public class Test {
	public Test() {}
	
	public void getCurrentFunction() {
		StackTraceElement element = Thread.currentThread().getStackTrace()[1];
	}
	
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
//		final Process process = builder.start();
//		// Set up monitor for service interruption.
//		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//		
//		// Schedule monitor thread to check interruption update every 5 seconds.
//		service.scheduleAtFixedRate(new Runnable() {
//			public void run() {
//				process.destroy();
//			}
//		}, 5, 5, TimeUnit.SECONDS);
//		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
//		}
//		System.out.println(process.waitFor());
//		reader.close();
//		service.shutdownNow();
//		System.out.println(Test.class.getDeclaredConstructor());
		
//		File jar = new File("/Users/tenglinxiao/Documents/cricket/scheduler/src/main/webapp/jobs/hive-test-1.0-SNAPSHOT.jar");
//		ClassLoader loader = Test.class.getClassLoader();
//		URLClassLoader l = new URLClassLoader(new URL[]{jar.toURL()}, loader);
//		Class cls = l.loadClass("com.dianping.cricket.scheduler.job.TestJob");
//		Job job = (Job)cls.newInstance();
//		job.start(null);
//		
//		System.out.println(l.loadClass("com.dianping.cricket.scheduler.job.TestJob"));
//		String mainEntry = "test";
//		Path path = Paths.get("").toAbsolutePath();
//		path = path.resolve(Paths.get(SchedulerConf.getConf().getJobJars()));
//		path = path.resolve(Paths.get(mainEntry));
//		System.out.println(path);
//		JarFile file = new JarFile(jar);
//		Enumeration<JarEntry> entries = file.entries();
//		while (entries.hasMoreElements()) {
//			JarEntry entry = entries.nextElement();
//			if (!entry.isDirectory()) {
//				System.out.println(entry.getName().replaceAll("/", "."));
//			}
//		}
		new Test().getCurrentFunction();
		System.out.println(MediaType.APPLICATION_JSON);
		String fileNameWithoutEx = "test3[23]";
		//System.out.println(JobUtil.appendTimestamp(Paths.get("test.append.jar")));
		
		System.out.println(new JobKey(""));
		
		System.out.println(Paths.get("").toAbsolutePath());
	}

}
