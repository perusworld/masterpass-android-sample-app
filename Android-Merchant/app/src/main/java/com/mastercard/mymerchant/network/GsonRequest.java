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

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import com.google.gson.JsonSyntaxException;
import com.mastercard.mymerchant.BuildConfig;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Wrapper for making JSON requests with GSON library.
 * It adds header, setup http params, serialize and deserialize json
 */
class GsonRequest<T> extends JsonRequest<T> {

    private static final String TAG = GsonRequest.class.getName();

    private final Class<T> responseClazz;
    private final String jsonRequestBody;
    private final boolean useGzipCompression;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private final Response.Listener<T> listener;

    private static final String HEADER_ENCODING = "Content-Encoding";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private String uniqueRequestId;

    public GsonRequest(RestMethod method, String url, Class<T> responseClazz, String jsonRequestBody, boolean useGzipCompression, ResponseListener<T> listener) {
        super(method.toInt(), url, (useGzipCompression ? "" : jsonRequestBody), listener, listener);
        Random r = new Random();
        uniqueRequestId = new Date().getTime() + "" + (r.nextInt(900) + 100);
        this.responseClazz = responseClazz;
        this.jsonRequestBody = jsonRequestBody;
        this.useGzipCompression = useGzipCompression;
        if (useGzipCompression) {
            this.headers = new HashMap<>();
            this.headers.put(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
            this.headers.put(HEADER_ENCODING, ENCODING_GZIP);
        } else {
            this.headers = null;
        }
        this.listener = listener;
        this.parameters = null;


        Log.d(TAG, "Creating JSON Request (" + uniqueRequestId + "): " + jsonRequestBody);
        setRetryPolicy(new DefaultRetryPolicy(BuildConfig.GSON_REQUEST_TIMEOUT, BuildConfig.GSON_REQUEST_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public boolean isGzipped(NetworkResponse response) {
        Map<String, String> headers = response.headers;
        return headers != null && !headers.isEmpty() && headers.containsKey(HEADER_ENCODING) &&
                headers.get(HEADER_ENCODING).equalsIgnoreCase(ENCODING_GZIP);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return parameters != null ? parameters : super.getParams();
    }

    @Override
    public byte[] getBody() {
        if (useGzipCompression) {
            return compress2(jsonRequestBody);
        } else {
            return super.getBody();
        }
    }

    @Override
    public void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        // If we have some custom message from server, repack Volley Error
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = null;
            if (useGzipCompression && isGzipped(response)) {
                json = ungzip(response.data);
            } else {
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }

            Log.d(TAG, "Received (" + uniqueRequestId + ") json: " + json);


            T responseObject = GsonRequestFactory.INSTANCE.getGson().fromJson(json, responseClazz);
            Response<T> result = Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    private String ungzip(byte[] bytes) {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bytes)), Charset.forName("utf-8"));
            StringWriter sw = new StringWriter();
            char[] chars = new char[1024];
            for (int len; (len = isr.read(chars)) > 0; ) {
                sw.write(chars, 0, len);
            }
            return sw.toString();
        } catch (IOException e) {

            Log.e(TAG, "unGZIP failed");
            e.printStackTrace();
        }
        return "unGZIP failed";
    }

    private static byte[] gzip(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(bos);
            OutputStreamWriter osw = new OutputStreamWriter(gzip, Charset.forName("utf-8"));
            osw.write(s);
            osw.close();
            return /*Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);*/bos.toByteArray();/*.toString("UTF-8");*/
        } catch (IOException e) {
            Log.e(TAG, "GZIP failed");
            e.printStackTrace();
        }
        return null;//"GZIP failed";
    }

    public static byte[] compress(String string) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
            GZIPOutputStream gos = new GZIPOutputStream(os);
            gos.write(string.getBytes());
            gos.close();
            byte[] compressed = os.toByteArray();
            os.close();
            return compressed;
        } catch (IOException e) {
            Log.e(TAG, "GZIP failed");
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] compress2(String string) {
        try {
            byte[] blockcopy = ByteBuffer
                    .allocate(4)
                    .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                    .putInt(string.length())
                    .array();
            ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
            GZIPOutputStream gos = new GZIPOutputStream(os);
            gos.write(string.getBytes());
            gos.close();
            os.close();
            byte[] compressed = new byte[4 + os.toByteArray().length];
            System.arraycopy(blockcopy, 0, compressed, 0, 4);
            System.arraycopy(os.toByteArray(), 0, compressed, 4, os.toByteArray().length);
            return compressed;
        } catch (IOException e) {
            Log.e(TAG, "GZIP failed");
            e.printStackTrace();
        }
        return null;
    }
}