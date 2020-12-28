package com.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.reqres.response.model.ListUserResponsePojo;
import com.reqres.uitilites.APIConstants;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ListUserTest extends BaseTest{

	private ListUserResponsePojo listUserResponsePojo;
	private RequestSpecification request;
	
	@BeforeClass
	public void setUpRequestSpec() {

		request = RestAssured.given();
		request.baseUri(APIConstants.BASEURI);
		request.basePath("/api/users");
	}

	@Test
	public void listUserTest() {

		Map<String,Object> queryParam = new HashMap<String, Object>();
		queryParam.put("page", 2);
		request.queryParams(queryParam);
		Response response = request.get();
		softAssert.assertEquals(response.statusCode(),200);

		listUserResponsePojo = response.as(ListUserResponsePojo.class);
		softAssert.assertTrue(listUserResponsePojo.support.text.equals("To keep ReqRes free, contributions towards server costs are appreciated!"));
		softAssert.assertEquals(listUserResponsePojo.total,12);
		softAssert.assertEquals(listUserResponsePojo.data.size(),6);
		softAssert.assertTrue(listUserResponsePojo.data.get(0).first_name.equals("Michael"));
		testMsg="Test Passed";
	}

}
