package com.dianping.cricket.mdx;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class Test {
	static interface i {
		public void get();
	}
	static class A {
		private String a;
		public void get() {};
	}
	static class B extends A implements i{
		private List<String> e;
		public B(int c) {}
		private String b;
		protected String c;
		public String d;
	}
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		for (Field f : B.class.getSuperclass().getDeclaredFields()) {
			System.out.println(f.getName());
		}
		
		
		System.out.println("hell{o}world".replaceAll("\\{o\\}", "xteng"));
		
		System.out.println(B.class.getDeclaredConstructor(int.class));
		
		System.out.println(((ParameterizedType)B.class.getDeclaredField("e").getGenericType()).getActualTypeArguments()[0]);
	}

}
