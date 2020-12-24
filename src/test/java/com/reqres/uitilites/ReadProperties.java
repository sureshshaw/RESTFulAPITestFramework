package com.reqres.uitilites;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	static InputStream fis;
	static Properties prop;
	public static String getValue(String key) {
		String value=null;
		try {
			fis = new FileInputStream("reqres.properties");
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(prop.containsKey(key)) {
			value=prop.getProperty(key);			
		}
		return value;
	}
	
}
