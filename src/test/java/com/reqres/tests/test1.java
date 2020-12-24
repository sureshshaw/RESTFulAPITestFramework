package com.reqres.tests;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class test1 {

	public static void main(String[] args) {

		SoftAssert sa = new SoftAssert();
		sa.assertTrue(false);
		sa.assertAll();
		Assert.assertTrue(false);
		
	}

}
