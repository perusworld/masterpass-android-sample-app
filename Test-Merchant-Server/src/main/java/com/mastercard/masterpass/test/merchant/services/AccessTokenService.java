package com.mastercard.masterpass.test.merchant.services;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.mastercard.masterpass.test.merchant.controllers.AccessTokenController;
import com.mastercard.masterpass.test.merchant.messages.json.AccessTokenRequest;
import com.mastercard.masterpass.test.merchant.messages.json.AccessTokenResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

@Path("/accessToken")
public class AccessTokenService {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AccessTokenResponse getRequestToken(AccessTokenRequest accessTokenRequest) {
		// Delegate action to controller
		return new AccessTokenController(new Config(), new RestOpenAPIService()).getAccessToken(accessTokenRequest);
	}
	
}
