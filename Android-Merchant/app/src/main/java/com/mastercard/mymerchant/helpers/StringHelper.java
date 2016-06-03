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

package com.mastercard.mymerchant.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple class for formatting strings
 */
public class StringHelper {
    /**
     * Converts double value of currency to text representation with correct spacing and
     * decimal separator
     *
     * @param currencyCode code of the currency
     * @param value        amount
     * @return text representation
     */
/*    public static String asCurrency(String currencyCode, double value) {
        Currency cur = Currency.getInstance(currencyCode);
        // , - grouping separator; . - monetary decimal separator
        DecimalFormat df = new DecimalFormat("###,##0.00");
        return cur.getSymbol() + df.format(value);
    }*/

    /**
     * Formats Java Date to XML ISO 8601 format eg 2016-01-08T14:52:57.539-05:00
     * @param date Java date object
     * @return ISO 8601 format used in XMLs
     */
    public static String formatToXMLDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
        return sdf.format(date);
    }
}
