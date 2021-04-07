package com.reqres.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.reqres.request.model.LoginRequestPojo;
import com.reqres.response.model.Login200ResponsePojo;
import com.reqres.response.model.Login400ResponsePojo;
import com.reqres.utilities.Helper;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

public class LoginTests extends BaseTest{

	private LoginRequestPojo loginRequestPojo;
	private Login200ResponsePojo login200ResponsePojo;
	private Login400ResponsePojo login400ResponsePojo;
	
	@BeforeClass
	public void initializePOJOs() {
		loginRequestPojo = new LoginRequestPojo();
	}

	@BeforeMethod
	public void setUpEndPoint() {
		request.basePath("/api/login");
	}
	
	@Test
	public void loginSuccessTest() {
		//We should instantiate a SoftAssert object within a @Test method. Scope of SoftAssert should only be within the Test method.
		//We should never use the same Soft Assertions with multiple test cases.

		SoftAssert softAssert = new SoftAssert();
		
		loginRequestPojo.setEmail(Helper.getValue("EMAIL"));
		loginRequestPojo.setPassword(Helper.getValue("PASSWORD"));

		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		response = request.post();

		softAssert.assertEquals(response.getStatusCode(), 200);

		login200ResponsePojo = response.as(Login200ResponsePojo.class);
		softAssert.assertNotNull(login200ResponsePojo.getToken());
		
		softAssert.assertAll();
	}

	@Test
	public void loginFailureTest() throws JsonMappingException, JsonProcessingException {
		SoftAssert softAssert = new SoftAssert();
		
		loginRequestPojo.setEmail(Helper.getValue("EMAIL"));

		request.body(loginRequestPojo);
		request.contentType(ContentType.JSON);
		response = request.post();

		softAssert.assertEquals(response.getStatusCode(), 200);

		login400ResponsePojo = response.as(Login400ResponsePojo.class);
		softAssert.assertTrue(login400ResponsePojo.getError().equals("Missing password"));
		softAssert.assertNotNull(login400ResponsePojo.getError());// checking that value of error key/property must exists  

		//validating response JSON schema so validating that all keys must be present in response json 
		softAssert.assertTrue(response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("responseJSONSchema/loginUnsucessfull400Schema.json"))).toString().startsWith("io.restassured.internal.ValidatableResponseImpl"));

		// Another way to validate all the presence of all keys/properties
		List<String> expectedKey = new ArrayList<String>();
		expectedKey.add("error");

		List<String> actualKey = new ArrayList<String>();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode parsedJsonObject = objectMapper.readTree(response.asString());
		Iterator<String> allKeys= parsedJsonObject.fieldNames();

		allKeys.forEachRemaining(key -> {
			Object value = parsedJsonObject.get(key);
			// TextNode can be related to String from previous example
			// You may need to add IntNode or BooleanNode for different types of values
			if(value instanceof TextNode)
				actualKey.add(key);
			// ObjectNode can be related to LinkedHashMap from previous example
			else if(value instanceof ObjectNode)
			{
				Iterator<String> nestedKey = ((ObjectNode)value).fieldNames();
				
				nestedKey.forEachRemaining(nKey -> 
					actualKey.add(key)
				);
			}
		});

		softAssert.assertEqualsNoOrder(expectedKey.toArray(), actualKey.toArray());

		softAssert.assertAll();
	}

}
