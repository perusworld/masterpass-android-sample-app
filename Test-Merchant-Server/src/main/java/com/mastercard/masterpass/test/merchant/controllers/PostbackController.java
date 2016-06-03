package com.mastercard.masterpass.test.merchant.controllers;

import com.mastercard.masterpass.test.merchant.ServiceName;
import com.mastercard.masterpass.test.merchant.messages.json.PostbackRequest;
import com.mastercard.masterpass.test.merchant.messages.json.PostbackResponse;
import com.mastercard.masterpass.test.merchant.messages.xml.MerchantTransactions;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchPostbackRequest;
import com.mastercard.masterpass.test.merchant.messages.xml.SwitchPostbackResponse;
import com.mastercard.masterpass.test.merchant.util.Config;
import com.mastercard.masterpass.test.merchant.util.RestOpenAPIService;
import com.mastercard.masterpass.test.merchant.util.XMLUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for sending Postback to Switch. It passes transaction amount, currency, purchase date and status.
 * Make call to Switch
 */
public class PostbackController {
    // KEYS from config.properties file
    public static final String KEY_POSTBACK_URL = ".postback.url";
    public static final String KEY_CONTENT_TYPE_XML = "content.type.xml";
    public static final String KEY_CONSTANTS_APPROVAL_CODE = "constants.approvalcode";
    public static final String KEY_CONSUMER_KEY = RestOpenAPIService.KEY_CONSUMER_KEY;
    /**
     * File/Console logger
     */
    private static final Logger logger = LogManager.getLogger(PostbackController.class);

    private Config mConfig;
    private RestOpenAPIService mRestOpenAPIService;

    /**
     * Ctor
     *
     * @param mConfig             reference to helper for reading config file
     * @param mRestOpenAPIService reference to network layer helper
     */
    public PostbackController(Config mConfig, RestOpenAPIService mRestOpenAPIService) {
        this.mConfig = mConfig;
        this.mRestOpenAPIService = mRestOpenAPIService;
    }

    /**
     * Passes Postback data to Switch
     *
     * @param postbackRequest request message from mobile app
     * @return response with result
     */
    public PostbackResponse getPostback(PostbackRequest postbackRequest) {
        logger.debug("getPostback Called");
        logger.debug(postbackRequest);

        // Get properties from config file
        String endpoint = mConfig.readProperties().getProperty(Config.activeProfile + KEY_POSTBACK_URL);
        String contentType = mConfig.readProperties().getProperty(KEY_CONTENT_TYPE_XML);
        String consumerKey = mConfig.readProperties().getProperty(Config.activeProfile + KEY_CONSUMER_KEY);
        String approvalCode = mConfig.readProperties().getProperty(KEY_CONSTANTS_APPROVAL_CODE);

        // Prepare msg body (convert request to XML)
        String requestBody = XMLUtil.ObjectToXML(SwitchPostbackRequest.class, convertModel(postbackRequest, approvalCode, consumerKey));

        // Prepare msg header
        String requestHeader = mRestOpenAPIService.createOAuthHeader(requestBody, endpoint, ServiceName.POSTBACK);

        // Make a call to Postback endpoint on Switch
        String switchPostbackResponseRaw = mRestOpenAPIService.postService(requestHeader, requestBody, endpoint, contentType, String.class);
        // We cannot use openAPIService to deserialize to SwitchPostbackResponse directly (via ClientResponse)
        // because root element and first child have the same xml tag but different model class. That is why
        // we are using our own JAXB unmarshaller
        SwitchPostbackResponse switchPostbackResponse = XMLUtil.XMLToObject(SwitchPostbackResponse.class, switchPostbackResponseRaw);
        logger.debug(switchPostbackResponse);

        Boolean areObjectsTheSame = false;
        // Check if we received response for our transaction
        if (switchPostbackResponse.getMerchantTransactions() != null && switchPostbackResponse.getMerchantTransactions().size() > 0) {
            logger.debug("First Transaction id=" + switchPostbackResponse.getMerchantTransactions().get(0).getTransactionId());
            // Compare only transaction id for simplicity
            if (switchPostbackResponse.getMerchantTransactions().get(0).getTransactionId().equalsIgnoreCase(postbackRequest.getTransactionId())) {
                areObjectsTheSame = true;
            }
        } else {
            logger.debug("No transactions");
        }

        // Build Response to mobile app
        PostbackResponse response = new PostbackResponse();
        response.setIsSuccess(areObjectsTheSame);
        logger.debug(response);
        return response;
    }

    /**
     * Converts request from mobile app to request to Switch
     *
     * @param postbackRequest request from mobile app
     * @param approvalCode    approval code from config
     * @param consumerKey     consumer key from config
     * @return request msg to Switch
     */
    private SwitchPostbackRequest convertModel(PostbackRequest postbackRequest, String approvalCode, String consumerKey) {
        SwitchPostbackRequest request = new SwitchPostbackRequest();

        MerchantTransactions merchantTransaction = new MerchantTransactions();
        merchantTransaction.setTransactionId(postbackRequest.getTransactionId());
        merchantTransaction.setCurrency(postbackRequest.getCurrency());
        merchantTransaction.setOrderAmount(postbackRequest.getOrderAmount());
        merchantTransaction.setPurchaseDate(postbackRequest.getPurchaseDate());
        merchantTransaction.setTransactionStatus(postbackRequest.getTransactionStatus());
        merchantTransaction.setApprovalCode(approvalCode);
        merchantTransaction.setConsumerKey(consumerKey);

        List<MerchantTransactions> merchantTransactionsList = new ArrayList<MerchantTransactions>();
        merchantTransactionsList.add(merchantTransaction);

        request.setMerchantTransactions(merchantTransactionsList);
        return request;
    }

}
