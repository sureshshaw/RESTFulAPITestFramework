package com.reqres.tests;

import org.testng.annotations.Test;

import com.reqres.request.model.LoginRequestPojo;
import com.reqres.response.model.Login200ResponsePojo;
import com.reqres.response.model.Login400ResponsePojo;
import com.reqres.uitilites.ReadProperties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class LoginTests extends BaseTest{

	private LoginRequestPojo loginRequestPojo;
	private Login200ResponsePojo login200ResponsePojo;
	private Login400ResponsePojo login400ResponsePojo;

	@Test
	public void loginSuccessTest() {

		loginRequestPojo = new LoginRequestPojo();

		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));
		loginRequestPojo.setPassword(ReadProperties.getValue("PASSWORD"));
		
		RestAssured.baseURI="https://reqres.in";
		RestAssured.basePath="/api/login";

		RequestSpecification request = RestAssured.given();
		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.request(Method.POST);

		softAssert.assertEquals(response.getStatusCode(), 200);

		login200ResponsePojo = response.as(Login200ResponsePojo.class);
		softAssert.assertNotNull(login200ResponsePojo.token);
		testMsg="Test Success";
	}

	@Test
	public void loginFailureTest() {
		loginRequestPojo = new LoginRequestPojo();
		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));
		
		RestAssured.baseURI="https://reqres.in";
		RestAssured.basePath="/api/login";

		RequestSpecification request = RestAssured.given();
		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.request(Method.POST);

		softAssert.assertEquals(response.getStatusCode(), 200);
		
		login400ResponsePojo = response.as(Login400ResponsePojo.class);
		softAssert.assertTrue(login400ResponsePojo.error.equals("Missing password"));
		
		testMsg ="Missing password";
		
	}

}
