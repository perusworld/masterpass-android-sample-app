package com.mastercard.masterpass.test.merchant.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.mastercard.masterpass.test.merchant.controllers.MerchantInitializationController;
import com.mastercard.masterpass.test.merchant.messages.json.MerchantInitializationRequest;
import com.mastercard.masterpass.test.merchant.messages.json.MerchantInitializationResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

@Path("/merchantInitialization")
public class MerchantInitializationService {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MerchantInitializationResponse getMerchantInitialization(MerchantInitializationRequest merchantInitializationRequest) {
		// Delegate action to controller
		return new MerchantInitializationController(new Config(), new RestOpenAPIService()).getMerchantInitialization(merchantInitializationRequest);
	}	
}
