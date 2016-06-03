package com.mastercard.masterpass.test.merchant.messages.json;

/**
 * Model for decryption request message posted from mobile app to merchant server
 */
public class DecryptionRequest {

	private String data;
	private String dataEncryptionKey;
	private String dataEncryptionIV;
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDataEncryptionKey() {
		return dataEncryptionKey;
	}

	public void setDataEncryptionKey(String dataEncryptionKey) {
		this.dataEncryptionKey = dataEncryptionKey;
	}

	public String getDataEncryptionIV() {
		return dataEncryptionIV;
	}

	public void setDataEncryptionIV(String dataEncryptionIV) {
		this.dataEncryptionIV = dataEncryptionIV;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [data=" + data + ", dataEncryptionKey=" + dataEncryptionKey + ", dataEncryptionIV="
				+ dataEncryptionIV + "]";
	}

}
