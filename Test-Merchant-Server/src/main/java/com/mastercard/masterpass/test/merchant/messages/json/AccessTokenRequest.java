package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for requesting access token message posted from mobile app to merchant server
 */
public class AccessTokenRequest {

    private String oauthToken;
    private String oauthVerifier;

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthVerifier() {
        return oauthVerifier;
    }

    public void setOauthVerifier(String oauthVerifier) {
        this.oauthVerifier = oauthVerifier;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [oauthToken=" + oauthToken + " ,oauthVerifier=" + oauthVerifier + "]";
    }
}
