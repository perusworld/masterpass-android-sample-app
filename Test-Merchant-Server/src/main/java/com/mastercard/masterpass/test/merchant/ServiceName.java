package com.mastercard.masterpass.test.merchant;

/**
 * List of Switch endpoints to which merchant server talks
 */
public enum ServiceName {
    REQUEST_TOKEN,
    MERCHANT_INITIALIZATION,
    ACCESS_TOKEN,
    CHECKOUT_RESOURCE,
    SESSION_KEY_SIGNING,
    POSTBACK,
    DECRYPTION
}
