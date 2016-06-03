package com.mastercard.masterpass.test.merchant.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.mastercard.masterpass.test.merchant.controllers.RequestTokenController;
import com.mastercard.masterpass.test.merchant.messages.json.RequestTokenResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

@Path("/requestToken")
public class RequestTokenService {
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public RequestTokenResponse getRequestToken() {
		// Delegate action to controller
		return new RequestTokenController(new Config(), new RestOpenAPIService()).getRequestToken();
	}
}
