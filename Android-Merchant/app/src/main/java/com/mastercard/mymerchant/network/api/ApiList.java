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

package com.mastercard.mymerchant.network.api;

import com.mastercard.mymerchant.BuildConfig;
import com.mastercard.mymerchant.network.api.request.AccessTokenRequest;
import com.mastercard.mymerchant.network.api.request.CheckoutResourceRequest;
import com.mastercard.mymerchant.network.api.request.MerchantInitializationRequest;

import com.mastercard.mymerchant.network.api.request.PostbackRequest;
import com.mastercard.mymerchant.network.api.request.SessionKeySigningRequest;
import com.mastercard.mymerchant.network.api.response.AccessTokenResponse;
import com.mastercard.mymerchant.network.api.response.CheckoutResourceResponse;
import com.mastercard.mymerchant.network.api.response.MerchantInitializationResponse;
import com.mastercard.mymerchant.network.api.response.PostbackResponse;
import com.mastercard.mymerchant.network.api.response.RequestTokenResponse;
import com.mastercard.mymerchant.network.api.response.SessionKeySigningResponse;

import java.util.HashMap;

/**
 * Class that contains list of endpoints along with request and response types
 */
public class ApiList {

    /**
     * List of end-points
     */
    public enum Method {
        API_SESSION_KEY_SIGNING("sessionKeySigning"),
        API_REQUEST_TOKEN("requestToken"),
        API_MERCHANT_INITIALIZATION("merchantInitialization"),
        API_ACCESS_TOKEN("accessToken"),
        API_CHECKOUT_RESOURCES("checkoutResource"),
        API_POSTBACK("postback"),
        API_INITIALIZATION_WEB("initialize.html");

        /**
         * Keep method name
         */
        private final String val;

        /**
         * ctor
         *
         * @param v api method name
         */
        private Method(String v) {
            val = v;
        }

        /**
         * Gets method name
         *
         * @return method name
         */
        public String getVal() {
            return val;
        }

        @Override
        public String toString() {
            return "" + this.name() + "[" + val + "]";
        }
    }

    /**
     * Base URL for all API calls. This is URL prefix ending with '/'
     */
    public static String API_BASE_URL = BuildConfig.MERCHANT_SERVER_URL;


    /**
     * Model class for keeping pair of request and response types
     */
    public static class Pair {
        public Class request;
        public Class response;

        public Pair(Class request, Class response) {
            this.request = request;
            this.response = response;
        }
    }

    /**
     * HashMap containing endpoint name and request/response types. If not request object needed
     * the type is null
     */
    private static HashMap<String, Pair> apiEndpointsList;

    static {
        apiEndpointsList = new HashMap<String, Pair>();
        apiEndpointsList.put(Method.API_SESSION_KEY_SIGNING.getVal(), new Pair(SessionKeySigningRequest.class, SessionKeySigningResponse.class));
        apiEndpointsList.put(Method.API_REQUEST_TOKEN.getVal(), new Pair(null, RequestTokenResponse.class));
        apiEndpointsList.put(Method.API_MERCHANT_INITIALIZATION.getVal(), new Pair(MerchantInitializationRequest.class, MerchantInitializationResponse.class));
        apiEndpointsList.put(Method.API_ACCESS_TOKEN.getVal(), new Pair(AccessTokenRequest.class, AccessTokenResponse.class));
        apiEndpointsList.put(Method.API_CHECKOUT_RESOURCES.getVal(), new Pair(CheckoutResourceRequest.class, CheckoutResourceResponse.class));
        apiEndpointsList.put(Method.API_POSTBACK.getVal(), new Pair(PostbackRequest.class, PostbackResponse.class));
        apiEndpointsList.put(Method.API_INITIALIZATION_WEB.getVal(), new Pair(null, null));
    }

    /**
     * Returns HashMap with list of defined endpoints
     *
     * @return HashMap with list of endpoints names and request/response types
     */
    public static HashMap<String, Pair> getEndpointsList() {
        return apiEndpointsList;
    }

}
