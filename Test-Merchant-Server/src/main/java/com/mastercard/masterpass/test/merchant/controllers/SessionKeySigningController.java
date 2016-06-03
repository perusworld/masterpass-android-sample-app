package com.mastercard.masterpass.test.merchant.controllers;

import org.apache.log4j.*;

import com.mastercard.masterpass.test.merchant.messages.json.SessionKeySigningRequest;
import com.mastercard.masterpass.test.merchant.messages.json.SessionKeySigningResponse;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchSessionKeySigningRequest;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchSessionKeySigningResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.ServiceName;
import com.mastercard.masterpass.test.merchant.util.XMLUtil;

/**
 * Controller for getting session key signing from Switch.
 * Make call to Switch
 */
public class SessionKeySigningController {

    // KEYS from config.properties file
    public static final String KEY_SESSION_KEY_SIGNING_URL = ".session.key.signing.url";
    public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";

    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(SessionKeySigningController.class);

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public SessionKeySigningController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }

    /**
     * Passes request for session key signing to Switch
     *
     * @param sessionKeySigningRequest request message from mobile app
     * @return response from Switch
     */
    public SessionKeySigningResponse getSessionKeySigning(SessionKeySigningRequest sessionKeySigningRequest) {
        logger.debug("getSessionKeySigning Called");
        logger.debug(sessionKeySigningRequest);

        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_SESSION_KEY_SIGNING_URL);
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);


        // Create body for request to Switch - convert JSON msg from mobile app to XML
        String requestBody = XMLUtil.ObjectToXML(SwitchSessionKeySigningRequest.class, convertModel(sessionKeySigningRequest));
        // Create header for request to Switch
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.SESSION_KEY_SIGNING);

        // Post request to Switch
        SwitchSessionKeySigningResponse switchSessionKeySigningResponse = mRestOpenAPIService.postService(requestHeader, requestBody, endpoint, contentType, SwitchSessionKeySigningResponse.class);
        logger.debug("getSessionKeySigning response from server: signature=" + switchSessionKeySigningResponse.getSessionSignature());

        // Convert back to JSON to return to mobile app
        SessionKeySigningResponse response = convertModel(switchSessionKeySigningResponse);

        logger.debug(response);
        return response;
    }

    /**
     * Converts request from mobile app to request to Switch.
     *
     * @param sessionKeySigningRequest request msg
     * @return request msg to Switch
     */
    private SwitchSessionKeySigningRequest convertModel(SessionKeySigningRequest sessionKeySigningRequest) {
        SwitchSessionKeySigningRequest request = new SwitchSessionKeySigningRequest();
        request.setAppId(sessionKeySigningRequest.getAppId());
        request.setAppVersion(sessionKeySigningRequest.getAppVersion());
        request.setAppSigningPublicKey(sessionKeySigningRequest.getAppSigningPublicKey());
        return request;
    }

    /**
     * Converts response from Switch to response to mobile app.
     *
     * @param switchSessionKeySigningResponse Switch response msg
     * @return response msg
     */
    private SessionKeySigningResponse convertModel(SwitchSessionKeySigningResponse switchSessionKeySigningResponse) {
        SessionKeySigningResponse response = new SessionKeySigningResponse();
        response.setAppId(switchSessionKeySigningResponse.getAppId());
        response.setAppSigningPublicKey(switchSessionKeySigningResponse.getAppSigningPublicKey());
        response.setAppVersion(switchSessionKeySigningResponse.getAppVersion());
        response.setSessionSignature(switchSessionKeySigningResponse.getSessionSignature());
        return response;
    }
}
