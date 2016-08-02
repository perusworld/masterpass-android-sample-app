#Test Merchant Server Overview

## Capabilities of Test Merchant Server

Test Merchant Server is responsible for handling the communication between the Test Merchant App and the MasterPass Switch throughout the checkout flow.

You can use this as a test backend server to run the Test Merchant App.

Services provided by Test Merchant Server are:

* Access Token
* Checkout Resources
* Merchant Initialization
* Postback
* Request Token
* Session Key Signing
* Version
* Web checkout

## Set up

Edit the config.properties file under `src/main/resources`

~~~
sandbox.p12.file.name= 	// file name of the p12 file onboarded to MasterCard OpenAPI
sandbox.p12.key.alias= 	// alias of key
sandbox.p12.password= 	// password of key
sandbox.consumer.key= 	// consumer key received from MasterCard OpenAPI
~~~

## Create .war file from Test Merchant Server source code

After the configuration, you can generate a war file using maven. 

Run the following command from this project's root directory

	mvn package

The generated war file will be in the target folder

	Test-Merchant-Server/target

# API Reference

## Access Token Service

**Resource URL**

POST /accessToken

**Sample Request**

POST /accessToken HTTP/1.1

Content-Type: application/json; charset=utf-8

Host: localhost:9998

~~~json
{
    "oauthToken": "ac2e19e217017d73dfbfa4e593718adb9e88e506",
    "oauthVerifier": "21420f0ba08cf8a68cf7c7dee8e97e7e50e610f5"
}
~~~

**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
    "accessToken": "02bbcd73d41ed06d565e4ed4cee375539f31391e"
}
~~~

## Checkout Resources Service

**Resource URL**

POST /checkoutResource

**Sample Request**

POST /checkoutResource HTTP/1.1

Content-Type: application/json; charset=utf-8

Host: localhost:9998
    
~~~json
{
    "accessToken": "02bbcd73d41ed06d565e4ed4cee375539f31391e",
    "checkoutId": "63869182"
}
~~~

**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
   "authenticationOptions": {
      "AuthenticateMethod": "NO AUTHENTICATION"
   },
   "card": {
      "AccountNumber": "5413850000000040",
      "BillingAddress": {
         "City": "New York",
         "Country": "US",
         "CountrySubdivision": "US-NY",
         "Line1": "114 5th Ave",
         "Line2": "FL 10",
         "Line3": "address line 3",
         "PostalCode": "10011"
      },
      "BrandId": "master",
      "BrandName": "Mastercard",
      "CardHolderName": "John Doe",
      "ExpiryMonth": 12,
      "ExpiryYear": 2016
   },
   "contact": {
      "Country": "US",
      "EmailAddress": "johndoe@mastercard.com",
      "FirstName": "John",
      "LastName": "Doe",
      "NationalId": null,
      "PhoneNumber": "555-555-5555"
   },
   "transactionId": 72518828,
   "walletId": 0
}
~~~

## Merchant Initialization Service

**Resource URL**

POST /merchantInitialization

**Sample Request**

POST /merchantInitialization HTTP/1.1

Content-Type: application/json; charset=utf-8

Host: localhost:9998    
 
~~~json
{
    "requestToken": "ac2e19e217017d73dfbfa4e593718adb9e88e506",
    "originUrl": "http://example.com",
    "extensionPoint":{
        "DSRP":{
            "DSRPOptions":{
                "Option":{
                    "AcceptanceType":"UCAF",
                    "BrandId":"master"
                }
            },
            "UnpredictableNumber":"jmTWRw=="
        }
    }
}
~~~


**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
    "requestToken": "ac2e19e217017d73dfbfa4e593718adb9e88e506",
    "extensionPoint":{
        "UnpredictableNumber":"jmTWRw=="
        }
}
~~~


## Postback Service

**Resource URL**

POST /postback

**Sample Request**

POST /postback HTTP/1.1

Content-Type: application/json; charset=utf-8

Host: localhost:9998
    
~~~json
{
   "currency": "USD",
   "orderAmount": "1500",
   "purchaseDate": "2016-01-08T14:52:57.539-05:00",
   "transactionId": "72619926",
   "transactionStatus": "Success"
}
~~~

**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
    "isSuccess": true
}
~~~


## Request Token Service

**Resource URL**

POST /requestToken

**Sample Request**

POST /requestToken HTTP/1.1

Host: localhost:9998

~~~
~~~

**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
    "requestToken": "ac2e19e217017d73dfbfa4e593718adb9e88e506"
}
~~~

## Session Key Signing Service

**Resource URL**

POST /sessionKeySigning

**Sample Request**

POST /sessionKeySigning HTTP/1.1

Content-Type: application/json; charset=utf-8

Host: localhost:9998
    
~~~json 
{
    "appId": "definitelydefinitelydefinitelydefinitely",
    "appVersion": "str1234000000000000000000",
    "appSigningPublicKey": "VGhpcyBpcyB5b3VyIHB1YmxpYyBrZXk="
}
~~~


**Sample Response**

HTTP/1.1 200 OK

content-type: application/json; charset=utf-8

~~~json
{
    "appId": "definitelydefinitelydefinitelydefinitely",
    "appVersion": "str1234000000000000000000",
    "appSigningPublicKey": "VGhpcyBpcyB5b3VyIHB1YmxpYyBrZXk=",
    "sessionSignature": "Ldc8KN/iif1p2768ECLVLaIZ3CB11rYzE4rGAO1vCYQ="
}
~~~

## Version Service

**Resource URL**

GET /version

**Sample Response**

HTTP/1.1 200 OK

content-type: text/plain; charset=utf-8

	29/12/2015 3.46PM

## Web checkout

**URL**

/initialize.html

/callback.html

**Description**

Opening these two pages in browser will trigger a web checkout
