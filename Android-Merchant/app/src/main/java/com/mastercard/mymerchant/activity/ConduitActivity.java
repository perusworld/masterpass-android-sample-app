package com.mastercard.mymerchant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.merchant.AmountData;
import com.mastercard.masterpass.merchant.AuthorizationResponse;
import com.mastercard.masterpass.merchant.InitializationListener;
import com.mastercard.masterpass.merchant.MasterPass;
import com.mastercard.masterpass.merchant.MasterPassButton;
import com.mastercard.masterpass.merchant.MasterPassButtonListener;
import com.mastercard.masterpass.merchant.TransactionDetails;
import com.mastercard.masterpass.merchant.TransactionResultListener;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.helpers.DialogHelper;
import com.mastercard.mymerchant.helpers.StringHelper;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.mco.McoInitializer;
import com.mastercard.mymerchant.mco.PaymentConfirmationIntegration;
import com.mastercard.mymerchant.mco.WebCheckoutCallback;
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

import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConduitActivity extends Activity {

    private static final String TAG = ConduitActivity.class.getSimpleName();
    private boolean closeDialog = false;
    private static final int DIALOG_DELAY_MILLIS = 2000;
    private MasterPassButton btnMasterpass;
    private final DataManager dataManager = DataManager.getInstance();
    private boolean sendClickEvent = false;
    private Uri intentData;
    private String callbackUrl;
    private String nonce;
    private long estimatedTotal;
    private long tax;
    private long total;
    private Currency currency;
    protected TextView txtStatus;
    RequestQueue requestQueue = null;
    protected boolean doneRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conduit);
        doneRequest = false;
        requestQueue = Volley.newRequestQueue(this);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        this.intentData = data;
        Log.d(TAG, action);
        Log.d(TAG, data.toString());
        callbackUrl = data.getQueryParameter("callback");
        Log.d(TAG, callbackUrl);
        nonce = data.getQueryParameter("nonce");
        total = estimatedTotal = new Double(Double.parseDouble(data.getQueryParameter("total")) * 100).longValue();
        tax = new Double(Double.parseDouble(data.getQueryParameter("tax")) * 100).longValue();
        currency = Currency.getInstance(Locale.US);
        if (dataManager.isMcoInitialized()) {
            initMCOCheckout(false);
        } else {
            initMCO();
        }
    }

    private void initMCO() {
        // Create AsyncTask
        new AsyncTask<Void, Void, MasterPassException>() {

            @Override
            protected MasterPassException doInBackground(Void... params) {
                try {
                    McoInitializer mcoInitializer = new McoInitializer();
                    // Initialize with app context so it is consistent throughout the app
                    mcoInitializer.initLiveMCO(getApplicationContext(), mInitializationListener);
                } catch (MasterPassException masterPassException) {
                    return masterPassException;
                }
                return null;
            }

            @Override
            protected void onPostExecute(MasterPassException masterPassException) {
                super.onPostExecute(masterPassException);
                // Check if we finished with error
                if (masterPassException != null) {
                    DataManager.getInstance().setMcoInitialized(false);
                    Toast.makeText(ConduitActivity.this, "Unable to initialize MCO SDK. " + masterPassException.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Unable to initialize MCO - " + masterPassException.getMessage());
                    masterPassException.printStackTrace();
                }
            }
        }.execute();
    }

    private final InitializationListener mInitializationListener = new InitializationListener() {
        @Override
        public void initializationFailed(MasterPassException e) {
            Log.e(TAG, "Init MCO: initializationFailed");
            e.printStackTrace();
            DataManager.getInstance().setMcoInitialized(false);
            // Notify user
            Toast.makeText(getApplicationContext(), "MCO initialization failed. Please restart the app. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void paymentMethodAvailable() {
            Log.i(TAG, "There is at least one MasterPass Available wallet on the device");
            DataManager.getInstance().setMcoInitialized(true);
            initMCOCheckout(true);
        }
    };

    protected void showDialog(String msg, boolean close) {
        if (close) {
            DialogHelper.closeDialog();
        }
        DialogHelper.showProgressDialog(ConduitActivity.this, msg);
        txtStatus.setText(msg);
    }

    protected void sendClickEvent() {
        if (sendClickEvent) {
            sendClickEvent = false;
            if (dataManager.isMcoInitialized() && null != btnMasterpass) {
                showDialog("Processing Payment ...", true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != btnMasterpass) {
                            btnMasterpass.getChildAt(0).callOnClick();
                        }
                    }
                }, DIALOG_DELAY_MILLIS);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendClickEvent();
        if (doneRequest) {
            returnToProducts(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (closeDialog) {

            closeDialog = false;

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

    private void initMCOCheckout(boolean sendClickEvent) {
        this.sendClickEvent = true;
        Log.v(TAG, "initMCOCheckout");

        RelativeLayout.LayoutParams btnParams = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // Check if the MCO SDK has been initialized. Initialization is started in StartActivity
        if (dataManager.isMcoInitialized()) {
            try {
                btnMasterpass = MasterPass.getInstance().getMasterPassButton(
                        masterPassButtonListener,
                        new WebCheckoutCallback(),
                        new PaymentConfirmationIntegration(this),
                        mTransactionResultListener,
                        null
                );
            } catch (MasterPassException e) {

                Log.e(TAG, "Getting MasterPass button exception: " + e.getMessage() + " " +
                        e.getErrorMessage() + " " + e.getErrorCode());
            }
        } else {
            //NOOP
        }
        Log.d(TAG, "Init MCO Checkout method end");
        if (sendClickEvent) {
            sendClickEvent();
        }
    }

    private final MasterPassButtonListener masterPassButtonListener = new MasterPassButtonListener() {
        @Override
        public TransactionDetails getTransactionDetails() {
            // Build transaction details object
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setIsShippingRequired(false);
            try {
                transactionDetails.setAmountData(new AmountData(
                        estimatedTotal,
                        tax,
                        total,
                        currency));
            } catch (MasterPassException e) {
                e.printStackTrace();
            }

            return transactionDetails;
        }
    };

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
            closeDialog = false;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogHelper.showDialog(ConduitActivity.this, "Transaction Cancelled", "Could not complete the transaction", null);

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
            closeDialog = true;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    DialogHelper.showDialog(ConduitActivity.this, "Transaction Failed", "Could not complete the transaction.\n" +
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
            closeDialog = true;

            // Clear information about address used for transaction - not saving it
            ShippingAddressesManager.INSTANCE.clearAddressChosenForTransaction();

            // After a short delay, close the dialog that was opened in transactionInitiated()
            // and show the error message. Delay is needed for user to see message.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    DialogHelper.showDialog(ConduitActivity.this, "Transaction Details Error", "Could not complete the transaction.\n" +
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
            showDialog("Completing Payment", true);

        }

        /**
         * Raised when the transaction has been authorized/completed.
         */
        @Override
        public void transactionAuthorized(final AuthorizationResponse authorizationResponse) {
            Log.d(TAG, "TransactionResultListener: transactionAuthorized");

            // If the transaction failed, then make sure the progress dialog stays on screen
            // for a short time.
            closeDialog = true;

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
                        DialogHelper.showDialog(ConduitActivity.this, "Completing Failed",
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
                                        DialogHelper.showDialog(ConduitActivity.this, "Completing Failed",
                                                "Could not complete transaction. Checkout Resources failed."
                                                , null);
                                    }

                                    @Override
                                    public void onResponse(CheckoutResourceResponse checkoutResourceResponse) {

                                        // Make a postback call to finalize transaction with Switch and Merchant Server
                                        performPostback("USD",
                                                String.format("%d", Math.round(total)),
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
                        DialogHelper.showDialog(ConduitActivity.this, "Completing Failed",
                                "Could not complete transaction. Sending Postback failed."
                                , null);
                    }

                    @Override
                    public void onResponse(PostbackResponse response) {
                        if (response.isSuccess) {
                            showDialog("Payment Success", true);
                            sendPaymentStatus(DIALOG_DELAY_MILLIS - startMillis, checkoutResourceResponse);
                        } else {
                            DialogHelper.showDialog(ConduitActivity.this, "Completing Failed",
                                    "Could not complete transaction. Postback failed."
                                    , null);
                        }
                    }
                }
        );
    }

    private void sendPaymentStatus(final long delay, final CheckoutResourceResponse checkoutResourceResponse) {
        StringRequest req = new StringRequest(Request.Method.POST, callbackUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, response);
                proceedToCompleteActivity(delay, checkoutResourceResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nonce", nonce);
                params.put("mpstatus", "success");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(req);
    }

    private void proceedToCompleteActivity(long delay, final CheckoutResourceResponse checkoutResourceResponse) {
        if (delay < 100) {
            delay = 100;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://threads"));
                startActivity(intent);
                // Close last instantiated progress dialog
                DialogHelper.closeProgressDialog();
                doneRequest = true;
            }
        }, delay);
    }

    private void returnToProducts(final boolean isSlideFromRight) {
        Intent intent = new Intent(ConduitActivity.this, ProductsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        if (isSlideFromRight) {
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }
}
