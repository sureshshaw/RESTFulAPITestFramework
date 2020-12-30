package com.reqres.tests;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.reqres.utilities.APIConstants;
import com.reqres.utilities.Helper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

	public static ExtentSparkReporter reporter;      //this must be static
	public static ExtentReports report;              //this must be static
	public static ExtentTest test;                   //this must be static

	public static String testMsg;
	public static RequestSpecification request;
	public static Response response;
	
	@BeforeSuite
	public void setUpTestSuite() {
		report = new ExtentReports();
		reporter = new ExtentSparkReporter("Report.html");
		reporter.config().setDocumentTitle("Reqres Rest API Test");
		reporter.config().setReportName("Reqres Rest API Test Report");
		report.attachReporter(reporter);
	}

	@BeforeMethod
	void initilizeTest() {
		request = RestAssured.given();
		request.baseUri(APIConstants.BASEURI);
		response = null;
		testMsg = "";
	}

	@AfterMethod
	public void generateReport(ITestResult result) {
		test = report.createTest(result.getName());
		
		if(result.isSuccess())
			test.pass(testMsg);
		else {
			test.fail(result.getThrowable().getMessage());
			test.fail("<a href='" + Helper.getResponseFilePath(result.getName(), response) + "'>Response JSON</a>");
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
