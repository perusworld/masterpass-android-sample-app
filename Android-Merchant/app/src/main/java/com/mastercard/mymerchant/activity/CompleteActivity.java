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

package com.mastercard.mymerchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.mastercard.mymerchant.Constants;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.adapter.BasketAdapter;
import com.mastercard.mymerchant.model.Basket;
import com.mastercard.mymerchant.network.api.response.CheckoutResourceResponse;
import com.mastercard.mymerchant.util.CurrencyUtils;
import com.mastercard.mymerchant.util.PreferencesHelper;
import com.mastercard.mymerchant.util.XmlFormatter;

/**
 * Activity representing complete transaction screen.
 * It shows bought products and summary section
 */
public class CompleteActivity extends Activity {
    /**
     * TAG used for logging.
     */
    private static final String TAG = CompleteActivity.class.getName();

    /**
     * Key for passing checkout resources inside the intent
     */
    public static final String KEY_CHECKOUT_RESOURCES = "CHECKOUT_RESOURCES";

    /**
     * The current basket.
     */
    private final Basket mBasket = DataManager.getInstance().mBasket;

    /**
     * UI Elements.
     */
    private TextView mTxtSubtotal;
    private TextView mTxtTotal;
    private TextView mTxtTax;
    private TextView mShippingOptionTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        TextView txtOderNo = (TextView) findViewById(R.id.txt_order_no);
        TextView txtRecipientName = (TextView) findViewById(R.id.tv_recipient_name);
        TextView txtAddressLine1 = (TextView) findViewById(R.id.tv_address_line1);
        TextView txtPostalCode = (TextView) findViewById(R.id.tv_postal_code);
        TextView txtCity = (TextView) findViewById(R.id.tv_city);
        TextView txtState = (TextView) findViewById(R.id.tv_state);
        final TextView txtDebug = (TextView) findViewById(R.id.tv_debug);
        Switch swDebug = (Switch) findViewById(R.id.sw_debug);

        int taxPercentage = 0;

        Intent intent = getIntent();
        if (intent != null) {
            CheckoutResourceResponse checkoutResources = (CheckoutResourceResponse) intent.getSerializableExtra(KEY_CHECKOUT_RESOURCES);
            if (checkoutResources != null) {
                txtOderNo.setText(String.valueOf(checkoutResources.getTransactionId()));

                if (checkoutResources.getShippingAddress() != null) {

                    txtRecipientName.setText(checkoutResources.getShippingAddress().getRecipientName());
                    txtAddressLine1.setText(checkoutResources.getShippingAddress().getLine1());
                    txtPostalCode.setText(checkoutResources.getShippingAddress().getPostalCode());
                    txtCity.setText(checkoutResources.getShippingAddress().getCity());
                    txtState.setText(checkoutResources.getShippingAddress().getCountrySubdivision());

                    String simpleCountrySubDivCode = checkoutResources.getShippingAddress().getCountrySubdivision();
                    // Cut the "US-" prefix if needed
                    if (simpleCountrySubDivCode.length() > 2) {
                        simpleCountrySubDivCode = simpleCountrySubDivCode.substring(3);
                    }
                    // Check if we have tax percentage for given address
                    if (Constants.TAX_MAP.containsKey(simpleCountrySubDivCode)) {
                        taxPercentage = Constants.TAX_MAP.get(simpleCountrySubDivCode);
                    }
                } else {
                    // In case no address (eg shipping not required) hide section
                    View layoutShippingAddress = findViewById(R.id.lay_shipping_address);
                    layoutShippingAddress.setVisibility(View.GONE);

                    View lblShipping = findViewById(R.id.lbl_shipping);
                    View txtShipping = findViewById(R.id.txt_shipping);
                    View divider2 = findViewById(R.id.divider2);
                    lblShipping.setVisibility(View.GONE);
                    txtShipping.setVisibility(View.GONE);
                    divider2.setVisibility(View.GONE);
                }
                txtDebug.setText(XmlFormatter.prettyFormat(checkoutResources.getRawSwitchResponse()));
            }
        }

        swDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtDebug.setVisibility(View.VISIBLE);
                } else {
                    txtDebug.setVisibility(View.GONE);
                }
            }
        });

        // Ensure the scroll view is scrolled right to the top
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });

        // Rename the title to "Success"
        TextView txtTitle = (TextView) findViewById(R.id.txt_cart);
        txtTitle.setText(R.string.txt_success);

        // Setup the list of products
        BasketAdapter mBasketAdapter = new BasketAdapter(this, DataManager.getInstance().mBasket, false);
        ListView mListProducts = (ListView) findViewById(android.R.id.list);
        mListProducts.setAdapter(mBasketAdapter);


        // Update the total and subtotal
        mTxtSubtotal = (TextView) findViewById(R.id.txt_subtotal);
        mTxtTotal = (TextView) findViewById(R.id.txt_total);
        mTxtTax = (TextView) findViewById(R.id.txt_tax);
        mShippingOptionTotal = (TextView) findViewById(R.id.txt_shipping);
        updateBasketTotals(taxPercentage);


        // Bind click event to the continue shopping button
        (findViewById(R.id.btn_continue_shopping)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToProducts(true);
            }
        });

        // We now need to clear the basket for the next time round
        String currency = PreferencesHelper.INSTANCE.getCurrencyString(this);
        DataManager.getInstance().clearBasket();
        DataManager.getInstance().mBasket.setCurrencyCode(currency);
    }

    @Override
    public void onBackPressed() {
        // Return to Products
        returnToProducts(false);
    }

    /**
     * Proceed to Products page
     *
     * @param isSlideFromRight indicate from where the new page should be animated
     */
    private void returnToProducts(boolean isSlideFromRight) {
        Intent intent = new Intent(CompleteActivity.this, ProductsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        if (isSlideFromRight) {
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    /**
     * Update the subtotal and total based on the current basket.
     *
     * @param taxPercentage tax level in %
     */
    private void updateBasketTotals(int taxPercentage) {
        // Calculate tax value
        long taxValue = Math.round(mBasket.mSubTotal * taxPercentage / 100.0f);

        // Update UI
        mTxtSubtotal.setText(CurrencyUtils.convertToText(mBasket.mSubTotal, mBasket.getCurrencyCode(), true));
        mTxtTotal.setText(CurrencyUtils.convertToText(mBasket.getTotal() + taxValue, mBasket.getCurrencyCode(), true));
        mTxtTax.setText(CurrencyUtils.convertToText(taxValue, mBasket.getCurrencyCode(), true));
        mShippingOptionTotal.setText(CurrencyUtils.convertToText(mBasket.mShippingPrice, mBasket.getCurrencyCode(), true));
    }
}
