package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for session key signing message returned to mobile app from merchant server
 */
public class SessionKeySigningResponse {

    private String appId;
    private String appVersion;
    private String appSigningPublicKey;
    private String sessionSignature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppSigningPublicKey() {
        return appSigningPublicKey;
    }

    public void setAppSigningPublicKey(String appSigningPublicKey) {
        this.appSigningPublicKey = appSigningPublicKey;
    }

    public String getSessionSignature() {
        return sessionSignature;
    }

    public void setSessionSignature(String sessionSignature) {
        this.sessionSignature = sessionSignature;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [appId=" + appId + " ,appVersion=" + appVersion +
                " ,appSigningPublicKey=" + appSigningPublicKey + " ,sessionSignature=" + sessionSignature + "]";
    }
}
