package com.mastercard.masterpass.test.merchant.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.ServiceName;
import com.mastercard.masterpass.test.merchant.messages.json.DecryptionRequest;
import com.mastercard.masterpass.test.merchant.messages.json.DecryptionResponse;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchDecryptionRequest;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchDecryptionResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.util.XMLUtil;

public class DecryptionController {

	// KEYS from config.properties file
	public static final String KEY_DECRYPTION_URL = ".decryption.url";
	public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";

	private static final Logger logger = LogManager.getLogger(DecryptionController.class);

	private Config mConfig;
	private RestOpenAPIService mRestOpenAPIService;

	/**
	 * @param mConfig
	 *            reference to helper for reading config file
	 * @param mRestOpenAPIService
	 *            reference to network layer helper
	 */
	public DecryptionController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
		super();
		this.mConfig = mConfig;
		this.mRestOpenAPIService = mRestOpenAPIService;
	}

	public List<DecryptionResponse> getDecryptedAddress(List<DecryptionRequest> decryptionRequest) {
		logger.debug("getDecryptedAddress Called");
		logger.debug(decryptionRequest);

		DecryptionResponse decryptionResponse = null;
		List<DecryptionResponse> decryptionResponseList = new ArrayList<>();

		// Get properties from config file
		String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_DECRYPTION_URL);
		String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);

		for (DecryptionRequest req : decryptionRequest) {
			// Create body for request to Switch - convert JSON msg from mobile
			// app to XML
			String requestBody = XMLUtil.ObjectToXML(SwitchDecryptionRequest.class, convertModel(req));
			// Create header for request to Switch
			String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.DECRYPTION);

			// Post request to Switch
			SwitchDecryptionResponse switchDecryptionResponse = mRestOpenAPIService.postService(requestHeader,requestBody, endpoint, contentType, SwitchDecryptionResponse.class);
			logger.debug("getDecryptedAddress response from server: data=" + switchDecryptionResponse.getData());

			// Convert back to JSON to return to mobile app
			decryptionResponse = convertModel(switchDecryptionResponse);
			decryptionResponseList.add(decryptionResponse);
		}
		logger.debug(decryptionResponseList);
		return decryptionResponseList;
	}

	/**
	 * Converts request from mobile app to request to Switch.
	 *
	 * @param decryptionRequest
	 *            request msg
	 * @return request msg to Switch
	 */
	private SwitchDecryptionRequest convertModel(DecryptionRequest decryptionRequest) {
		SwitchDecryptionRequest switchDecryptionRequest = new SwitchDecryptionRequest();
		switchDecryptionRequest.setData(decryptionRequest.getData());
		switchDecryptionRequest.setDataEncryptionKey(decryptionRequest.getDataEncryptionKey());
		switchDecryptionRequest.setDataEncryptionIV(decryptionRequest.getDataEncryptionIV());
		return switchDecryptionRequest;
	}

	/**
	 * Converts response from Switch to response to mobile app.
	 *
	 * @param switchDecryptionResponse
	 *            Switch response msg
	 * @return response msg
	 */
	private DecryptionResponse convertModel(SwitchDecryptionResponse switchDecryptionResponse) {
		DecryptionResponse decryptionResponse = new DecryptionResponse();
		decryptionResponse.setData(switchDecryptionResponse.getData());
		return decryptionResponse;
	}

}
