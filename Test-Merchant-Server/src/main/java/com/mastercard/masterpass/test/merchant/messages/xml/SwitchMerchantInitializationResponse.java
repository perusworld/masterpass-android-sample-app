
package com.mastercard.masterpass.test.merchant.messages.xml;

import com.mastercard.masterpass.test.merchant.messages.shared.ExtensionPointMerchantInitializationResponse;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OAuthToken" type="{}NonEmptyString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"oAuthToken", "extensionPoint"})
@XmlRootElement(name = "MerchantInitializationResponse")
public class SwitchMerchantInitializationResponse {

    @XmlElement(name = "OAuthToken", required = true)
    protected String oAuthToken;
    @XmlElement(name = "ExtensionPoint", required = true)
    protected ExtensionPointMerchantInitializationResponse extensionPoint;

    public String getoAuthToken() {
        return oAuthToken;
    }

    public void setoAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public ExtensionPointMerchantInitializationResponse getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(ExtensionPointMerchantInitializationResponse extensionPoint) {
        this.extensionPoint = extensionPoint;
    }
}
