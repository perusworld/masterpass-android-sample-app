package com.mastercard.masterpass.test.merchant.messages.json;


import com.mastercard.masterpass.test.merchant.messages.shared.AuthenticationOptions;
import com.mastercard.masterpass.test.merchant.messages.shared.Card;
import com.mastercard.masterpass.test.merchant.messages.shared.Contact;
import com.mastercard.masterpass.test.merchant.messages.shared.ShippingAddress;

/**
 * Model for requesting checkout resources message returned to mobile app from merchant server
 */
public class CheckoutResourceResponse {

    private String transactionId;

    private String walletId;

    private Card card;

    private Contact contact;

    private ShippingAddress shippingAddress;

    private AuthenticationOptions authenticationOptions;

    private String extensionPoint;

    private String rawSwitchResponse;

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

    public String getRawSwitchResponse() {
        return rawSwitchResponse;
    }

    public void setRawSwitchResponse(String rawSwitchResponse) {
        this.rawSwitchResponse = rawSwitchResponse;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [transactionId=" + transactionId + " ,walletId=" + walletId +
                " ,card=" + card + " ,contact=" + contact + " ,shippingAddress=" + shippingAddress + " ,authenticationOptions=" +
                authenticationOptions + " ,extensionPoint=" + extensionPoint  + "]";
    }
}
