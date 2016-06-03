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

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mastercard.mymerchant.network.api.request.BaseRequest;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * Factory for creating API JSON requests using Gson library.
 */
public enum GsonRequestFactory {
    INSTANCE;

    private static final String TAG = "GsonRequestFactory";
    private final Gson mGson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new NetDateTimeAdapter()).create();

    /**
     * Creates GET GsonRequest. Fills all fields and convert requestObject to JSON representation
     *
     * @param method        REST methods type: GET, POST
     * @param url           full URL of the endpoint to hit
     * @param responseClazz class types of response
     * @param requestObject object with request or null if not needed
     * @param listener      API response listener
     * @param <T>           type of response
     * @return created GsonRequest
     */
    public <T> GsonRequest<T> createGsonRequest(final RestMethod method, final String url, final Class<T> responseClazz, final BaseRequest requestObject, final ResponseListener<T> listener) {
        if (requestObject != null) {

            Log.d(TAG, "Converting " + requestObject.getClass().getName() + " to JSON");
        }
        if (method == RestMethod.GET) {
            String fullWithParams = url;
            if (requestObject != null) {
                SortedMap<String, String> fields = requestObject.getFields();
                if (fields.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("?");
                    boolean isFirst = true;

                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (!isFirst) {
                            sb.append("&");
                        }
                        isFirst = false;
                        sb.append(key);
                        sb.append("=");
                        sb.append(value);
                    }
                    fullWithParams += sb.toString();
                }
            }
            Log.d(TAG, "Full URL with params: " + fullWithParams);
            return new GsonRequest<>(method, fullWithParams, responseClazz, "", false, listener);
        } else if (method == RestMethod.POST) {
            final String body = mGson.toJson(requestObject);
            return new GsonRequest<>(method, url, responseClazz, body, false, listener);
        } else {
            Log.d(TAG, "Unknown REST method");
            return null;
        }
    }


    /**
     * Gets {@link com.google.gson.Gson} object used for parsing
     *
     * @return {@link com.google.gson.Gson} parser
     */
    public Gson getGson() {
        return mGson;
    }

    /**
     * Custom adapter for converting {@link java.util.Date}
     * to JSON and back
     */
    class NetDateTimeAdapter extends TypeAdapter<Date> {
        @Override
        public Date read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            Date result = null;
            String str = reader.nextString();
            str = str.replaceAll("[^0-9]", "");
            if (!TextUtils.isEmpty(str)) {
                try {
                    result = new Date(Long.parseLong(str));
                } catch (NumberFormatException e) {

                    Log.d(TAG, "Error during parsing Date: " + e.toString());
                }
            }
            return result;
        }

        @Override
        public void write(JsonWriter writer, Date value) throws IOException {
            //.NET Date parser writer not needed
            throw new RuntimeException(".NET Date writer to JSON not implemented");
        }
    }

}
