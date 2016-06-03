package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"brandId", "acceptanceType"})
@XmlRootElement(name = "Option")
public class Option {
    @XmlElement(name = "BrandId", required = false)
    private String brandId;
    @XmlElement(name = "AcceptanceType", required = false)
    private String acceptanceType;

    public String getAcceptanceType() {
        return acceptanceType;
    }

    public void setAcceptanceType(String acceptanceType) {
        this.acceptanceType = acceptanceType;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
