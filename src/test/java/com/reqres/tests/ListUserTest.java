package com.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.reqres.response.model.ListUserResponsePojo;
import com.reqres.uitilites.APIConstants;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ListUserTest extends BaseTest{
	
	ListUserResponsePojo listUserResponsePojo;
	
	@Test
	public void listUserTest() {
		RestAssured.baseURI=APIConstants.BASEURI;
		RestAssured.basePath="/api/users";
		
		RequestSpecification request = RestAssured.given();
		Map<String,Object> queryParam = new HashMap<String, Object>();
		queryParam.put("page", 2);
		request.queryParams(queryParam);
		Response response = request.request(Method.GET);
		softAssert.assertEquals(response.statusCode(),200);
		
		listUserResponsePojo = response.as(ListUserResponsePojo.class);
		softAssert.assertTrue(listUserResponsePojo.support.text.equals("To keep ReqRes free, contributions towards server costs are appreciated!"));
		softAssert.assertEquals(listUserResponsePojo.total,12);
		softAssert.assertEquals(listUserResponsePojo.data.size(),6);
		softAssert.assertTrue(listUserResponsePojo.data.get(0).first_name.equals("Michael"));
		testMsg="Test Passed";
	}

}
