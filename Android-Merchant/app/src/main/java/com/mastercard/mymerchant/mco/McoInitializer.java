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
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mastercard.mymerchant.BuildConfig;
import com.mastercard.mymerchant.Config;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.network.ResponseListener;
import com.mastercard.mymerchant.network.RestMethod;
import com.mastercard.mymerchant.network.api.ApiList;
import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.core.SessionKeySigningRequest;
import com.mastercard.masterpass.core.SessionKeySigningResponse;
import com.mastercard.masterpass.mc.core.crypto.CryptoUtil;
import com.mastercard.masterpass.mc.merchant.mpswitch.MockLocalMasterPassSwitchServices;
import com.mastercard.masterpass.merchant.InitializationListener;
import com.mastercard.masterpass.merchant.MasterPass;
import com.mastercard.masterpass.merchant.MasterPassMerchantConfig;

/**
 * Used to initialize the [MCO-SDK] library.
 */
public class McoInitializer {
    /**
     * Logging TAG
     */
    private static final String TAG = McoInitializer.class.getName();

    /**
     * App context
     */
    private Context applicationContext;

    /**
     * Method that create config object and initialize MCO-SDK library live version
     *
     * @param applicationContext app context
     * @throws MasterPassException
     */
    public void initLiveMCO(final Context applicationContext) throws MasterPassException {
        Log.d(TAG, "initLiveMCO");
        this.applicationContext = applicationContext;
        // Create MasterPass Merchant Config
        final MasterPassMerchantConfig masterPassMerchantConfig = Config.getMasterPassWalletConfig(applicationContext);

        Log.d(TAG, "MasterPassMerchantConfig [" + masterPassMerchantConfig.getLocale() + ", " +
                masterPassMerchantConfig.getAllowedNetworkTypes().toString() + ", " +
                masterPassMerchantConfig.getSupportedCryptogramType() + "]");
        // For live use real servers
        // Create public key by simple Base64 encoding
        String publicKey = Base64.encodeToString(CryptoUtil.getSessionKeyPair().getPublic().getEncoded(), Base64.DEFAULT);

        // Call Merchant Server to get Session Key Signing (Signature used for further communication)
        DataManager.getInstance().getNetworkManager().makeApiCall(RestMethod.POST,
                ApiList.Method.API_SESSION_KEY_SIGNING,
                new com.mastercard.mymerchant.network.api.request.SessionKeySigningRequest(BuildConfig.APP_ID_FOR_SWITCH, BuildConfig.VERSION_NAME, publicKey),
                new ResponseListener<com.mastercard.mymerchant.network.api.response.SessionKeySigningResponse>() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(applicationContext, "Failed to contact the Test Merchant Server. Please restart the app.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(com.mastercard.mymerchant.network.api.response.SessionKeySigningResponse response) {
                        Log.d(TAG, "Signature: " + response.sessionSignature);
                        // Attempt to initialize the MCO SDK.
                        try {
                            MasterPass.getInstance().init(
                                    applicationContext,
                                    masterPassMerchantConfig,
                                    convertResponse(response),
                                    mInitializationListener);
                        } catch (MasterPassException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Initialization listener that defines behaviour to occur at certain initialization states.
     */
    private final InitializationListener mInitializationListener = new InitializationListener() {
        @Override
        public void initializationFailed(MasterPassException e) {
            Log.e(TAG, "Init MCO: initializationFailed");
            e.printStackTrace();
            DataManager.getInstance().setMcoInitialized(false);
            // Notify user
            Toast.makeText(applicationContext, "MCO initialization failed. Please restart the app. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void paymentMethodAvailable() {
            Log.i(TAG, "There is at least one MasterPass Available wallet on the device");
        }
    };


    /**
     * Converts our model to MCO model for SessionKeySigning response model
     *
     * @param response our model
     * @return MCO model
     */
    private SessionKeySigningResponse convertResponse(com.mastercard.mymerchant.network.api.response.SessionKeySigningResponse response) {
        SessionKeySigningResponse result = new SessionKeySigningResponse();
        result.setAppId(response.appId);
        result.setAppVersion(response.appVersion);
        result.setAppSigningPublicKey(response.appSigningPublicKey);
        result.setSessionSignature(response.sessionSignature);
        return result;
    }

    /**
     * Build SessionKeySigningResponse object.
     * In this version it uses 'Mock Switch' to get valid object
     *
     * @param applicationContext app context
     * @return SessionKeySigningResponse object
     * @throws MasterPassException
     */
    private SessionKeySigningResponse requestMockSessionKeySigning(Context applicationContext) throws MasterPassException {
        // Create public key by simple Base64 encoding
        String publicKey = Base64.encodeToString(CryptoUtil.getSessionKeyPair().getPublic().getEncoded(), Base64.DEFAULT);

        // Build session key signing request
        SessionKeySigningRequest sessionKeySigningRequest = new SessionKeySigningRequest();
        sessionKeySigningRequest.setAppVersion(BuildConfig.VERSION_NAME);
        sessionKeySigningRequest.setAppId(applicationContext.getPackageName());
        sessionKeySigningRequest.setAppSigningPublicKey(publicKey);

        // Request for signing the key
        MockLocalMasterPassSwitchServices switchServices = new MockLocalMasterPassSwitchServices();
        return switchServices.getSessionKeySigningResponse(sessionKeySigningRequest);
    }
}
