package com.mastercard.masterpass.test.merchant.services;

import com.mastercard.masterpass.test.merchant.controllers.PostbackController;
import com.mastercard.masterpass.test.merchant.controllers.RequestTokenController;
import com.mastercard.masterpass.test.merchant.messages.json.PostbackRequest;
import com.mastercard.masterpass.test.merchant.messages.json.PostbackResponse;
import com.mastercard.masterpass.test.merchant.messages.json.RequestTokenResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/postback")
public class PostbackService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PostbackResponse getPostback(PostbackRequest postbackRequest) {
		// Delegate action to controller
		return new PostbackController(new Config(), new RestOpenAPIService()).getPostback(postbackRequest);
	}
}
