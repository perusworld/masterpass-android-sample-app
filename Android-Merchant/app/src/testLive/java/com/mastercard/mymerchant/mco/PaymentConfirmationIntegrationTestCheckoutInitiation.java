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

import android.app.Application;
import android.content.Context;
import android.test.mock.MockApplication;
import android.test.mock.MockContext;

import com.android.volley.NetworkError;
import com.mastercard.masterpass.core.CryptogramType;
import com.mastercard.masterpass.merchant.CheckoutInitiationCallback;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.network.NetworkManager;
import com.mastercard.mymerchant.network.ResponseListener;
import com.mastercard.mymerchant.network.RestMethod;
import com.mastercard.mymerchant.network.api.ApiList;
import com.mastercard.mymerchant.network.api.model.ExtensionPointMerchantInitializationResponse;
import com.mastercard.mymerchant.network.api.request.BaseRequest;
import com.mastercard.mymerchant.network.api.response.BaseResponse;
import com.mastercard.mymerchant.network.api.response.MerchantInitializationResponse;
import com.mastercard.mymerchant.network.api.response.RequestTokenResponse;
import com.mastercard.mymerchant.util.PreferencesHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JMockit.class)
public class PaymentConfirmationIntegrationTestCheckoutInitiation {

    private static PaymentConfirmationIntegration pci = null;

    @BeforeClass
    public static void setUp() {
        new MockUp<NetworkManager>() {
            @Mock
            public void $init(Application app) {
                System.out.println("Mock constructor called 2");
            }
        };
        pci = new PaymentConfirmationIntegration(new MockContext());
    }

    @AfterClass
    public static void tearDown() {
        pci = null;
    }

    @Test
    public void testSuccess(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).extensionPoint.setUnpredictableNumber("3XtSZQ==");
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(true);
                assertEquals("24759876", requestToken);
                assertEquals("3XtSZQ==", unpredictableNumber);


            }

            @Override
            public void onFailure() {
                assertTrue(false);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testSuccessICC(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.ICC;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).extensionPoint.setUnpredictableNumber("3XtSZQ==");
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(true);
                assertEquals("24759876", requestToken);
                assertEquals("3XtSZQ==", unpredictableNumber);


            }

            @Override
            public void onFailure() {
                assertTrue(false);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testSuccessICCAndUCAF(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF_AND_ICC;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).extensionPoint.setUnpredictableNumber("3XtSZQ==");
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(true);
                assertEquals("24759876", requestToken);
                assertEquals("3XtSZQ==", unpredictableNumber);


            }

            @Override
            public void onFailure() {
                assertTrue(false);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testRequestTokenVolleyError(@Mocked final PreferencesHelper mock) throws Exception {

        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    listener.onErrorResponse(new NetworkError());

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    listener.onErrorResponse(new NetworkError());
                }

            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String s, String s1) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationVolleyError(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };

        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";
                    listener.onResponse((T) response);

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    listener.onErrorResponse(new NetworkError());
                }

            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String s, String s1) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationNullResponse(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = null;
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationWrongRequestToken(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "1234";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).extensionPoint.setUnpredictableNumber("3XtSZQ==");
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationNullExtensionPoint(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = null;
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationNullUnpredictableNumber(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }

    @Test
    public void testMerchantInitializationEmptyUnpredictableNumber(@Mocked final PreferencesHelper mock) throws Exception {

        new Expectations(1) {
            {
                mock.getCryptogramTypeEnum((Context) any);
                result = CryptogramType.UCAF;
            }
        };
        DataManager.getInstance().setNetworkManager(new NetworkManager(new MockApplication()) {
            @Override
            public <T extends BaseResponse> void makeApiCall(RestMethod method, ApiList.Method endpointName, BaseRequest request, ResponseListener<T> listener) {
                BaseResponse response = null;
                if (endpointName == ApiList.Method.API_REQUEST_TOKEN) {
                    response = new RequestTokenResponse();
                    ((RequestTokenResponse) response).requestToken = "24759876";

                } else if (endpointName == ApiList.Method.API_MERCHANT_INITIALIZATION) {
                    response = new MerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).requestToken = "24759876";
                    ((MerchantInitializationResponse) response).extensionPoint = new ExtensionPointMerchantInitializationResponse();
                    ((MerchantInitializationResponse) response).extensionPoint.setUnpredictableNumber("");
                }
                listener.onResponse((T) response);
            }
        });

        CheckoutInitiationCallback checkoutInitiationCallback = new CheckoutInitiationCallback() {
            @Override
            public void onSuccess(String requestToken, String unpredictableNumber) {
                assertTrue(false);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        };
        pci.checkoutInitiationRequest(checkoutInitiationCallback);
    }
}