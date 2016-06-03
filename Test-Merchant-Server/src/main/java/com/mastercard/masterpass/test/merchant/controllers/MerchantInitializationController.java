package com.mastercard.masterpass.test.merchant.controllers;

import com.mastercard.masterpass.test.merchant.messages.json.MerchantInitializationResponse;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchMerchantInitializationResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.messages.json.MerchantInitializationRequest;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchMerchantInitializationRequest;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.ServiceName;
import com.mastercard.masterpass.test.merchant.util.XMLUtil;

/**
 * Controller for initialization of merchant with Switch.
 * Make call to Switch
 */
public class MerchantInitializationController {

    // KEYS from config.properties file
    public static final String KEY_MERCHANT_INIT_URL = ".merchant.init.url";
    public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(MerchantInitializationController.class);

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public MerchantInitializationController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }

    /**
     * Passes request for mechant initialization to Switch
     *
     * @param merchantInitializationRequest request message from mobile app
     * @return response from Switch
     */
    public MerchantInitializationResponse getMerchantInitialization(MerchantInitializationRequest merchantInitializationRequest) {
        logger.debug("getMerchantInitialization Called");
        logger.debug(merchantInitializationRequest);

        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_MERCHANT_INIT_URL);
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);


        // Create body for request to Switch - convert JSON msg from mobile app to XML
        String requestBody = XMLUtil.ObjectToXML(SwitchMerchantInitializationRequest.class, convertModel(merchantInitializationRequest));
        // Create header for request to Switch
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.MERCHANT_INITIALIZATION);

        // Post request to Switch
        SwitchMerchantInitializationResponse switchMerchantInitializationResponse = mRestOpenAPIService.postService(requestHeader, requestBody, endpoint, contentType, SwitchMerchantInitializationResponse.class);
        logger.debug("getMerchantInitialization response from server: oAuthToken=" + switchMerchantInitializationResponse.getoAuthToken());//OAuthToken=requestToken

        // Convert back to JSON to return to mobile app
        MerchantInitializationResponse response = convertModel(switchMerchantInitializationResponse);

        logger.debug(response);
        return response;
    }

    /**
     * Converts request from mobile app to request to Switch.
     *
     * @param merchantInitializationRequest request msg
     * @return request msg to Switch
     */
    private SwitchMerchantInitializationRequest convertModel(MerchantInitializationRequest merchantInitializationRequest) {
        SwitchMerchantInitializationRequest request = new SwitchMerchantInitializationRequest();
        request.setoAuthToken(merchantInitializationRequest.getRequestToken());
        request.setOriginUrl(merchantInitializationRequest.getOriginUrl());
        request.setExtensionPointMerchantInitializationRequest(merchantInitializationRequest.getExtensionPoint());
        return request;
    }

    /**
     * Converts response from Switch to response to mobile app.
     *
     * @param switchMerchantInitializationResponse Switch response msg
     * @return response msg
     */
    private MerchantInitializationResponse convertModel(SwitchMerchantInitializationResponse switchMerchantInitializationResponse) {
        MerchantInitializationResponse response = new MerchantInitializationResponse();
        response.setRequestToken(switchMerchantInitializationResponse.getoAuthToken());
        response.setExtensionPoint(switchMerchantInitializationResponse.getExtensionPoint());
        return response;
    }
}
