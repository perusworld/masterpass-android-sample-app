package com.mastercard.masterpass.test.merchant.controllers;

import com.mastercard.masterpass.test.merchant.messages.json.CheckoutResourceResponse;
import com.mastercard.masterpass.test.merchant.messages.xml.*;
import com.mastercard.masterpass.test.merchant.util.XMLUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mastercard.masterpass.test.merchant.messages.json.CheckoutResourceRequest;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.ServiceName;


/**
 * Controller for getting session key signing from Switch.
 * Make call to Switch
 */
public class CheckoutResourceController {
    // KEYS from config.properties file
    public static final String KEY_CHECKOUT_RESOURCE_URL = ".checkout.resource.url";
    public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(CheckoutResourceController.class);

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public CheckoutResourceController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }


    /**
     * Passes request for session key signing to Switch
     *
     * @param checkoutResourceRequest request message from mobile app
     * @return response from Switch
     */
    public CheckoutResourceResponse getCheckoutResource(CheckoutResourceRequest checkoutResourceRequest) {
        logger.debug("getCheckoutResource Called");
        logger.debug(checkoutResourceRequest);

        String accessToken = checkoutResourceRequest.getAccessToken();
        String resourceId = checkoutResourceRequest.getCheckoutId();
        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_CHECKOUT_RESOURCE_URL) + "/" + resourceId + "?wallet=phw";
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);

        mRestOpenAPIService.setAccessToken(accessToken);

        // No input params for Switch = empty body
        String requestBody = null;
        // Create header for request to Switch
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.CHECKOUT_RESOURCE);

        // Post GET request to Switch as raw string. Save raw string response for passing back to mobile app
        String switchCheckoutResourceResponseRaw = mRestOpenAPIService.getService(requestHeader, requestBody, endpoint, contentType, String.class);
        logger.debug(switchCheckoutResourceResponseRaw);
        // Convert to XML
        SwitchCheckoutResourceResponse switchCheckoutResourceResponse = XMLUtil.XMLToObject(SwitchCheckoutResourceResponse.class, switchCheckoutResourceResponseRaw);
        logger.debug("getCheckoutResource response from server: transactionId=" + switchCheckoutResourceResponse.getTransactionId());

        // Convert back to JSON to return to mobile app
        CheckoutResourceResponse response = convertModel(switchCheckoutResourceResponse);
        // Set raw response for demo/debug purposes
        response.setRawSwitchResponse(switchCheckoutResourceResponseRaw);

        logger.debug(response);
        return response;
    }

    /**
     * Converts response from Switch to response to mobile app.
     *
     * @param switchCheckoutResourceResponse Switch response msg
     * @return response msg
     */
    private CheckoutResourceResponse convertModel(SwitchCheckoutResourceResponse switchCheckoutResourceResponse) {
        CheckoutResourceResponse response = new CheckoutResourceResponse();
        response.setCard(switchCheckoutResourceResponse.getCard());
        response.setTransactionId(switchCheckoutResourceResponse.getTransactionId());
        response.setWalletId(switchCheckoutResourceResponse.getWalletId());
        response.setAuthenticationOptions(switchCheckoutResourceResponse.getAuthenticationOptions());
        response.setContact(switchCheckoutResourceResponse.getContact());
        response.setShippingAddress(switchCheckoutResourceResponse.getShippingAddress());
        response.setExtensionPoint(switchCheckoutResourceResponse.getExtensionPoint());
        return response;
    }
}
