package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for requesting access token message returned to mobile app from merchant server
 */
public class AccessTokenResponse {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        accessToken = token;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [accessToken=" + accessToken + "]";
    }
}
