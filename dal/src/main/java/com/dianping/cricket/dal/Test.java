package com.dianping.cricket.dal;

import java.util.AbstractList;
import java.util.ArrayList;

import sun.reflect.Reflection;

public class Test extends AbstractPersistable {
	static class A {
		private String a;
	}
	static class B extends A {
		private String a;
		public void test() {
			say();
		}
		public void say() {
//			StackTraceElement[] elements = Thread.currentThread().getStackTrace();
//			for (StackTraceElement elem : elements) {
//				System.out.println(elem);
//			}
			System.out.println(Thread.currentThread().getStackTrace()[2].getClass());
		}
	}
	public static void main(String args[]) throws NoSuchFieldException, SecurityException {
		System.out.println(B.class.getDeclaredField("a").getType());
		System.out.println(AbstractList.class.isAssignableFrom(ArrayList.class));
		new B().test();

	}

}
