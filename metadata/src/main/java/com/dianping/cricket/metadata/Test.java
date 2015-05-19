package com.dianping.cricket.metadata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Test {
	public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, IOException {
		System.out.println(args.length);
		File file = new File("target/hive-test-1.0-SNAPSHOT.jar");
		System.out.println(file.toURI().toURL());
		URL[] urls = new URL[]{file.toURI().toURL()};
		ClassLoader loader = new URLClassLoader(urls);
		Class<?> cls = loader.loadClass("com.dianping.test.App");
		for (Method m : cls.getDeclaredMethods()) {
			System.out.println(m.getName());

		}
		
		Object obj = cls.newInstance();
		
		Method m = cls.getDeclaredMethod("main", String[].class);
		System.out.println(m.getName());
		m.invoke(obj, (Object)new String[]{});
		
		System.out.println(obj);
		JarFile jar = new JarFile(file, true);
		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				System.out.println(entry);
			}
		}
	}
	
}	
