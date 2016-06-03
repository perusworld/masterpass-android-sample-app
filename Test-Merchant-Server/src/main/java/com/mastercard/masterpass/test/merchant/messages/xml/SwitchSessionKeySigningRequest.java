
package com.mastercard.masterpass.test.merchant.messages.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="AppId" type="{}NonEmptyString"/>
 *         &lt;element name="AppVersion" type="{}NonEmptyString"/>
 *         &lt;element name="AppSigningPublicKey" type="{}NonEmptyString"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"appId", "appVersion", "appSigningPublicKey"})
@XmlRootElement(name = "SessionKeySigningRequest")
public class SwitchSessionKeySigningRequest {

    @XmlElement(name = "AppId", required = true)
    protected String appId;
    @XmlElement(name = "AppVersion", required = true)
    protected String appVersion;
    @XmlElement(name = "AppSigningPublicKey", required = true)
    protected String appSigningPublicKey;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppSigningPublicKey() {
        return appSigningPublicKey;
    }

    public void setAppSigningPublicKey(String appSigningPublicKey) {
        this.appSigningPublicKey = appSigningPublicKey;
    }

}
