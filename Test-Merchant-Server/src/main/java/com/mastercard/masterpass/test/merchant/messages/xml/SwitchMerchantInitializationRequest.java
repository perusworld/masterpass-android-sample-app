
package com.mastercard.masterpass.test.merchant.messages.xml;

import com.mastercard.masterpass.test.merchant.messages.shared.ExtensionPointMerchantInitializationRequest;

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
 *         &lt;element name="OriginUrl" type="{}NonEmptyString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"oAuthToken", "originUrl", "extensionPoint"})
@XmlRootElement(name = "MerchantInitializationRequest")
public class SwitchMerchantInitializationRequest {

    @XmlElement(name = "OAuthToken", required = true)
    protected String oAuthToken;
    @XmlElement(name = "OriginUrl", required = true)
    protected String originUrl;
    @XmlElement(name = "ExtensionPoint", required = false)
    protected ExtensionPointMerchantInitializationRequest extensionPoint;

    public String getoAuthToken() {
        return oAuthToken;
    }

    public void setoAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public ExtensionPointMerchantInitializationRequest getExtensionPointMerchantInitializationRequest() {
        return extensionPoint;
    }

    public void setExtensionPointMerchantInitializationRequest(ExtensionPointMerchantInitializationRequest extensionPointMerchantInitializationRequest) {
        this.extensionPoint = extensionPointMerchantInitializationRequest;
    }
}
