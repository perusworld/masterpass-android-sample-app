package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"authenticateMethod"})
@XmlRootElement(name = "AuthenticationOptions")
public class AuthenticationOptions {

    @XmlElement(name = "AuthenticateMethod")
    private String authenticateMethod;

    public String getAuthenticateMethod() {
        return authenticateMethod;
    }

    public void setAuthenticateMethod(String authenticateMethod) {
        this.authenticateMethod = authenticateMethod;
    }
}
