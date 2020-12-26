package com.reqres.tests;

import java.io.File;
import java.util.Iterator;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reqres.request.model.LoginRequestPojo;
import com.reqres.response.model.Login200ResponsePojo;
import com.reqres.response.model.Login400ResponsePojo;
import com.reqres.uitilites.APIConstants;
import com.reqres.uitilites.ReadProperties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.module.jsv.JsonSchemaValidator;

public class LoginTests extends BaseTest{

	private LoginRequestPojo loginRequestPojo;
	private Login200ResponsePojo login200ResponsePojo;
	private Login400ResponsePojo login400ResponsePojo;

	@Test
	public void loginSuccessTest() {

		loginRequestPojo = new LoginRequestPojo();

		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));
		loginRequestPojo.setPassword(ReadProperties.getValue("PASSWORD"));
		
		RestAssured.baseURI=APIConstants.BASEURI;
		RestAssured.basePath="/api/login";

		RequestSpecification request = RestAssured.given();
		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.request(Method.POST);

		softAssert.assertEquals(response.getStatusCode(), 200);

		login200ResponsePojo = response.as(Login200ResponsePojo.class);
		softAssert.assertNotNull(login200ResponsePojo.token);// verifying that "token" field of response json exists
		testMsg="Test Success";
	}

	@Test
	public void loginFailureTest() {
		loginRequestPojo = new LoginRequestPojo();
		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));
		
		RestAssured.baseURI=APIConstants.BASEURI;
		RestAssured.basePath="/api/login";

		RequestSpecification request = RestAssured.given();
		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.request(Method.POST);

		softAssert.assertEquals(response.getStatusCode(), 200);
		
		login400ResponsePojo = response.as(Login400ResponsePojo.class);
		softAssert.assertTrue(login400ResponsePojo.error.equals("Missing password"));
		softAssert.assertNotNull(login400ResponsePojo.error);// checking that error property/key of response json must exists

		softAssert.assertTrue(response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("responseJSON/loginUnsucessfull400.json"))).toString().startsWith("io.restassured.internal.ValidatableResponseImpl"));
				
		//System.out.println(login400ResponsePojo.error1);
		testMsg ="Missing password";
		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode node = om.readTree(response.asString());
			Iterator<String> allNodes = node.fieldNames();
			allNodes.forEachRemaining(k -> {
				System.out.println(k);
			});
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
