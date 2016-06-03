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

package com.mastercard.mymerchant.mco;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.mastercard.masterpass.core.CryptogramType;
import com.mastercard.mymerchant.Constants;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.manager.ShippingMethodsManager;
import com.mastercard.mymerchant.model.DbAddressModel;
import com.mastercard.mymerchant.network.ResponseListener;
import com.mastercard.mymerchant.network.RestMethod;
import com.mastercard.mymerchant.network.api.ApiList;
import com.mastercard.mymerchant.network.api.model.DSRP;
import com.mastercard.mymerchant.network.api.model.DSRPOptions;
import com.mastercard.mymerchant.network.api.model.ExtensionPointMerchantInitialization;
import com.mastercard.mymerchant.network.api.model.Option;
import com.mastercard.mymerchant.network.api.request.MerchantInitializationRequest;
import com.mastercard.mymerchant.network.api.response.MerchantInitializationResponse;
import com.mastercard.mymerchant.network.api.response.RequestTokenResponse;
import com.mastercard.masterpass.core.MasterPassAddress;
import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.merchant.AmountData;
import com.mastercard.masterpass.merchant.CheckoutInitiationCallback;
import com.mastercard.masterpass.merchant.PaymentConfirmationCallback;
import com.mastercard.masterpass.merchant.ShippingMethod;
import com.mastercard.mymerchant.util.CurrencyUtils;
import com.mastercard.mymerchant.util.PreferencesHelper;

import java.util.List;

/**
 * [MCO-SDK] Callback class passed to MCO when retrieving the MasterPass button.
 * It is responsible for interacting with user when he is on Payment Confirmation page.
 * Used to provide shipping addresses list, shipping methods list, default values
 * and confirming total amount after shipping costs.
 */
public class PaymentConfirmationIntegration implements PaymentConfirmationCallback {

    /**
     * TAG for logging purposes.
     */
    private static final String TAG = PaymentConfirmationIntegration.class.getName();
    private Context mContext;

    /***
     * Constructor.
     *
     * @param context current context
     */
    public PaymentConfirmationIntegration(Context context) {
        mContext = context;
    }

    /***
     * Retrieves a list of shipping methods valid for the supplied address.
     *
     * @param masterPassAddress The address with which we want to find shipping methods for.
     * @return List of valid shipping methods.
     */
    @Override
    public List<ShippingMethod> getUpdatedShippingMethodsList(MasterPassAddress masterPassAddress) {
        Log.v(TAG, "getUpdatedShippingMethodsList");
        // We are using static list of shipping methods not taking into account chosen address
        return ShippingMethodsManager.INSTANCE.getSupportedShippingMethods();
    }

    /***
     * Retrieves address list from merchant application.
     *
     * @return List of shipping addresses.
     */
    @Override
    public List<MasterPassAddress> getShippingAddressesList(boolean b) {
        Log.v(TAG, "getShippingAddressesList");
        List<DbAddressModel> dbAddressModels = ShippingAddressesManager.INSTANCE.getAddresses();
        return ShippingAddressesManager.INSTANCE.dbAddressListToMasterPassAddressList(dbAddressModels);
    }

    /***
     * Retrieves default shipping method ID from merchant application.
     *
     * @return Default shipping method ID.
     */
    @Override
    public String getDefaultShippingMethodId() {
        Log.v(TAG, "getDefaultShippingMethodId");
        Log.d(TAG, "Default shipping method id: " + ShippingMethodsManager.INSTANCE.getDefaultShippingMethodId());
        return String.valueOf(ShippingMethodsManager.INSTANCE.getDefaultShippingMethodId());
    }

    /***
     * Gets the default shipping address ID.
     *
     * @return Default shipping address ID.
     */
    @Override
    public String getDefaultShippingAddressId() {
        Log.v(TAG, "getDefaultShippingAddressId");
        MasterPassAddress defaultShippingAddress = ShippingAddressesManager.INSTANCE.getDefaultShippingAddress();
        if (defaultShippingAddress != null) {
            Log.d(TAG, "Default shipping address id: " + defaultShippingAddress.getId());
            return defaultShippingAddress.getId();
        } else {
            Log.d(TAG, "Default shipping address id: null");
            return null;
        }
    }

    /***
     * Requests checkout initiation. Contacts Merchant Server to request token and initialise merchant.
     *
     * @param checkoutInitiationCallback Callback class that will be triggered from this method.
     */
    @Override
    public void checkoutInitiationRequest(final CheckoutInitiationCallback checkoutInitiationCallback) {
        Log.v(TAG, "checkoutInitiationRequest");

        // Make a call to Merchant Server
        DataManager.getInstance().getNetworkManager().makeApiCall(RestMethod.POST,
                ApiList.Method.API_REQUEST_TOKEN, null, new ResponseListener<RequestTokenResponse>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        checkoutInitiationCallback.onFailure();
                    }

                    @Override
                    public void onResponse(final RequestTokenResponse requestTokenResponse) {
                        // Prepare request object
                        MerchantInitializationRequest merchantInitRequest = new MerchantInitializationRequest();
                        merchantInitRequest.setRequestToken(requestTokenResponse.requestToken);
                        merchantInitRequest.setOriginUrl(Constants.DEFAULT_ORIGIN_URL);
                        // Prepare DSRP Data
                        CryptogramType cryptogramTypeEnum = PreferencesHelper.INSTANCE.getCryptogramTypeEnum(mContext);
                        Option option = null;
                        if (cryptogramTypeEnum == CryptogramType.UCAF) {
                            option = new Option(Constants.DEFAULT_BRAND_ID, "UCAF");
                        } else {
                            // In case ICC or UCAF_AND_ICC use ICC as it is stronger
                            option = new Option(Constants.DEFAULT_BRAND_ID, "ICC");
                        }
                        Log.d(TAG, "Requesting cryptogram: " + option.getAcceptanceType());
                        DSRPOptions dsrpOptions = new DSRPOptions(option);
                        // Set DSRP object
                        DSRP dsrp = new DSRP(dsrpOptions, null);// Empty UN, so Switch will generate one
                        merchantInitRequest.setExtensionPoint(new ExtensionPointMerchantInitialization(dsrp));
                        // Make a call to Merchant Server
                        DataManager.getInstance().getNetworkManager().makeApiCall(RestMethod.POST,
                                ApiList.Method.API_MERCHANT_INITIALIZATION,
                                merchantInitRequest,
                                new ResponseListener<MerchantInitializationResponse>() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, error.toString());
                                        checkoutInitiationCallback.onFailure();
                                    }

                                    @Override
                                    public void onResponse(MerchantInitializationResponse merchantInitializationResponse) {

                                        if (merchantInitializationResponse == null) {
                                            Log.d(TAG, "MerchantInitialization response is null");
                                            checkoutInitiationCallback.onFailure();
                                            return;
                                        }

                                        // Check if requestToken is the same that we passed in request
                                        if (!merchantInitializationResponse.requestToken.equalsIgnoreCase(requestTokenResponse.requestToken)) {
                                            Log.d(TAG, "Token returned in MerchantInitialization is different then requested");
                                            checkoutInitiationCallback.onFailure();
                                            return;
                                        }
                                        if (merchantInitializationResponse.extensionPoint != null
                                                && merchantInitializationResponse.extensionPoint.getUnpredictableNumber() != null
                                                && !merchantInitializationResponse.extensionPoint.getUnpredictableNumber().isEmpty()) {
                                            checkoutInitiationCallback.onSuccess(requestTokenResponse.requestToken, merchantInitializationResponse.extensionPoint.getUnpredictableNumber());
                                        } else {
                                            Log.e(TAG, "Switch didn't return Unpredictable Number. Cannot proceed");
                                            checkoutInitiationCallback.onFailure();
                                        }
                                    }
                                });

                    }
                });
    }

    /***
     * Gets the full amount data, including the cost of shipping using the supplied shipping method.
     *
     * @param masterPassAddress Shipping address.
     * @param selectedMethodId  Shipping method.
     * @param amountData        Existing amount data.
     * @return Updated amount data.
     * @throws MasterPassException
     */
    @Override
    public AmountData getFullAmountData(
            MasterPassAddress masterPassAddress,
            @Nullable String selectedMethodId,
            AmountData amountData)
            throws MasterPassException {
        Log.v(TAG, "getFullAmountData");

        // Check if existing AmountData is not null
        if (amountData == null) {
            throw new MasterPassException("custom-code", "Current AmountData is null");
        }

        // Get existing sub-total
        long oldTotal = amountData.getEstimatedTotal();

        // Get existing tax
        long taxValue = amountData.getTax();

        // If we have an address, update tax value
        if (masterPassAddress != null) {
            // Check if we have tax percentage for given address
            int taxPercentage = 0;
            if (Constants.TAX_MAP.containsKey(masterPassAddress.getAdminArea())) {
                taxPercentage = Constants.TAX_MAP.get(masterPassAddress.getAdminArea());
            }
            // Override with new value
            taxValue = Math.round(oldTotal * (taxPercentage / 100.0f));
        }
        // Add tax
        long newTotal = oldTotal + taxValue;

        if (selectedMethodId == null || selectedMethodId.isEmpty() || masterPassAddress == null) {
            // If shipping method has not been supplied, then we just return the amount data without
            // calculating extra shipping cost.
            return new AmountData(amountData.getEstimatedTotal(),
                    amountData.getTax(),
                    amountData.getTotal(),
                    amountData.getCurrency());
        }

        // Find selected shipping method
        ShippingMethod selectedShippingMethod = ShippingMethodsManager.INSTANCE.getShippingMethodById(selectedMethodId);

        // Check that a shipping method exists for the supplied ID
        if (selectedShippingMethod != null) {
            // Add shipping cost of the selected shipping method
            newTotal += CurrencyUtils.parseValue(selectedShippingMethod.getShippingCost());

            // Store the shipping cost
            DataManager.getInstance().mBasket.mShippingPrice = CurrencyUtils.parseValue(selectedShippingMethod.getShippingCost());

            // Set address that will be used for transaction (as temp, storing will be triggered after transaction authorised)
            ShippingAddressesManager.INSTANCE.setAddressChosenForTransaction(masterPassAddress);

            // Return the newly calculated amount data model
            return new AmountData(amountData.getEstimatedTotal(),
                    amountData.getTax(),
                    amountData.getTotal(),
                    amountData.getCurrency());

        } else {

            // Can't find the selected method - throw exception
            throw new MasterPassException("custom-code", "Can't find given shipping method with id=" + selectedMethodId);
        }
    }
}
