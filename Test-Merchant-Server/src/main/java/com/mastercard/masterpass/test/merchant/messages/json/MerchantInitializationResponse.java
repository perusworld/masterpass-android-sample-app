package com.mastercard.masterpass.test.merchant.messages.json;

import com.mastercard.masterpass.test.merchant.messages.shared.ExtensionPointMerchantInitializationResponse;

/**
 * Model for merchant initialization message returned to mobile app from merchant server
 */
public class MerchantInitializationResponse {

    private String requestToken;
    protected ExtensionPointMerchantInitializationResponse extensionPoint;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String token) {
        requestToken = token;
    }

    public ExtensionPointMerchantInitializationResponse getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(ExtensionPointMerchantInitializationResponse extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [requestToken=" + requestToken + ", extensionPoint=" + extensionPoint.toString() + "]";
    }
}
