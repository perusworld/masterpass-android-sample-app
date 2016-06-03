package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for postback message returned to mobile app from merchant server
 */
public class PostbackResponse {

    private Boolean isSuccess;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [isSuccess=" + isSuccess + "]";
    }
}
