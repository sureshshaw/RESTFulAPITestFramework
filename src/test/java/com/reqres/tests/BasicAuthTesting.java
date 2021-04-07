package com.reqres.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BasicAuthTesting {

	//http://demo.guru99.com/V4/sinkministatement.php?CUSTOMER_ID=68195&PASSWORD=1234!&Account_No=1
	
	public static void main(String[] args) {
	RequestSpecification request = RestAssured.given();
	request.baseUri("https://the-internet.herokuapp.com");
	request.basePath("/basic_auth");
	request.auth().basic("admin", "admin");
	Response response = request.get();
	
	System.out.println(response.getStatusCode());
	System.out.println("=============== Headers ==========");
	System.out.println(response.getHeaders());
	System.out.println("=============== Cookies ==========");
	System.out.println(response.getCookies());

	}

}
