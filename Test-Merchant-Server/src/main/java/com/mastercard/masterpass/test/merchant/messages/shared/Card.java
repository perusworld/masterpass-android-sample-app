package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"brandId", "brandName", "accountNumber", "billingAddress", "cardHolderName", "expiryMonth", "expiryYear"})
@XmlRootElement(name = "Card")
public class Card {

    @XmlElement(name = "BrandId")
    private String brandId;

    @XmlElement(name = "BrandName")
    private String brandName;

    @XmlElement(name = "AccountNumber")
    private String accountNumber;

    @XmlElement(name = "BillingAddress")
    private BillingAddress billingAddress;

    @XmlElement(name = "CardHolderName")
    private String cardHolderName;

    @XmlElement(name = "ExpiryMonth")
    private int expiryMonth;

    @XmlElement(name = "ExpiryYear")
    private int expiryYear;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [brandId=" + brandId + " ,brandName=" + brandName +
                " ,accountNumber=" + accountNumber + " ,billingAddress=" + billingAddress +
                " ,cardHolderName=" + cardHolderName + " ,expiryMonth=" + expiryMonth +" ,expiryYear=" + expiryYear +"]";
    }
}
