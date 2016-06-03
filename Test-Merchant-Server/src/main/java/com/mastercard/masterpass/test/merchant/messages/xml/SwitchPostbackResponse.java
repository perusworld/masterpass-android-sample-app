
package com.mastercard.masterpass.test.merchant.messages.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"merchantTransactions", "extensionPoint"})
@XmlRootElement(name = "MerchantTransactions")
public class SwitchPostbackResponse {

    @XmlElement(name = "MerchantTransactions", required = false)
    protected List<MerchantTransactions> merchantTransactions;

    @XmlElement(name = "ExtensionPoint", required = false)
    protected String extensionPoint;

    public List<MerchantTransactions> getMerchantTransactions() {
        return merchantTransactions;
    }

    public void setMerchantTransactions(List<MerchantTransactions> merchantTransactions) {
        this.merchantTransactions = merchantTransactions;
    }

    public String getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(String extensionPoint) {
        this.extensionPoint = extensionPoint;
    }
}
