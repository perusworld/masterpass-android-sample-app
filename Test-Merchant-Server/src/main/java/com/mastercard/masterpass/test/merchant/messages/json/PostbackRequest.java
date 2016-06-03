package com.mastercard.masterpass.test.merchant.messages.json;

import com.mastercard.masterpass.test.merchant.messages.shared.TransactionStatus;

/**
 * Model for postback message posted from mobile app to merchant server
 */
public class PostbackRequest {

    private String transactionId;
    private String currency;
    private String orderAmount;
    private String purchaseDate;
    private TransactionStatus transactionStatus;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [transactionId=" + transactionId + " ,currency=" + currency + " ,orderAmount=" + orderAmount +
                " ,purchaseDate=" + purchaseDate +  " ,transactionStatus=" + transactionStatus + "]";
    }
}
