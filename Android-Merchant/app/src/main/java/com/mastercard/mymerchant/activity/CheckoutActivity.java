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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mastercard.masterpass.mc.core.network.VolleyWebServiceManager;
import com.mastercard.masterpass.mc.core.network.WebServiceManagerCallback;
import com.mastercard.masterpass.merchant.DecryptionCallback;
import com.mastercard.masterpass.merchant.DecryptionListener;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.adapter.BasketAdapter;
import com.mastercard.mymerchant.helpers.DialogHelper;
import com.mastercard.mymerchant.helpers.StringHelper;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.mco.AddressDecryptionListener;
import com.mastercard.mymerchant.mco.PaymentConfirmationIntegration;
import com.mastercard.mymerchant.mco.WebCheckoutCallback;
import com.mastercard.mymerchant.model.Basket;
import com.mastercard.mymerchant.model.BasketItem;
import com.mastercard.mymerchant.model.TransactionStatus;
import com.mastercard.mymerchant.network.ResponseListener;
import com.mastercard.mymerchant.network.RestMethod;
import com.mastercard.mymerchant.network.api.ApiList;
import com.mastercard.mymerchant.network.api.request.AccessTokenRequest;
import com.mastercard.mymerchant.network.api.request.CheckoutResourceRequest;
import com.mastercard.mymerchant.network.api.request.PostbackRequest;
import com.mastercard.mymerchant.network.api.response.AccessTokenResponse;
import com.mastercard.mymerchant.network.api.response.CheckoutResourceResponse;
import com.mastercard.mymerchant.network.api.response.PostbackResponse;
import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.merchant.AmountData;
import com.mastercard.masterpass.merchant.AuthorizationResponse;
import com.mastercard.masterpass.merchant.MasterPass;
import com.mastercard.masterpass.merchant.MasterPassButton;

import com.mastercard.masterpass.merchant.TransactionDetails;
import com.mastercard.masterpass.merchant.TransactionResultListener;
import com.mastercard.mymerchant.util.CurrencyUtils;
import com.mastercard.mymerchant.util.WebCheckoutUtils;


import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/***
 * Activity used at checkout.
 */
public class CheckoutActivity extends Activity
        implements AdapterView.OnItemClickListener {

    /**
     * TAG used for logging.
     */
    private static final String TAG = CheckoutActivity.class.getName();

    /**
     * Delay for "Completing payment" dialog
     */
    private static final int DIALOG_DELAY_MILLIS = 2000;

    /**
     * The current basket.
     */
    private final Basket mBasket = DataManager.getInstance().mBasket;

    /**
     * UI Elements.
     */
    private BasketAdapter mBasketAdapter;
    private TextView mTxtSubtotal;
    private TextView mTxtTotal;

    /**
     * Data Manager.
     */
    private final DataManager mDataManager = DataManager.getInstance();

    /**
     * The "Buy with MasterPass" button.
     */
    private RelativeLayout mLayoutBuyWithMasterPass;
    private RelativeLayout mLayoutOr;

    /**
     * Defines if the progress dialog should close after a delay, or if it should close
     * immediately when returning from MCO.
     */
    private boolean mDelayCloseProgressDialog = false;

    /**
     * Handler used for reinit MasterPass Button
     */
    private Handler mReinitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mLayoutBuyWithMasterPass = (RelativeLayout) findViewById(R.id.layout_buy_with_masterpass);
        mLayoutOr = (RelativeLayout) findViewById(R.id.layout_or);

        // Get the currency being used
        String currency = DataManager.getInstance().mBasket.getCurrencyCode();

        // Ensure no shipping is currently applied
        mBasket.mShippingPrice = 0;

        // Ensure the scroll view is scrolled right to the top
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        });

        // Setup the list of products
        mBasketAdapter = new BasketAdapter(this, mBasket, currency);
        ListView mListProducts = (ListView) findViewById(android.R.id.list);
        mListProducts.setAdapter(mBasketAdapter);
        mListProducts.setItemsCanFocus(true);
        mListProducts.setOnItemClickListener(this);

        // Update the total and subtotal
        mTxtSubtotal = (TextView) findViewById(R.id.txt_subtotal);
        mTxtTotal = (TextView) findViewById(R.id.txt_total);
        updateBasketTotals();

        // Initially hide "or"
        mLayoutOr.setVisibility(View.GONE);

        // Initializes the [MCO-SDK] checkout
        initMCOCheckout();

        // Hide the shipping options (requires making total appear below divider 2)
        removeShippingAndTotal();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDelayCloseProgressDialog) {

            mDelayCloseProgressDialog = false;

            Log.v(TAG, "Delayed close dialog");

            // After a short delay, close the dialog that was opened in transactionInitiated()
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Close last instantiated progress dialog
                    DialogHelper.closeProgressDialog();
                }
            }, DIALOG_DELAY_MILLIS);

        } else {
            // In case user clicked 'Back' and returned to this page
            Log.v(TAG, "Immediate close dialog");

            // Close last instantiated progress dialog
            DialogHelper.closeProgressDialog();

        }

    }

    @Override
    protected void onDestroy() {
        if (mReinitHandler != null) {
            mReinitHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    /***
     * [MCO-SDK] Called this method after completing Web checkout process. Have to collect data from intent and send it for further process
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getData() != null) {
            ArrayList<String> tokensList;
            try {
                tokensList = WebCheckoutUtils.getTransactionKeys(intent.getData().toString());
                getAdditionalResourceAndComplete(tokensList.get(WebCheckoutUtils.ID_REQUEST_TOKEN), tokensList.get(WebCheckoutUtils.ID_VERIFIER_TOKEN), tokensList.get(WebCheckoutUtils.ID_CHECKOUT));
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to proceed transaction");
            }
        }
    }

    /***
     * [MCO-SDK] Initializes everything needed to checkout using MCO.
     */
    private void initMCOCheckout() {
        Log.v(TAG, "initMCOCheckout");

        // Check if the MCO SDK has been initialized. Initialization is started in StartActivity
        if (mDataManager.isMcoInitialized()) {

            RelativeLayout.LayoutParams btnParams = new
                    RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

            try {
                // Clear any old chosen addresses before transaction
                ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

                // Build transaction details object
                TransactionDetails transactionDetails = new TransactionDetails();
                transactionDetails.setIsShippingRequired(mDataManager.mBasket.doesBasketRequireShipping());
                transactionDetails.setAmountData(
                        new AmountData(
                                mDataManager.mBasket.getSubTotal(),
                                0,
                                mDataManager.mBasket.getSubTotal(),
                                Currency.getInstance(Locale.US)));

                // Get MasterPass Button from MCO SDK
                MasterPassButton mBuyWithMasterPassButton;
                if (mDataManager.mBasket.doesBasketRequireShipping()) {
                    mBuyWithMasterPassButton = MasterPass.getInstance()
                            .getMasterPassButton(
                                    transactionDetails,
                                    new WebCheckoutCallback(),
                                    new PaymentConfirmationIntegration(this),
                                    mTransactionResultListener,
                                    new AddressDecryptionListener(CheckoutActivity.this));
                } else {
                    mBuyWithMasterPassButton = MasterPass.getInstance()
                            .getMasterPassButton(
                                    transactionDetails,
                                    new WebCheckoutCallback(),
                                    new PaymentConfirmationIntegration(this),
                                    mTransactionResultListener,
                                    null);
                }

                if (mBuyWithMasterPassButton != null) {

                    Log.d(TAG, "getMasterPassButton: received");

                    // Show the "or" view as we now have more than one option on screen
                    mLayoutOr.setVisibility(View.VISIBLE);

                    // Show the MasterPass button
                    mBuyWithMasterPassButton.setLayoutParams(btnParams);
                    mLayoutBuyWithMasterPass.removeAllViews();
                    mLayoutBuyWithMasterPass.addView(mBuyWithMasterPassButton);

                } else {

                    mLayoutOr.setVisibility(View.GONE);
                    Log.e(TAG, "getMasterPassButton: null");
                }

            } catch (MasterPassException e) {

                Log.e(TAG, "Getting MasterPass button exception: " + e.getMessage() + " " +
                        e.getErrorMessage() + " " + e.getErrorCode());
            }

        } else {
            // MCO not ready so try in a moment
            Log.d(TAG, "MCO not yet initialized, re-schedule getting button in 1s");
            (mReinitHandler).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        CheckoutActivity.this.initMCOCheckout();
                    }
                }
            }, 1000);// 1s
        }

        Log.d(TAG, "Init MCO Checkout method end");
    }

    /**
     * [MCO-SDK] Transaction result listener passed to MCO.
     * Defines behaviour to occur at different states within the transaction.
     */
    private final TransactionResultListener mTransactionResultListener = new TransactionResultListener() {

        /**
         * Raised when the transaction has been cancelled.
         */
        @Override
        public void transactionCanceled() {
            Log.d(TAG, "TransactionResultListener: transactionCanceled");

            // Close any old dialogs
            DialogHelper.closeDialog();

            // If the transaction is cancelled, close progress dialog immediately
            mDelayCloseProgressDialog = false;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogHelper.showDialog(CheckoutActivity.this, "Transaction Cancelled", "Could not complete the transaction", null);

                }
            }, 100);
        }

        /**
         * Raised when the transaction fails.
         * @param e Exception object raised by the MCO SDK.
         */
        @Override
        public void transactionFailed(final MasterPassException e) {
            Log.d(TAG, "TransactionResultListener: transactionFailed " + e.toString() + " " + e.getErrorCode() + " " + e.getErrorMessage() + " " + e.getMessage());
            e.printStackTrace();

            // Close any old dialogs
            DialogHelper.closeDialog();

            // If the transaction failed, then make sure the progress dialog stays on screen
            // for a short time.
            mDelayCloseProgressDialog = true;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    DialogHelper.showDialog(CheckoutActivity.this, "Transaction Failed", "Could not complete the transaction.\n" +
                            e.getMessage(), null);

                }
            }, DIALOG_DELAY_MILLIS);
        }

        /**
         * Raised when the transactions details are incorrect.
         * @param e Exception object raised by the MCO SDK.
         */
        @Override
        public void transactionDetailsInputException(final MasterPassException e) {
            Log.d(TAG, "TransactionResultListener: transactionDetailsInputException " + e.getErrorCode() + " " + e.getErrorMessage() + " " + e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            // Close any old dialogs
            DialogHelper.closeDialog();

            // If the transaction details error occur, then make sure the progress dialog stays on screen
            // for a short time.
            mDelayCloseProgressDialog = true;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    DialogHelper.showDialog(CheckoutActivity.this, "Transaction Details Error", "Could not complete the transaction.\n" +
                            e.getMessage(), null);

                }
            }, DIALOG_DELAY_MILLIS);
        }

        /**
         * Raised when the transaction has been initiated.
         */
        @Override
        public void transactionInitiated() {
            Log.d(TAG, "TransactionResultListener: transactionInitiated");
            // Close any old dialogs
            DialogHelper.closeDialog();

            // Show a progress dialog which will become visible when the MCO activity closes itself
            DialogHelper.showProgressDialog(CheckoutActivity.this, "Completing Payment");
        }

        /**
         * Raised when the transaction has been authorized/completed.
         */
        @Override
        public void transactionAuthorized(final AuthorizationResponse authorizationResponse) {
            Log.d(TAG, "TransactionResultListener: transactionAuthorized");

            // If the transaction failed, then make sure the progress dialog stays on screen
            // for a short time.
            mDelayCloseProgressDialog = true;

            // Save address chosen for transaction
            ShippingAddressesManager.INSTANCE.saveAddressChosenForTransaction();

            // Get additional data and go to complete screen
            getAdditionalResourceAndComplete(
                    authorizationResponse.getRequestToken(),
                    authorizationResponse.getVerifierToken(),
                    authorizationResponse.getCheckoutId());
        }
    };

    /**
     * Try to request additional information about transaction and proceed to complete screen
     *
     * @param requestToken  transaction request token
     * @param verifierToken transaction verify token
     * @param checkoutId    checkout id
     */
    private void getAdditionalResourceAndComplete(final String requestToken, final String verifierToken, final String checkoutId) {
        final long startMillis = System.currentTimeMillis();
        // Save purchase date as now
        final Date purchaseDate = new Date();
        // Prepare request
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setOauthToken(requestToken);
        accessTokenRequest.setOauthVerifier(verifierToken);

        // Make a call to Merchant Server to get access token
        DataManager.getInstance().getNetworkManager().makeApiCall(
                RestMethod.POST,
                ApiList.Method.API_ACCESS_TOKEN,
                accessTokenRequest,
                new ResponseListener<AccessTokenResponse>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        DialogHelper.showDialog(CheckoutActivity.this, "Completing Failed",
                                "Could not complete transaction. Getting Access Token failed.", null);
                    }

                    @Override
                    public void onResponse(AccessTokenResponse accessTokenResponse) {
                        // Prepare request
                        CheckoutResourceRequest checkoutResourceRequest = new CheckoutResourceRequest();
                        checkoutResourceRequest.setAccessToken(accessTokenResponse.accessToken);
                        checkoutResourceRequest.setCheckoutId(checkoutId);

                        // Make a call to Merchant Server to get additional data about transaction
                        DataManager.getInstance().getNetworkManager().makeApiCall(
                                RestMethod.POST,
                                ApiList.Method.API_CHECKOUT_RESOURCES,
                                checkoutResourceRequest,
                                new ResponseListener<CheckoutResourceResponse>() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, error.toString());
                                        DialogHelper.showDialog(CheckoutActivity.this, "Completing Failed",
                                                "Could not complete transaction. Checkout Resources failed."
                                                , null);
                                    }

                                    @Override
                                    public void onResponse(CheckoutResourceResponse checkoutResourceResponse) {

                                        // Make a postback call to finalize transaction with Switch and Merchant Server
                                        performPostback(mDataManager.mBasket.getCurrencyCode(),
                                                String.format("%d", Math.round(mDataManager.mBasket.getTotal() * 100)),
                                                StringHelper.formatToXMLDate(purchaseDate),
                                                TransactionStatus.Success,
                                                startMillis,
                                                checkoutResourceResponse);
                                    }
                                });

                    }
                });
    }

    /**
     * Make a call to Test Merchant Server to finalize transaction with Postback data
     *
     * @param currency                 transaction currency eg USD
     * @param orderAmount              transaction amount in cents
     * @param purchaseDate             date in ISO 8601
     * @param transactionStatus        status "Success" or "Failure"
     * @param startMillis              timestamp when the post-transaction processing started
     * @param checkoutResourceResponse checkout resources received from Test Merchant Server
     */
    private void performPostback(String currency, String orderAmount, String purchaseDate,
                                 TransactionStatus transactionStatus, final long startMillis,
                                 final CheckoutResourceResponse checkoutResourceResponse) {
        // Prepare request
        PostbackRequest postbackRequest = new PostbackRequest();
        postbackRequest.setTransactionId(checkoutResourceResponse.getTransactionId());
        postbackRequest.setCurrency(currency);
        postbackRequest.setOrderAmount(orderAmount);
        postbackRequest.setPurchaseDate(purchaseDate);
        postbackRequest.setTransactionStatus(transactionStatus);

        // Make a call to Merchant Server to pass postback data
        DataManager.getInstance().getNetworkManager().makeApiCall(
                RestMethod.POST,
                ApiList.Method.API_POSTBACK,
                postbackRequest,
                new ResponseListener<PostbackResponse>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        DialogHelper.showDialog(CheckoutActivity.this, "Completing Failed",
                                "Could not complete transaction. Sending Postback failed."
                                , null);
                    }

                    @Override
                    public void onResponse(PostbackResponse response) {
                        if (response.isSuccess) {
                            // After min number of seconds, proceed to Complete Page
                            proceedToCompleteActivity(DIALOG_DELAY_MILLIS - startMillis, checkoutResourceResponse);
                        } else {
                            DialogHelper.showDialog(CheckoutActivity.this, "Completing Failed",
                                    "Could not complete transaction. Postback failed."
                                    , null);
                        }
                    }
                }
        );
    }

    /**
     * Proceed to CompleteActivity after short delay
     *
     * @param delay                    in msec
     * @param checkoutResourceResponse additional data about transaction
     */
    private void proceedToCompleteActivity(long delay, final CheckoutResourceResponse checkoutResourceResponse) {
        if (delay < 100) {
            delay = 100;
        }

        // After a short delay, close the dialog that was opened in transactionInitiated()
        // and proceed to the checkout completion screen.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                // Proceed to the completion activity
                Intent intent = new Intent(CheckoutActivity.this, CompleteActivity.class);
                // Pass checkout resources to CompleteActivity
                intent.putExtra(CompleteActivity.KEY_CHECKOUT_RESOURCES, checkoutResourceResponse);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                // Close last instantiated progress dialog
                DialogHelper.closeProgressDialog();

            }
        }, delay);
    }

    /**
     * Update the subtotal and total based on the current basket.
     */
    private void updateBasketTotals() {

        mTxtSubtotal.setText(CurrencyUtils.convertToText(mBasket.getSubTotal(), mBasket.getCurrencyCode(), true));
        mTxtTotal.setText(CurrencyUtils.convertToText(mBasket.getTotal(), mBasket.getCurrencyCode(), true));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Grab the item that was clicked
        BasketItem item = mBasketAdapter.getItem(position);

        // Remove it from the basket
        int numItemsLeft = mBasket.removeProduct(item.mProduct);

        // Update out on-screen totals
        updateBasketTotals();

        // If there are no items left in the basket, then go back to the products activity
        if (numItemsLeft <= 0) {
            Log.w(TAG, "No items left in basket");
            DialogHelper.emptyBasket(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    backToProducts();
                }
            });
        }

        // Because the Basket is shared by the adapter, it will have automatically updated
        mBasketAdapter.notifyDataSetChanged();
    }

    /**
     * Override the onBackPressed behaviour to force navigation to the products page.
     */
    @Override
    public void onBackPressed() {
        backToProducts();
    }

    /**
     * Navigate to the products page whilst clearing the backstack.
     */
    private void backToProducts() {
        Intent intent = new Intent(this, ProductsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }


    /**
     * Remove the shipping and total details as this page doesn't require it but it's using a shared layout
     */
    private void removeShippingAndTotal() {
        findViewById(R.id.lbl_shipping).setVisibility(View.GONE);
        findViewById(R.id.txt_shipping).setVisibility(View.GONE);
        findViewById(R.id.divider2).setVisibility(View.GONE);
        findViewById(R.id.lbl_total).setVisibility(View.GONE);
        findViewById(R.id.txt_total).setVisibility(View.GONE);
        findViewById(R.id.divider3).setVisibility(View.GONE);
        findViewById(R.id.lbl_tax).setVisibility(View.GONE);
        findViewById(R.id.txt_tax).setVisibility(View.GONE);
        findViewById(R.id.divider5).setVisibility(View.GONE);
    }
}