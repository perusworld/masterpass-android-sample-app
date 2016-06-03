package com.mastercard.masterpass.test.merchant.messages.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"data"})
@XmlRootElement(name = "DecryptionResponse")
public class SwitchDecryptionResponse {

	@XmlElement(name = "Data", required = true)
    protected String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
}
