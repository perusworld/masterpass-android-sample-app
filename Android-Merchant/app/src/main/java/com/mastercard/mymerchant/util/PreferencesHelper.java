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

package com.mastercard.mymerchant.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.mastercard.mymerchant.Constants;
import com.mastercard.masterpass.core.CryptogramType;

import java.util.Locale;

/**
 * Helper class for dealing with Shared Preferences.
 * It manages:
 * - cryptogram type
 * - locale
 * - currency
 */
public enum PreferencesHelper {
    INSTANCE;//singleton

    private final String KEY_CRYPTOGRAM_TYPE = "cryptogramType";
    private final String KEY_LOCALE = "locale";
    private final String KEY_CURRENCY = "currency";

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public CryptogramType getCryptogramTypeEnum(Context context) {
        return CryptogramType.valueOf(getCryptogramTypeString(context));
    }

    public String getCryptogramTypeString(Context context) {
        return getSharedPreferences(context).getString(KEY_CRYPTOGRAM_TYPE, CryptogramType.UCAF.toString());
    }

    public void setCryptogramType(Context context, String cryptogramTypeString) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_CRYPTOGRAM_TYPE, cryptogramTypeString);
        editor.apply();
    }

    public String getLocaleString(Context context) {
        return getSharedPreferences(context).getString(KEY_LOCALE, Locale.US.getCountry());
    }

    public void setLocale(Context context, String localeString) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_LOCALE, localeString);
        editor.apply();
    }

    public String getCurrencyString(Context context, String defaultCurrency) {
        String currency = getCurrencyString(context);
        if (currency != null) {
            return currency;
        } else {
            return defaultCurrency;
        }
    }

    public String getCurrencyString(Context context) {
        return getSharedPreferences(context).getString(KEY_CURRENCY, Constants.DEFAULT_CURRENCY_CODE);
    }

    public void setCurrency(Context context, String currencyString) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_CURRENCY, currencyString);
        editor.apply();
    }
}
