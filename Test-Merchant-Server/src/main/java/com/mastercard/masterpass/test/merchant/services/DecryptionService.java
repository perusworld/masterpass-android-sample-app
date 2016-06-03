package com.mastercard.masterpass.test.merchant.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.controllers.DecryptionController;
import com.mastercard.masterpass.test.merchant.messages.json.DecryptionRequest;
import com.mastercard.masterpass.test.merchant.messages.json.DecryptionResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;

@Path("/decrypt")
public class DecryptionService {
	
	private static final Logger logger = LogManager.getLogger(DecryptionService.class);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<DecryptionResponse> getDecryptedAdderess(List<DecryptionRequest> decryptionRequest){
		//Delegate action to controller
		return new DecryptionController(new Config(), new RestOpenAPIService()).getDecryptedAddress(decryptionRequest);
	}
}