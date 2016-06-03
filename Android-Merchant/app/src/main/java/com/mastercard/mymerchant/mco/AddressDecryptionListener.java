package com.mastercard.mymerchant.mco;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.mastercard.masterpass.mc.core.network.VolleyWebServiceManager;
import com.mastercard.masterpass.mc.core.network.WebServiceManagerCallback;
import com.mastercard.masterpass.merchant.DecryptionCallback;
import com.mastercard.masterpass.merchant.DecryptionListener;
import com.mastercard.mymerchant.network.api.ApiList;

/**
 * Created by ambar on 21/4/16.
 */
public class AddressDecryptionListener implements DecryptionListener {

    private Context mContext;
    private static final String TAG = AddressDecryptionListener.class.getName();

    public AddressDecryptionListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void decryptAddresses(String request, final DecryptionCallback decryptionCallback) {
        Log.v(TAG, "Request : " + request);
        Log.v(TAG, "Decryption Callback : " + decryptionCallback.toString());
        VolleyWebServiceManager manager = new VolleyWebServiceManager(mContext,
                new WebServiceManagerCallback() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(TAG, "Response " + response.toString());
                        decryptionCallback.onSuccess(response);
                    }
                    @Override
                    public void onError(String errorCode) {
                        decryptionCallback.onFailure(errorCode);
                        Log.v(TAG, "Error " + errorCode);
                    }
                }
        );
        manager.sendRequest(
                ApiList.API_BASE_URL + "decrypt",
                request,
                Request.Priority.HIGH
        );
    }
}
