package com.mastercard.masterpass.test.merchant.messages.json;


/**
 * Model for requesting checkout resources message posted from mobile app to merchant server
 */
public class CheckoutResourceRequest {

    private String accessToken;
    private String checkoutId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [accessToken=" + accessToken + " ,checkoutId=" + checkoutId + "]";
    }
}
