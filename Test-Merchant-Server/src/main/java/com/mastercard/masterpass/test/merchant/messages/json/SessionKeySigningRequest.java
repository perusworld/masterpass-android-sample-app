package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for session key signing message posted from mobile app to merchant server
 */
public class SessionKeySigningRequest {
    private String appId;
    private String appVersion;
    private String appSigningPublicKey;

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [appId=" + appId + " ,appVersion=" + appVersion +
                " ,appSigningPublicKey=" + appSigningPublicKey + "]";
    }
}
