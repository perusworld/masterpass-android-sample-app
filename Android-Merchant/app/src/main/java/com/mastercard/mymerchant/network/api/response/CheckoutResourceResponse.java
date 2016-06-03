/*
 *    ****************************************************************************
 *    Copyright (c) 2015, MasterCard International Incorporated and/or its
 *    affiliates. All rights reserved.
 *    <p/>
 *    The contents of this file may only be used subject to the MasterCard
 *    Mobile Payment SDK for MCBP and/or MasterCard Mobile MPP UI SDK
 *    Materials License.
 *    <p/>
 *    Please refer to the file LICENSE.TXT for full details.
 *    <p/>
 *    TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS", WITHOUT
 *    WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *    WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *    NON INFRINGEMENT. TO THE EXTENT PERMITTED BY LAW, IN NO EVENT SHALL
 *    MASTERCARD OR ITS AFFILIATES BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *    IN THE SOFTWARE.
 *    *****************************************************************************
 */

package com.mastercard.mymerchant.network.api.response;

import com.google.gson.annotations.SerializedName;
import com.mastercard.mymerchant.network.api.model.AuthenticationOptions;
import com.mastercard.mymerchant.network.api.model.Card;
import com.mastercard.mymerchant.network.api.model.Contact;
import com.mastercard.mymerchant.network.api.model.ShippingAddress;

import java.io.Serializable;

/**
 * Model for endpoint "checkoutResource" response.
 */
public class CheckoutResourceResponse extends BaseResponse implements Serializable {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("walletId")
    private String walletId;

    @SerializedName("card")
    private Card card;

    @SerializedName("contact")
    private Contact contact;

    @SerializedName("shippingAddress")
    private ShippingAddress shippingAddress;

    @SerializedName("authenticationOptions")
    private AuthenticationOptions authenticationOptions;

    @SerializedName("rawSwitchResponse")
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

    public String getRawSwitchResponse() {
        return rawSwitchResponse;
    }

    public void setRawSwitchResponse(String rawSwitchResponse) {
        this.rawSwitchResponse = rawSwitchResponse;
    }
}
