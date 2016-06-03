package com.mastercard.masterpass.test.merchant.controllers;

import com.mastercard.masterpass.test.merchant.messages.json.AccessTokenResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.messages.json.AccessTokenRequest;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.OauthUtil;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.ServiceName;

/**
 * Controller for getting access token from Switch.
 * Make call to Switch
 */
public class AccessTokenController {

    // KEYS from config.properties file
    public static final String KEY_ACCESS_TOKEN_URL = ".access.token.url";
    public static final String KEY_CONTENT_TYPE_JSON = "content.type.json";

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public AccessTokenController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(AccessTokenController.class);

    /**
     * Passes request for session key signing to Switch
     *
     * @param accessTokenRequest request message from mobile app
     * @return response from Switch
     */
    public AccessTokenResponse getAccessToken(AccessTokenRequest accessTokenRequest) {
        logger.debug("getAccessToken Called");
        logger.debug(accessTokenRequest);

        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_ACCESS_TOKEN_URL);
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_JSON);

        // Set input params that are put in http header
        String oauthToken = accessTokenRequest.getOauthToken();
        String oauthVerifier = accessTokenRequest.getOauthVerifier();
        mRestOpenAPIService.setRequestToken(oauthToken);
        mRestOpenAPIService.setVerifierToken(oauthVerifier);

        // No input params for Switch = empty body
        String requestBody = null;
        // Create header for request to Switch
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.ACCESS_TOKEN);

        // Post request to Switch
        String serverResponse = mRestOpenAPIService.postService(requestHeader, requestBody, endpoint, contentType, String.class);
        logger.debug("getAccessToken response from server: " + serverResponse);

        // Extract access token which is in OAuth Token field
        String responseRaw = OauthUtil.extractOAuthToken(serverResponse);

        // Create response object
        AccessTokenResponse response = new AccessTokenResponse();
        response.setAccessToken(responseRaw);

        logger.debug(response);
        return response;
    }

}
