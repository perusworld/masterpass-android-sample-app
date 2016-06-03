package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for request token message returned to mobile app from merchant server
 */
public class RequestTokenResponse {

    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String token) {
        requestToken = token;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [requestToken=" + requestToken + "]";
    }
}
