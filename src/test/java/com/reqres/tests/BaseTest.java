package com.reqres.tests;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseTest {

	public static ExtentSparkReporter reporter;      //this must be static
	public static ExtentReports report;              //this must be static
	public static ExtentTest test;                   //this must be static

	SoftAssert softAssert;
	public static String testMsg;

	@BeforeSuite
	public void setUpTestSuite() {
		report = new ExtentReports();
		reporter = new ExtentSparkReporter("Report.html");
		reporter.config().setDocumentTitle("Reqres Rest API Test");
		reporter.config().setReportName("Reqres Rest API Test Report");
		report.attachReporter(reporter);
	}

	@BeforeMethod
	void initilizeAssert() {
		testMsg="";
		softAssert = new SoftAssert();
	}

	@AfterMethod
	public void generateReport(ITestResult result) {
		test = report.createTest(result.getName());

		try {
			softAssert.assertAll();
		}catch(AssertionError e) {
			result.setStatus(2);
		}
		
		if(result.isSuccess())
			test.pass(testMsg);
		else {
			test.fail(testMsg);
			test.fail(result.getThrowable());
		}
	}

	@AfterSuite
	public void launchReport() {
		report.flush();
		File htmlFile = new File("Report.html");
		try {
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
