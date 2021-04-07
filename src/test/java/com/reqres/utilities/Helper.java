package com.reqres.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;

public class Helper {

	static InputStream fis;
	static Properties prop;
	
	public static void loadPropertyFile() {
		try {
			fis = new FileInputStream("reqres.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key) {
		String value=null;
		if(prop.containsKey(key)) {
			value=prop.getProperty(key);			
		}
		return value;
	}

	public static boolean isKeyPresent(Response response, String key) {
		boolean flag = false;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode parsedJsonObject = null;
		try {
			parsedJsonObject = objectMapper.readTree(response.asString());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Iterator<String> allKeys= parsedJsonObject.fieldNames();
		while(allKeys.hasNext()) {
			Object value = parsedJsonObject.get(key);
			if(allKeys.next().equals(key)) {
				flag = true;
				break;
			}
			else if(value instanceof ObjectNode) {
				Iterator<String> nestedKey = ((ObjectNode)value).fieldNames();
				while(nestedKey.hasNext()) {
					if(nestedKey.next().equals(key)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}
	
	
	//saving the response json as failure proof as we do in ui automation by taking screenshot of page where bug is found. 
	//This saved json can be used as request payload for any other API
	public static String getResponseFilePath(String fileName, Response response) {
		FileWriter writer = null;
		String filePath = "responseJSON/" + fileName + ".json";
		try {
			writer = new FileWriter(filePath);
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
		
		return filePath;
	}

}
