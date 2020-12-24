package com.reqres.response.model;

import java.util.List;

public class ListUserResponsePojo{
	
	public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public List<Datum> data;
    public Support support;
    
}
