package com.mastercard.masterpass.test.merchant.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.mastercard.masterpass.test.merchant.controllers.CheckoutResourceController;
import com.mastercard.masterpass.test.merchant.messages.json.CheckoutResourceRequest;
import com.mastercard.masterpass.test.merchant.messages.json.CheckoutResourceResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

@Path("/checkoutResource")
public class CheckoutResourceService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CheckoutResourceResponse getCheckoutResource(CheckoutResourceRequest checkoutResourceRequest) {
		// Delegate action to controller
		return new CheckoutResourceController(new Config(), new RestOpenAPIService()).getCheckoutResource(checkoutResourceRequest);
	}
}