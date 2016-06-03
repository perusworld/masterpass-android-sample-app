package com.mastercard.masterpass.test.merchant.messages.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "data", "dataEncryptionKey", "dataEncryptionIV" })
@XmlRootElement(name = "DecryptionRequest")
public class SwitchDecryptionRequest {

	@XmlElement(name = "Data", required = true)
	protected String data;
	@XmlElement(name = "DataEncryptionKey", required = true)
	protected String dataEncryptionKey;
	@XmlElement(name = "DataEncryptionIV", required = true)
	protected String dataEncryptionIV;

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

}
