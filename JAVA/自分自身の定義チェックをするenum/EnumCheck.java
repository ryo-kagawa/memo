package com.example.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumCheck {
	YES("‚Í‚¢"),
	NO("‚¢‚¢‚¦"),
	YES2("‚Í‚¢")
	;
	private final int NUM;
	private final String VALUE;
	private static int num = 0;
	private static int numCount() {
		return num++;
	}
	static {
		List<Test> testList = new ArrayList<Test>(Arrays.asList(Test.values()));
		for(Test test1: Test.values()) {
			testList.remove(test1);
			for(Test test2: testList) {
				if(test1.VALUE.equals(test2.VALUE)) {
					throw new IllegalArgumentException(Test.class + "‚Ìˆø”‚ª•s³‚Å‚·");
				}
			}
		}
	}
	private Test(String value) {
		NUM = numCount();
		VALUE = value;
	}
	public int getNum() {
		return NUM;
	}
	public String getValue() {
		return VALUE;
	}
}
