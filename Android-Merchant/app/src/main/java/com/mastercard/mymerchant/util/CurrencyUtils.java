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

import java.text.DecimalFormat;
import java.util.Currency;

/**
 * Various utility methods for dealing with currencies and amounts.
 */
public class CurrencyUtils {

    /**
     * Formats amount as text with correct number of fraction digits depending on currency (also can
     * include currency sign and grouping separator).<br/>
     * Some currencies have 0, 1 or 3 digits after decimal point<br/>
     * No grouping separator
     *
     * @param amount                amount to format in lowest currency denomination eg 1299 (with represents $12.99 or YEN 1299)
     * @param currencyCodeLetters   currency code as 3 letters eg USD
     * @param includeCurrencySymbol true if currency symbol should be included
     * @return formatted text
     */
    public static String convertToText(long amount, String currencyCodeLetters, boolean includeCurrencySymbol) {
        return convertToText(amount, currencyCodeLetters, includeCurrencySymbol, false);
    }

    /**
     * Formats amount as text with correct number of fraction digits depending on currency (also can
     * include currency sign and grouping separator).<br/>
     * Some currencies have 0, 1 or 3 digits after decimal point<br/>
     * No grouping separator
     *
     * @param amount                amount to format in lowest currency denomination eg 1299 (with represents $12.99 or YEN 1299)
     * @param currency              currency object
     * @param includeCurrencySymbol true if currency symbol should be included
     * @return formatted text
     */
    public static String convertToText(long amount, Currency currency, boolean includeCurrencySymbol) {
        return convertToText(amount, currency, includeCurrencySymbol, false);
    }

    /**
     * Formats amount as text with correct number of fraction digits depending on currency (also can
     * include currency sign and grouping separator).<br/>
     * Some currencies have 0, 1 or 3 digits after decimal point
     *
     * @param amount                   amount to format in lowest currency denomination eg 1299 (with represents $12.99 or YEN 1299)
     * @param currencyCodeLetters      currency code as 3 letters eg USD
     * @param includeCurrencySymbol    true if currency symbol should be included
     * @param includeGroupingSeparator true if currency grouping separator should be included
     * @return formatted text
     */
    public static String convertToText(long amount, String currencyCodeLetters, boolean includeCurrencySymbol, boolean includeGroupingSeparator) {
        Currency currency = Currency.getInstance(currencyCodeLetters);
        return convertToText(amount, currency, includeCurrencySymbol, includeGroupingSeparator);
    }

    /**
     * Formats amount as text with correct number of fraction digits depending on currency (also can
     * include currency sign and grouping separator).<br/>
     * Some currencies have 0, 1 or 3 digits after decimal point
     *
     * @param amount                   amount to format in lowest currency denomination eg 1299 (with represents $12.99 or YEN 1299)
     * @param currency                 currency object
     * @param includeCurrencySymbol    true if currency symbol should be included
     * @param includeGroupingSeparator true if currency grouping separator should be included
     * @return formatted text
     */
    public static String convertToText(long amount, Currency currency, boolean includeCurrencySymbol, boolean includeGroupingSeparator) {
        // Get number of digits after decimal point
        int numFractionDigits = currency.getDefaultFractionDigits();
        // Calculate modifier for amount for division
        double modifier = Math.pow(10.0, numFractionDigits);
        // Build pattern for DecimalFormat
        String pattern = "0";
        if (includeGroupingSeparator) {
            pattern = "###,##0";
        }
        // Depending on number of digits after decimal point, add proper number of 0
        for (int i = 0; i < numFractionDigits; i++) {
            if (i == 0) {
                pattern += ".";
            }
            pattern += "0";
        }
        // Create formatter
        DecimalFormat df = new DecimalFormat(pattern);
        if (includeCurrencySymbol) {
            // Divide amount by modifier, format and add currency symbol
            return currency.getSymbol() + df.format(amount / modifier);
        } else {
            // Divide amount by modifier and format
            return df.format(amount / modifier);
        }
    }

    /**
     * Converts amount stored as a text (including currency sign and decimal point) to long value
     * representing amount in lowest currency denomination
     *
     * @param text text with amount eg $12.99
     * @return long value eg 1299
     */
    public static long parseValue(String text) {
        // Remove any characters that are not digits (including period)
        String rawValue = text.replaceAll("[^\\d]+", "");
        // Parse as long
        return Long.parseLong(rawValue);
    }
}
