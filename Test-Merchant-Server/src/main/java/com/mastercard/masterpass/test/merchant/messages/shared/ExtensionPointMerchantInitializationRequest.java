package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dsrp"})
@XmlRootElement(name = "ExtensionPoint")
public class ExtensionPointMerchantInitializationRequest {
    @XmlElement(name = "DSRP", required = false)
    private DSRP dsrp;

    public DSRP getDsrp() {
        return dsrp;
    }

    public void setDsrp(DSRP dsrp) {
        this.dsrp = dsrp;
    }
}
