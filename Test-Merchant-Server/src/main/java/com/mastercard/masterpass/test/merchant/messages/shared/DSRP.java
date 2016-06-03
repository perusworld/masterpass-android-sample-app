package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"dsrpOptions", "unpredictableNumber"})
@XmlRootElement(name = "DSRP")
public class DSRP {
    @XmlElement(name = "DSRPOptions", required = false)
    private DSRPOptions dsrpOptions;
    @XmlElement(name = "UnpredictableNumber", required = false)
    private String unpredictableNumber;

    public DSRPOptions getDsrpOptions() {
        return dsrpOptions;
    }

    public void setDsrpOptions(DSRPOptions dsrpOptions) {
        this.dsrpOptions = dsrpOptions;
    }

    public String getUnpredictableNumber() {
        return unpredictableNumber;
    }

    public void setUnpredictableNumber(String unpredictableNumber) {
        this.unpredictableNumber = unpredictableNumber;
    }
}
