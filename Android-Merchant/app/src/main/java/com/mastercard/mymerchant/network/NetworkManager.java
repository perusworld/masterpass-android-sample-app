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

package com.mastercard.mymerchant.network;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mastercard.mymerchant.network.api.ApiList;
import com.mastercard.mymerchant.network.api.request.BaseRequest;
import com.mastercard.mymerchant.network.api.response.BaseResponse;


import java.util.HashMap;

/**
 * Main class when interacting with network layer
 */
public class NetworkManager {

    private static final String TAG = NetworkManager.class.getName();
    /**
     * Request queue for volley.
     */
    private RequestQueue mRequestQueue;

    public NetworkManager(Application application) {
        // Create a new request queue
        mRequestQueue = Volley.newRequestQueue(application.getApplicationContext());
    }

    /**
     * Wrapper for makeApiCallInternal to intercept request and response and perform custom actions
     *
     * @param <T>          type of response
     * @param method       REST methods type: GET, POST
     * @param endpointName name of the endpoint to call
     * @param request      request objects or null if not needed
     * @param listener     listener for responses
     */
    public <T extends BaseResponse> void makeApiCall(final RestMethod method, final ApiList.Method endpointName, final BaseRequest request, final ResponseListener<T> listener) {

        // Make real API call
        makeApiCallInternal(method, endpointName, request, listener);
    }

    /**
     * Internal method used to make API call. It creates JSON request and put it in the queue
     *
     * @param <T>          type of response
     * @param method       REST methods type: GET, POST
     * @param endpointName name of the endpoint to call
     * @param request      request objects or null if not needed
     * @param listener     listener for responses
     */
    private <T extends BaseResponse> void makeApiCallInternal(final RestMethod method, final ApiList.Method endpointName, final BaseRequest request, final ResponseListener<T> listener) {
        Log.d(TAG, "Making API call [" + method + "] to " + ApiList.API_BASE_URL + endpointName.getVal());
        // Get endpoints list
        HashMap<String, ApiList.Pair> endpointsList = ApiList.getEndpointsList();
        // Find called endpoint on the list
        ApiList.Pair pair = endpointsList.get(endpointName.getVal());
        // Check if we have information about called endpoint
        if (pair == null) {
            Log.d(TAG, "Endpoint [" + endpointName.toString() + "] is not on ApiList");
            return;
        }
        // Check if request object is not null when not null is required
        if (pair.request != null && request == null) {
            Log.d(TAG, "The request object should be of [" + pair.request.getName() + "] type not [null]");
            return;
        }
        // Check if request is null when null is required
        if (pair.request == null && request != null) {
            Log.d(TAG, "The request object should be null not [" + request.getClass().getName() + "]");
            return;
        }
        // Check request type matches the required one
        if (pair.request != null && !request.getClass().equals(pair.request)) {
            Log.d(TAG, "The request object should be of [" + pair.request.getName() + "] type not [" + request.getClass().getName() + "]");
            return;
        }

        // Create request
        GsonRequest apiRequest = GsonRequestFactory.INSTANCE.createGsonRequest(method, ApiList.API_BASE_URL + endpointName.getVal(), pair.response, request, listener);

        // Place request in the queue
        mRequestQueue.add(apiRequest);
        Log.d(TAG, "Request added to requests queue");
    }
}
