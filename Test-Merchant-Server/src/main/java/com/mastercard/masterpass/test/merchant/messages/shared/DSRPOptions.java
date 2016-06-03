package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"option"})
@XmlRootElement(name = "DSRPOptions")
public class DSRPOptions {
    @XmlElement(name = "Option", required = false)
    private Option option;

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
