package com.reqres.tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.reqres.response.model.ListUserResponsePojo;

public class ListUserTest extends BaseTest{

	static Logger log = Logger.getLogger(ListUserTest.class);
	private ListUserResponsePojo listUserResponsePojo;
	
	@BeforeMethod
	public void setUpEndPoint() {
		request.basePath("/api/users");
		
	}

	@Test
	public void listUserTest() {
		log.info("This is my first log4j's statement");
		SoftAssert softAssert = new SoftAssert();

		Map<String,Object> queryParam = new HashMap<String, Object>();
		queryParam.put("page", 2);
		request.queryParams(queryParam);
		response = request.get();
		softAssert.assertEquals(response.statusCode(),200);

		listUserResponsePojo = response.as(ListUserResponsePojo.class);
		softAssert.assertTrue(listUserResponsePojo.getSupport().getText().equals("To keep ReqRes free, contributions towards server costs are appreciated!"));
		softAssert.assertEquals(listUserResponsePojo.getTotal(),12);
		softAssert.assertEquals(listUserResponsePojo.getData().size(),6);
		softAssert.assertTrue(listUserResponsePojo.getData().get(0).getFirst_name().equals("Michael"));
		
		
//		System.out.println("All Headers of response are :- ");
//		Headers allHeaders = response.getHeaders();
//		for(Header header : allHeaders)
//		{
//			System.out.print(header.getName() +" : ");
//			System.out.println(header.getValue());
//		}
		boolean isPresent = response.getHeaders().hasHeaderWithName("Content-Type");
		softAssert.assertTrue(isPresent);
		
		softAssert.assertAll();
		log.info("This is my last log4j's statement");
	}

}
