package com.mastercard.mymerchant.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ambar on 30/3/16.
 */
public class WebCheckoutUtils {

    private static final String TAG = "WebCheckoutUtils";

    /**
     * [MCO-SDK] Static ID's declared to easily read the data of Tokens List
     */

    public static final int ID_CHECKOUT = 0;
    public static final int ID_VERIFIER_TOKEN = 1;
    public static final int ID_REQUEST_TOKEN = 2;

    /**
     * [MCO-SDK] Try to get tokens from browser to app redirection URL to complete transaction
     *
     * @param appRedirectionUrl  URL that is returned by browser after completing payment process
     *
     */

    public static ArrayList<String> getTransactionKeys(String appRedirectionUrl) {

        /*UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
        urlQuerySanitizer.registerParameter("checkout", UrlQuerySanitizer.getUrlLegal());
        urlQuerySanitizer.registerParameter("oauth_token", UrlQuerySanitizer.getUrlLegal());
        urlQuerySanitizer.registerParameter("oauth_verifier", UrlQuerySanitizer.getUrlLegal());
        urlQuerySanitizer.parseUrl(appRedirectionUrl);*/
        try {
            appRedirectionUrl = java.net.URLDecoder.decode(appRedirectionUrl, "UTF-8");
            Log.v(TAG, "appRedirectionUrl = " + appRedirectionUrl);

            String arr1[] = appRedirectionUrl.split("checkout/");
            String arr2[] = arr1[arr1.length - 1].split("&");
            String arr3[] = arr2[1].split("=");
            String arr4[] = arr2[2].split("=");

            Log.v(TAG, "checkoutId : " + arr2[0] + "\noauth_verifier : " + arr3[1] + "\noauth_token : " + arr4[1]);

            ArrayList<String> tokensList = new ArrayList<>();
            tokensList.add(ID_CHECKOUT, arr2[0]);
            tokensList.add(ID_VERIFIER_TOKEN, arr3[1]);
            tokensList.add(ID_REQUEST_TOKEN, arr4[1]);
            return tokensList;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Getting unsupported encoding exception: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Getting MasterPass exception: " + e.getMessage());
            return null;
        }
    }
}
