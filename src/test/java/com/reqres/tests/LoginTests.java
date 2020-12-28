package com.reqres.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;
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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.module.jsv.JsonSchemaValidator;

public class LoginTests extends BaseTest{

	private LoginRequestPojo loginRequestPojo;
	//private LoginRequestPojo loginRequestPojo1;
	private Login200ResponsePojo login200ResponsePojo;
	private Login400ResponsePojo login400ResponsePojo;
	private RequestSpecification request;

	@BeforeClass
	public void setUpRequestSpec() {

		request = RestAssured.given();
		request.baseUri(APIConstants.BASEURI);
		request.basePath("/api/login");
	}

	@Test
	public void loginSuccessTest() {

		loginRequestPojo = new LoginRequestPojo();
		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));
		loginRequestPojo.setPassword(ReadProperties.getValue("PASSWORD"));


		//		loginRequestPojo1 = new LoginRequestPojo();
		//		loginRequestPojo1.setEmail(ReadProperties.getValue("EMAIL"));
		//		loginRequestPojo1.setPassword(ReadProperties.getValue("PASSWORD"));

		//		List<LoginRequestPojo> all = new ArrayList<LoginRequestPojo>();
		//		all.add(loginRequestPojo);
		//		all.add(loginRequestPojo1);

		//		request.body(all);

		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.post();

		softAssert.assertEquals(response.getStatusCode(), 200);

		login200ResponsePojo = response.as(Login200ResponsePojo.class);
		softAssert.assertNotNull(login200ResponsePojo.token);// verifying that "token" field of response json exists
		testMsg="Test Success";
	}

	@Test
	public void loginFailureTest() {
		//		RestAssured
		//		.given()
		//		// Logging all details
		//		.log()
		//		.all()
		//		.when()
		//		.get();

		loginRequestPojo = new LoginRequestPojo();
		loginRequestPojo.setEmail(ReadProperties.getValue("EMAIL"));

		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		Response response = request.post();

		softAssert.assertEquals(response.getStatusCode(), 200);

		login400ResponsePojo = response.as(Login400ResponsePojo.class);
		softAssert.assertTrue(login400ResponsePojo.error.equals("Missing password"));
		softAssert.assertNotNull(login400ResponsePojo.error);// checking that value of error key/property must exists  

		//validating response JSON schema so validating that all keys must be present in response json 
		softAssert.assertTrue(response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("responseJSONSchema/loginUnsucessfull400Schema.json"))).toString().startsWith("io.restassured.internal.ValidatableResponseImpl"));

		testMsg ="Missing password";

		// Another way to validate all the presence of all keys/properties
		List<String> expectedKey = new ArrayList<String>();
		expectedKey.add("error");

		List<String> actualKey = new ArrayList<String>();

		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode node = om.readTree(response.asString());
			Iterator<String> allNodes = node.fieldNames();
			while(allNodes.hasNext()) {
				actualKey.add(allNodes.next());
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		softAssert.assertEqualsNoOrder(expectedKey.toArray(), actualKey.toArray());

		//saving the response json as failure proof as we do in ui automation by taking screenshot of page where bug is found. 
		//This saved json can be used as request payload for any other API
		FileWriter writer = null;
		try {
			writer = new FileWriter("responseJSON/loginUnsucessfull400.json");
			writer.write(response.asPrettyString());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
