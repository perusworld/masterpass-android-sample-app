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

package com.mastercard.mymerchant.network.api.request;

import com.google.gson.annotations.SerializedName;
import com.mastercard.mymerchant.helpers.MapUtils;
import com.mastercard.mymerchant.model.TransactionStatus;

import java.util.SortedMap;

import static com.mastercard.mymerchant.helpers.MapUtils.entry;

/**
 * Model for endpoint "postback" request.
 */
public class PostbackRequest extends BaseRequest {

    @SerializedName("transactionId")
    String transactionId;
    @SerializedName("currency")
    String currency;
    @SerializedName("orderAmount")
    String orderAmount;
    @SerializedName("purchaseDate")
    String purchaseDate;
    @SerializedName("transactionStatus")
    TransactionStatus transactionStatus;

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    @Override
    public SortedMap<String, String> getFields() {
        return MapUtils.asSortedMap(
                entry("transactionId", transactionId),
                entry("currency", currency),
                entry("orderAmount", orderAmount),
                entry("purchaseDate", purchaseDate),
                entry("transactionStatus", String.valueOf(transactionStatus)));
    }
}
