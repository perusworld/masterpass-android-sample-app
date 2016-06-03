package com.mastercard.masterpass.test.merchant.messages.xml;

import com.mastercard.masterpass.test.merchant.messages.shared.AuthenticationOptions;
import com.mastercard.masterpass.test.merchant.messages.shared.Card;
import com.mastercard.masterpass.test.merchant.messages.shared.Contact;
import com.mastercard.masterpass.test.merchant.messages.shared.ShippingAddress;

import javax.xml.bind.annotation.*;

/**
 * Model for requesting checkout resources message returned to mobile app from merchant server
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"card", "transactionId", "contact", "shippingAddress", "authenticationOptions", "walletId", "extensionPoint"})
@XmlRootElement(name = "Checkout")
public class SwitchCheckoutResourceResponse {

    @XmlElement(name = "Card")
    private Card card;

    @XmlElement(name = "TransactionId")
    private String transactionId;

    @XmlElement(name = "Contact")
    private Contact contact;

    @XmlElement(name = "ShippingAddress")
    private ShippingAddress shippingAddress;

    @XmlElement(name = "AuthenticationOptions")
    private AuthenticationOptions authenticationOptions;

    @XmlElement(name = "WalletID")
    private String walletId;

    @XmlElement(name = "ExtensionPoint")
    private String extensionPoint;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AuthenticationOptions getAuthenticationOptions() {
        return authenticationOptions;
    }

    public void setAuthenticationOptions(AuthenticationOptions authenticationOptions) {
        this.authenticationOptions = authenticationOptions;
    }

    public String getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(String extensionPoint) {
        this.extensionPoint = extensionPoint;
    }
}
