package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"unpredictableNumber"})
@XmlRootElement(name = "ExtensionPoint")
public class ExtensionPointMerchantInitializationResponse {
    @XmlElement(name = "UnpredictableNumber", required = false)
    private String unpredictableNumber;

    public String getUnpredictableNumber() {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(String unpredictableNumber) {
        this.unpredictableNumber = unpredictableNumber;
    }

    @Override
    public String toString() {
        return "[unpredictableNumber=" + unpredictableNumber + "]";
    }
}
