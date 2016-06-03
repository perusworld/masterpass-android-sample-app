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
import com.mastercard.mymerchant.network.api.model.ExtensionPointMerchantInitialization;

import java.util.SortedMap;

/**
 * Model for endpoint "merchantInitialization" request.
 */
public class MerchantInitializationRequest extends BaseRequest {

    @SerializedName("requestToken")
    String requestToken;

    @SerializedName("originUrl")
    String originUrl;

    @SerializedName("extensionPoint")
    ExtensionPointMerchantInitialization extensionPoint;

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public void setExtensionPoint(ExtensionPointMerchantInitialization extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    @Override
    public SortedMap<String, String> getFields() {
        return MapUtils.asSortedMap();// No need to add field as we are doing POST request
    }
}
