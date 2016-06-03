===================
= MCO Integration =
===================

All places where the sample application integrates with the MCO SDK are tagged in the code with '[MCO-SDK]'.
To easily find them, the developer can search the project for exact matches (In Android Studio, use Ctrl+Shift+F or Edit->Find->Find in path...).

The key integration points are:
- Initialization: This is performed in "StartActivity" so it is invoked every time the app is lunched
- MasterPass Button: This is set up, retrieved and displayed in "CheckoutActivity"
- "CheckoutActivity" also creates the "TransactionDetails" object which is used to start an MCO transaction and the "TransactionResultListener" callbacks
  which are used to provide status updates
- The "PaymentConfirmationIntegration" class provides an implementation of MCO callbacks during checkout. For example - choosing a shipping address


Notes:
- When building the app, make sure "Test Artifact" is set to "Unit Test" in "Build Variants" window. 

======================
=   BEFORE Building  =
======================

Before building apps from source code, the URL pointing to Test Merchant Server has to be edited.
It is located in app/build.gradle file as MERCHANT_SERVER_URL build config field. 
The APP_ID_FOR_SWITCH is unique guid given by MasterCard for requested application ID (package name).