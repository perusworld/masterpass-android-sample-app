package com.mastercard.masterpass.test.merchant.messages.json;

import com.mastercard.masterpass.test.merchant.messages.shared.ExtensionPointMerchantInitializationRequest;

/**
 * Model for merchant initialization message posted from mobile app to merchant server
 */
public class MerchantInitializationRequest {

    private String requestToken;
    private String originUrl;
    private ExtensionPointMerchantInitializationRequest extensionPoint;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public ExtensionPointMerchantInitializationRequest getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(ExtensionPointMerchantInitializationRequest extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [requestToken=" + requestToken + " ,originUrl=" + originUrl + " ,extensionPoint=" + extensionPoint.toString() +  "]";
    }

}
