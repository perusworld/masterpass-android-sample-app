# Masterpass Android Checkout Sample App

## Overview

The Masterpass Merchant Android SDK enables developers to implement secure and native checkout experiences with a Masterpass-enabled Wallet App.

Here is a list of some of the Masterpass-enabled Wallet Apps. 

* [Masterpass by MasterCard](https://play.google.com/store/apps/details?id=com.mastercard.mp.wallet)
* [Fifth Third Masterpass](http://mstr.cd/fifththird)
* [KeyBank Masterpass](http://mstr.cd/keybank)

The merchant application submits a **Masterpass OAuth Token** and **Transaction Details** to the Merchant SDK. The Merchant SDK passes them to a Masterpass-enabled wallet application. After user is successfully authenticated, the Merchant SDK returns a **MasterPass OAuth Verifier** and a **Masterpass Checkout ID** to the merchant application.

The **Masterpass OAuth Verifier** and the **Masterpass Checkout ID** should be exchanged with the MasterPass backend to access secure payment information. 

## Use Cases

### Checkout with a Masterpass-enabled Wallet App

Recognized user with a Masterpass-enabled Wallet App with single card. 
![recognized-checkout-flow](https://cloud.githubusercontent.com/assets/3765458/17342110/a127b97c-58c5-11e6-8fc7-9c5a0aae3591.png)

### No MasterPass-enabled Wallet on user's device

Coming soon..

## Integration Tutorial

### 1. Set up your backend to communicate with MasterPass

See the [Test Merchant Server](https://github.com/MasterCard/masterpass-android-sample-app/tree/master/Test-Merchant-Server) for more informaiton

### 2. Add the Android Checkout SDK to your application

Copy the following files to your Android Project's app/libs folder:

    masterpass-core-release.aar
    masterpass-merchant-release.aar
    mobile-checkout-core-lib-release.aar
    mobile-checkout-merchant-lib-release.aar

The SDK will run on devices with Android 4.4 Kitkat and newer; however, to add the SDK to an Android application, the application must be compiled using Android API Level 23.

**Sample code for build.gradle file**

```java
// add the following to your application's build.gradle file
repositories {
    jcenter()
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile(name: 'masterpass-core-release', ext: 'aar')
    compile(name: 'masterpass-merchant-release', ext: 'aar')
    compile(name: 'mobile-checkout-core-lib-release', ext: 'aar')
    compile(name: 'mobile-checkout-merchant-lib-release', ext: 'aar')

    compile 'com.google.code.gson:gson:2.3'
    compile 'com.android.support:recyclerview-v7:23.0.0'
    compile 'com.android.support:design:23.0.0'
    compile 'com.mcxiaoke.volley:library:1.0.19'
    
    // other dependencies for your app ...
}
```

### 3. Initialize the SDK

To initialize the SDK, you need to provide `ApplicaitonContext`, `MerchantConfig`, `SessionKeySigningResponse`, and `InitializationListener`.

**Sample initialization code**

```java
// SessionKeySigning
SessionKeySigningRequest sessionKeySigningRequest = new SessionKeySigningRequest();
sessionKeySigningRequest.setAppId("...");       // app id assigned by masterpass
sessionKeySigningRequest.setAppVersion("...");  // app version assigned by masterpass
// the SessionKeySigningRequest public key should come from the SDK's CryptoUtil class
String publicKey = Base64.encodeToString(CryptoUtil.getSessionKeyPair().getPublic().getEncoded(), Base64.DEFAULT);
sessionKeySigningRequest.setAppSigningPublicKey(publicKey);

// You should get the SessionKeySigningResponse by calling 
// MasterPass SessionKeySigningServices via your merchant server

SessionKeySigningResponse signingResponse = new SessionKeySigningResponse();
signingResponse.setAppId("...");                // app id
signingResponse.setAppVersion("...");           // app version
signingResponse.setAppSigningPublicKey("...");  // must be Base64 encoded RSA public key sent in request
signingResponse.setSessionSignature("...");     // must be session signature received from MasterPass

// start SDK initialiation 
MasterPass masterpass = MasterPass.getInstance().init(
        getApplicationContext(),                                // application context
        new MasterPassMerchantConfig() (
            Locale.getDefault(),                                // locale
            EnumSet.allOf(MasterPassCard.PaymentNetwork.class), // payment networks supported
            CryptogramType.UCAF,                                // cryptogram type supported
            true,                                               // enable Analytics
            EnvironmentType.SANDBOX                             // MasterPass environment, SANDBOX / PROD
        ), 
        signingResponse,                                        // session key signing response from above
        new InitializationListener() {
            @Override
            public void initializationFailed(MasterPassException e) {
                Log.e(TAG, "Init MCO: initializationFailed");
            }
            
            @Override
            public void paymentMethodAvailable() {
                Log.i(TAG, "There is at least one MasterPass Available wallet on the device");
            }
        }
    );
```

### 4. Display the Buy with MasterPass button

To display the Buy with MasterPass button, you must provide `TransactionDetails`, `WebUrlCallback`, `PaymentConfirmationCallback`, `TransactionResultListener` and `DecryptionListener` to the SDK. See the Implement Transaction Callbacks and Listeners section for more details

**Sample code for retreiving MasterPassButton from the SDK**

```java
// Build transaction details object
TransactionDetails transactionDetails = new TransactionDetails();
transactionDetails.setIsShippingRequired(false); // shipping NOT required
transactionDetails.setAmountData(
        new AmountData(
                10000,      // long
                0,          // long
                10000,      // long
                Currency.getInstance(Locale.US))); 

MasterPass.getInstance().getMasterPassButton(
            transactionDetails,
            new WebUrlCallback() {
                @Override
                public void getAppToWebUrl(UriCallback uriCallback) {
                    uriCallback.completedWithUri(Uri.parse("url_to_web_checkout_link"));
                }
            },
            new PaymentConfirmationCallback() {
                // app to implement, see next section for more info
            },
            new TransactionResultListener() { 
                // app to implement, see next section for more info
            },
            new DecryptionListener() { 
                // app to implement, see next section for more info
            }
        );
```

**Sample code for adding LayoutParams and displaying MasterPassButton in your app**

```java
// Add layout parameters to MasterPassButton
RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
    RelativeLayout.LayoutParams.WRAP_CONTENT,
    RelativeLayout.LayoutParams.WRAP_CONTENT);
btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
btnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
masterPassButton.setLayoutParams(btnParams);

// Add MasterPassButton to your view
((RelativeLayout) getView().findViewById(R.id.layout_test_merchant_checkout)).addView(masterPassButton);
```

### 5. Implement Transaction Callbacks and Listeners
Two use cases are applicable for the SDK UI: **Shipping Not Required Transaction** and **Shipping Required Transaction**. Depending on the use case, the implementation of the callbacks and listeners may be different.

####PaymentConfirmationCallback
The SDK uses this interface to communicate data related to checkout with the merchant application.

**Sample code for PaymentConfirmationCallback (Shipping Not Required Transaction)**

```java
PaymentConfirmationCallback masterPassPaymentConfirmationCallback = new PaymentConfirmationCallback() {
    @Override
    public void checkoutInitiationRequest(CheckoutInitiationCallback callback) {
        // Upon submitting a payment, the SDK will ask you for a MasterPass Request Token and an Unpredictable Number
        // Merchant App should call Merchant backend to call MasterPass backend to retrieve them 
        // using the requestToken and Merchant-init service call
        callback.onSuccess("request_token", "unpredictable_number");

        // if Merchant is unable to get Request Token and UN from MasterPass
        // callback.onFailure();
    }
    
    // Below methods are not required to be implemented if shipping is not required
    @Override public List getUpdatedShippingMethodsList(@Nullable MasterPassAddress selectedAddress) { 
        return null; 
    } 
    @Override public List getShippingAddressesList(boolean limitExceeded) { 
        return null; 
    }
    @Override public String getDefaultShippingMethodId() { 
        return null; 
    }
    @Override public String getDefaultShippingAddressId() { 
        return null; 
    }
    @Override public AmountData getFullAmountData(@Nullable MasterPassAddress selectedAddress, 
        @Nullable String selectedMethodId, AmountData currentAmountData) throws MasterPassException { 
        return null; 
    }
};
```

**Sample code for PaymentConfirmationCallback (Shipping Required Transaction)**
```java
PaymentConfirmationCallback masterPassPaymentConfirmationCallback = new PaymentConfirmationCallback() {
    @Override 
    public void checkoutInitiationRequest(CheckoutInitiationCallback callback) {
        // Upon submitting a payment, the SDK will ask you for a MasterPass Request Token and an Unpredictable Number
        // Merchant App should call Merchant backend to call MasterPass backend to retrieve them 
        // using the requestToken and Merchant-init service call
        callback.onSuccess("request_token", "unpredictable_number");

        // if Merchant is unable to get Request Token and UN from MasterPass
        // callback.onFailure();
    }
    @Override
    public List getUpdatedShippingMethodsList(@Nullable MasterPassAddress selectedAddress) {
        /* app to provide a list of Shipping Methods based on the selected address for
           user to select, if any */
        String shippingMethodsValues = 
                "[" +
                "{\"id\":\"1\",\"title\":\"7 Day Ground\",\"description\":\"1\",\"shippingCost\":\"4.99\"}," +
                "{\"id\":\"2\",\"title\":\"USPS First Class\",\"description\":\"2\",\"shippingCost\":\"4.99\"}," +
                "{\"id\":\"3\",\"title\":\"Expedited\",\"description\":\"3\",\"shippingCost\":\"4.99\"}" +
                "]";

        ShippingMethod[] shippingMethodsArray = gson.fromJson(shippingMethodsValues,ShippingMethod[].class);
        return Arrays.asList(shippingMethodsArray);
    }
    @Override
    public List getShippingAddressesList(boolean limitExceeded) {
        /* app to provide a list of Shipping Addresses for user to select, if any */
        return null;
    }
    @Override
    public String getDefaultShippingMethodId() {
        /* app to provide id of a default Shipping Method, if any */
        return null;
    }
    @Override
    public String getDefaultShippingAddressId() {
        /* app to provide id of a default Shipping Address, if any */
        return null;
    }
    @Override
    public AmountData getFullAmountData(@Nullable MasterPassAddress selectedAddress,
        @Nullable String selectedMethodId, AmountData currentAmountData) throws MasterPassException {
        /* app to update new amount data based on a newly selected shipping
           method / address */
        double total = Double.valueOf(currentAmountData.getEstimatedTotal())
            + Double.valueOf(currentAmountData.getTax())
            + Double.valueOf("4.99");
        currentAmountData.setTotal(new DecimalFormat("00.00").format(total));
        return currentAmountData;
    }
};
```

#### TransactionResultListener
The SDK uses this interface to notify the Merchant App on the status of the transaction and returns AuthorizationResponse to the Merchant App.

**Sample code for TransactionResultListener**

```java
TransactionResultListener masterPassTransactionResultListener = new TransactionResultListener() {
    @Override
    public void transactionAuthorized(AuthorizationResponse response) {
        // Upon a transaction is authorized, the Merchant Backend can use the data inside AuthorizationResponse 
        // to access Checkout Data from MasterPass via the AccessToken Service and Checkout Service

        // Merchants App should send the Request Token, Verifier Token and Checkout ID to Merchant backend
        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this); // replace with your own Activity class
        builder.setTitle("Checkout Completed!")
            .setMessage("Request Token: " + response.getRequestToken() +
                        "\nVerifier Token: " + response.getVerifierToken() + 
                        "\nCheckout ID: " + response.getCheckoutId()).create().show();
    }

    @Override public void transactionInitiated() { 
        /* app to handle notification that transaction initiated */ 
    }
    @Override public void transactionCanceled() { 
        /* app to handle transaction canceled */ 
    }
    @Override public void transactionFailed(MasterPassException exception) { 
        /* app to handle transaction failed */ 
    }
    @Override public void transactionDetailsInputException(MasterPassException exception) {
        /* app to handle case of invalid transaction details */ 
    }
};
```

#### DecryptionListener (only if shipping required) 

The DecryptionListener is called by the merchant SDK when the Buy with MasterPass button is clicked, if shipping is required, to decrypt the shipping addresses. If shipping is not required, the merchant application can pass in null during initialization.

Merchant applicaiton should call Merchant backend to call MasterPass backend to decrypt the addresses. For MasterPass backend decryption API service documentation, please refer here.

```java
DecryptionListener masterPassDecryptionListener = new DecryptionListener() {    
    @Override
    public void decryptAddresses(String request, final DecryptionCallback decryptionCallback) {
        VolleyWebServiceManager manager = new VolleyWebServiceManager(getActivity(), 
            new WebServiceManagerCallback() {            
                @Override 
                public void onResponse(String response) {
                    decryptionCallback.onSuccess(response);
                }        
                @Override 
                public void onError(String errorCode) {
                    decryptionCallback.onFailure(errorCode);
                }
            }
        );    
        manager.sendRequest(
            "merchant_server_url_for_decryption", 
            request, 
            Request.Priority.HIGH
        );
    }
};
```

### 6. Testing

#### In-App Checkout

Please download the companion test wallet app to test your integration.

1. Install the companion Test Wallet Apps on an Android device.
2. Launch the companion Test Wallet Apps once to initialize it.
3. Install your app and launch it to initialize the Merchant SDK.
4. Launch your activity with MasterPassButton to start a transaction.


#### Web Checkout Fallback

See the [Test Merchant Server](https://github.com/MasterCard/masterpass-android-sample-app/blob/master/Test-Merchant-Server/README.md#web-checkout) for more details
