package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for decryption request message returned to mobile app from merchant server
 */
public class DecryptionResponse {

	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [data=" + data + "]";
	}
}
