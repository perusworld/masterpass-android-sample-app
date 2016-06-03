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

package com.mastercard.mymerchant;

import com.mastercard.mymerchant.model.Product;
import com.mastercard.masterpass.core.MasterPassCard;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Constants {
    /**
     * List of products to use within the application.
     */
    public static final Product[] PRODUCTS = {
            new Product("Samsung Galaxy S6", "The first thing Will noticed about the latest Galaxy smartphone is its new striking design, which comes in a choice of colours", 4, 59485, R.drawable.product_samsung_galaxy_s6, false),
            new Product("Apple iPhone 6 16GB", "Every feature we add to iPhone must answer yes to one question: will it make the experience better? So the colours", 4.2f, 36900, R.drawable.product_iphone_6, false),
            new Product("GoPro Camera HD HERO4", "Black Edition Is The Most Advanced Gopro, Ever. No Expense Was Spared During Its Development, Resulting In A Gopro", 3, 29995, R.drawable.product_gopro_camera_4, false),
            new Product("Apple MacBook Pro 13.3", "Enjoy precise graphics and intuitive computing with the Apple MacBook Pro MD101B/A 13\" Laptop", 5, 72300, R.drawable.product_macbook_pro, false),
            new Product("SanDisk 32GB Flash Drive ", "Take your favorite files with you on the compact and portable SanDisk Cruzer Blade USB flash drive.", 2, 2599, R.drawable.product_usb_stick, false),
            new Product("Apple iPad Air 2 128GB", "The iPad Air has the power of lightness with Retina display. So much to see. So little to hold iPad has always been about the display and that's even more pertinent with iPad Air.", 4.5f, 65999, R.drawable.product_ipad_air_2, false),
            new Product("Samsung Galaxy S4 mini", "The Samsung Galaxy S4 mini isn't quite done yet with its array of amazing features worth experiencing", 2.0f, 26990, R.drawable.product_samsung_galaxy_s4_mini, false),
            new Product("Apple iPhone 6 Plus 16GB", "It's hard to believe a phone so thin could offer so many features: a larger display, a faster chip, the latest wireless technology, an 8MP iSight camera and more. All in a beautiful aluminium body designed and made with an unprecedented level of precision", 3.75f, 43900, R.drawable.product_iphone_6_plus, false),
            new Product("Samsung UE60F6100 3D HD TV", "Experience entertainment like never before with the Samsung F6100 Series 6 LED TV", 3.75f, 99900, R.drawable.product_samsung_tv, false),
            new Product("Hauppauge HD Video Recorder", "HD PVR is the world's first High-Definition video recorder for making real-time H.264 compressed recordings at resolutions up to 1080i.", 2.5f, 12793, R.drawable.product_high_def_recorder, false),
            new Product("The Amazing Spider-Man", "The Amazing Spider-Man™ is the story of Peter Parker (Garfield), an outcast high schooler who was abandoned by his parents as a boy, leaving him to be raised by his Uncle Ben (Sheen) and Aunt May (Field). Like most teenagers, Peter is trying to figure out who he is and how he got to be the person he is today.", 4.0f, 999, R.drawable.amazing_spiderman, true)
    };

    /**
     * Tax percentage values
     */
    public static HashMap<String, Integer> TAX_MAP;

    static {
        TAX_MAP = new HashMap<>();
        TAX_MAP.put("AL", 10);
        TAX_MAP.put("AK", 10);
        TAX_MAP.put("AZ", 10);
        TAX_MAP.put("AR", 10);
        TAX_MAP.put("CA", 10);
        TAX_MAP.put("CO", 10);
        TAX_MAP.put("CT", 10);
        TAX_MAP.put("DE", 10);

        TAX_MAP.put("FL", 5);
        TAX_MAP.put("GA", 5);
        TAX_MAP.put("HI", 5);
        TAX_MAP.put("ID", 5);
        TAX_MAP.put("IL", 5);
        TAX_MAP.put("IN", 5);
        TAX_MAP.put("IA", 5);
        TAX_MAP.put("NY", 5);
    }

    /**
     * Default currency code to use throughout the application.
     */
    public static final String DEFAULT_CURRENCY_CODE = "USD";

    /**
     * List of supported payments networks by this Merchant
     */
    public static final EnumSet SUPPORTED_PAYMENT_NETWORKS = EnumSet.of(
            MasterPassCard.PaymentNetwork.AMEX,
            MasterPassCard.PaymentNetwork.DISCOVER,
            MasterPassCard.PaymentNetwork.MASTERCARD,
            MasterPassCard.PaymentNetwork.VISA);

    /**
     * List of supported locale
     */
    public static final String[] SUPPORTED_LOCALE = new String[]{
            Locale.US.getCountry()/*,//Other Locale are not supported with this version of MCO
            Locale.UK.getCountry(),
            Locale.FRANCE.getCountry(),
            Locale.GERMANY.getCountry()*/
    };

    /**
     * List of supported currencies
     */
    public static final String[] SUPPORTED_CURRENCIES = new String[]{
            "USD"/*, //For simplicity use only $
            "GBP",
            "EUR",
            "PLN",
            "BGN",
            "ALL",
            "JPY",// 0 fraction digits
            "JOD",// 3 fraction digits
            "CLF",// 4 fraction digits
            "MGA"// 1 fraction digits */
    };

    // Constants
    public static final String DEFAULT_BRAND_ID = "master";
    public static final String DEFAULT_ORIGIN_URL = "http://mastercard.com";

    /**
     * List of all valid US States
     */
    public static final Map<String, String> STATE_MAP;

    static {
        STATE_MAP = new HashMap<String, String>();
        STATE_MAP.put("AL", "Alabama");
        STATE_MAP.put("AK", "Alaska");
        STATE_MAP.put("AZ", "Arizona");
        STATE_MAP.put("AR", "Arkansas");
        STATE_MAP.put("CA", "California");
        STATE_MAP.put("CO", "Colorado");
        STATE_MAP.put("CT", "Connecticut");
        STATE_MAP.put("DE", "Delaware");
        STATE_MAP.put("FL", "Florida");
        STATE_MAP.put("GA", "Georgia");
        STATE_MAP.put("HI", "Hawaii");
        STATE_MAP.put("ID", "Idaho");
        STATE_MAP.put("IL", "Illinois");
        STATE_MAP.put("IN", "Indiana");
        STATE_MAP.put("IA", "Iowa");
        STATE_MAP.put("KS", "Kansas");
        STATE_MAP.put("KY", "Kentucky");
        STATE_MAP.put("LA", "Louisiana");
        STATE_MAP.put("ME", "Maine");
        STATE_MAP.put("MD", "Maryland");
        STATE_MAP.put("MA", "Massachusetts");
        STATE_MAP.put("MI", "Michigan");
        STATE_MAP.put("MN", "Minnesota");
        STATE_MAP.put("MS", "Mississippi");
        STATE_MAP.put("MO", "Missouri");
        STATE_MAP.put("MT", "Montana");
        STATE_MAP.put("NE", "Nebraska");
        STATE_MAP.put("NV", "Nevada");
        STATE_MAP.put("NH", "New Hampshire");
        STATE_MAP.put("NJ", "New Jersey");
        STATE_MAP.put("NM", "New Mexico");
        STATE_MAP.put("NY", "New York");
        STATE_MAP.put("NC", "North Carolina");
        STATE_MAP.put("ND", "North Dakota");
        STATE_MAP.put("OH", "Ohio");
        STATE_MAP.put("OK", "Oklahoma");
        STATE_MAP.put("OR", "Oregon");
        STATE_MAP.put("PA", "Pennsylvania");
        STATE_MAP.put("RI", "Rhode Island");
        STATE_MAP.put("SC", "South Carolina");
        STATE_MAP.put("SD", "South Dakota");
        STATE_MAP.put("TN", "Tennessee");
        STATE_MAP.put("TX", "Texas");
        STATE_MAP.put("UT", "Utah");
        STATE_MAP.put("VT", "Vermont");
        STATE_MAP.put("VA", "Virginia");
        STATE_MAP.put("WA", "Washington");
        STATE_MAP.put("WV", "West Virginia");
        STATE_MAP.put("WI", "Wisconsin");
        STATE_MAP.put("WY", "Wyoming");
    }
}
