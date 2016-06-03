package com.mastercard.masterpass.test.merchant.messages.xml;

import com.mastercard.masterpass.test.merchant.messages.shared.TransactionStatus;

import javax.xml.bind.annotation.*;

/**
 * Model with data about transaction that is passed during Postback call after checkout resources
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"transactionId", "consumerKey", "currency", "orderAmount", "purchaseDate", "transactionStatus",
        "approvalCode", "preCheckoutTransactionId", "expressCheckoutIndicator", "extensionPoint"})
@XmlRootElement(name = "MerchantTransactions")
public class MerchantTransactions {

    @XmlElement(name = "TransactionId", required = true)
    protected String transactionId;

    @XmlElement(name = "ConsumerKey", required = false)
    protected String consumerKey;

    @XmlElement(name = "Currency", required = true)
    protected String currency;

    @XmlElement(name = "OrderAmount", required = true)
    protected String orderAmount;

    @XmlElement(name = "PurchaseDate", required = true)
    protected String purchaseDate;

    @XmlElement(name = "TransactionStatus", required = true)
    protected TransactionStatus transactionStatus;

    @XmlElement(name = "ApprovalCode", required = true)
    protected String approvalCode;

    @XmlElement(name = "PreCheckoutTransactionId", required = false)
    protected String preCheckoutTransactionId;

    @XmlElement(name = "ExpressCheckoutIndicator", required = false)
    protected boolean expressCheckoutIndicator;

    @XmlElement(name = "ExtensionPoint", required = false)
    protected String extensionPoint;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
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

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getPreCheckoutTransactionId() {
        return preCheckoutTransactionId;
    }

    public void setPreCheckoutTransactionId(String preCheckoutTransactionId) {
        this.preCheckoutTransactionId = preCheckoutTransactionId;
    }

    public boolean isExpressCheckoutIndicator() {
        return expressCheckoutIndicator;
    }

    public void setExpressCheckoutIndicator(boolean expressCheckoutIndicator) {
        this.expressCheckoutIndicator = expressCheckoutIndicator;
    }

    public String getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(String extensionPoint) {
        this.extensionPoint = extensionPoint;
    }
}
