package com.mastercard.masterpass.test.merchant.controllers;

import com.mastercard.masterpass.test.merchant.messages.json.RequestTokenResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.OauthUtil;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.ServiceName;

/**
 * Controller for getting request token from Switch.
 * Make call to Switch
 */
public class RequestTokenController {

    // KEYS from config.properties file
    public static final String KEY_REQUEST_TOKEN_URL = ".request.token.url";
    public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(RequestTokenController.class);

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public RequestTokenController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }

    /**
     * Passes request for requestToken to Switch
     *
     * @return oAuthToken in response object containing requestToken
     */
    public RequestTokenResponse getRequestToken() {
        logger.debug("getRequestToken Called");


        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_REQUEST_TOKEN_URL);
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);


        // No input params for Switch = empty body
        String requestBody = null;
        // Create header for request to Switch
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.REQUEST_TOKEN);

        // Post request to Switch
        String serverResponse = mRestOpenAPIService.postService(requestHeader, requestBody, endpoint, contentType, String.class);
        logger.debug("getRequestToken response from server: " + serverResponse);

        // Extract request token which is in OAuth Token field
        String responseRaw = OauthUtil.extractOAuthToken(serverResponse);

        // Create response object
        RequestTokenResponse response = new RequestTokenResponse();
        response.setRequestToken(responseRaw);

        logger.debug(response);
        return response;
    }
}
