package com.mastercard.mymerchant.mco;

import android.net.Uri;

import com.mastercard.masterpass.merchant.UriCallback;
import com.mastercard.masterpass.merchant.WebUrlCallback;
import com.mastercard.mymerchant.network.api.ApiList;

/**
 * Created by e050862 on 5/12/16.
 */
public class WebCheckoutCallback implements WebUrlCallback{

    @Override
    public void getAppToWebUrl(UriCallback uriCallback) {
        uriCallback.completedWithUri(Uri.parse(ApiList.API_BASE_URL + ApiList.Method.API_INITIALIZATION_WEB.getVal()));
    }
}
