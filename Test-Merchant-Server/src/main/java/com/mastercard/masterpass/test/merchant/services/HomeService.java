package com.mastercard.masterpass.test.merchant.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class HomeService {

	@GET
	public String getIndex() {
		return "Test Merchant Server";
	}

}
