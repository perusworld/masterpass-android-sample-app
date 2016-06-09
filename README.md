# Masterpass Android Checkout Sample App

## Overview

The MasterPass Merchant Android SDK enables developers to quickly implement secure and native checkout experiences within Android applications.

The merchant application submits a MasterPass Payment Request to the Merchant SDK, which then sends the request to a MasterPass-enabled wallet application. Afterwards, the Merchant SDK returns a MasterPass Payment Verifier Token to the merchant application.

The tokens should be exchanged with the MasterPass backend to access encrypted payment information. 

## Use Cases

### One or more MasterPass-enabled Wallets on user's android phone



### No MasterPass-enabled Wallet on user's android phone

## Integration

### 1. Set up your backend to communicate with MasterPass

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
~~~

### 3. Initialize the SDK

To initialize the SDK, you need to provide `ApplicaitonContext`, `MerchantConfig`, `SessionKeySigningResponse`, and `InitializationListener`.

Sample initialization code

~~~java
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
            "SANDBOX"                                           // MasterPass environment, SANDBOX / PROD
        ), 
        signingResponse,                                        // session key signing response from above
        new InitializationListener() {
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
        }
    );
```

### 4. Display the Buy with MasterPass button

### 5. Implement transaction callbacks

### 6. Testing

#### In-App Checkout
#### Web Checkout Fallback
