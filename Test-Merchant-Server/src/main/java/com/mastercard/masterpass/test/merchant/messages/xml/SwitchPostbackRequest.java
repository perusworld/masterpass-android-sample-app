
package com.mastercard.masterpass.test.merchant.messages.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0">
 * <xs:element name="MerchantTransactions" type="MerchantTransactions" />
 * <xs:complexType name="MerchantTransactions">
 * <xs:sequence>
 * <xs:element name="MerchantTransactions" type="MerchantTransaction" minOccurs="0" maxOccurs="unbounded" />
 * <xs:element name="ExtensionPointMerchantInitialization" type="ExtensionPointMerchantInitialization" minOccurs="0" />
 * </xs:sequence>
 * </xs:complexType>
 * <xs:complexType name="MerchantTransaction">
 * <xs:sequence>
 * <xs:element name="TransactionId" type="xs:string" />
 * <xs:element name="ConsumerKey" type="xs:string" minOccurs="0" />
 * <xs:element name="Currency" type="xs:string" />
 * <xs:element name="OrderAmount" type="xs:long" />
 * <xs:element name="PurchaseDate" type="xs:dateTime" />
 * <xs:element name="TransactionStatus" type="TransactionStatus" />
 * <xs:element name="ApprovalCode" type="xs:string" />
 * <xs:element name="PreCheckoutTransactionId" type="xs:string" minOccurs="0" />
 * <xs:element name="ExpressCheckoutIndicator" type="xs:boolean" minOccurs="0" />
 * <xs:element name="ExtensionPointMerchantInitialization" type="ExtensionPointMerchantInitialization" minOccurs="0" />
 * </xs:sequence>
 * </xs:complexType>
 * <xs:simpleType name="TransactionStatus">
 * <xs:restriction base="xs:string">
 * <xs:enumeration value="Success" />
 * <xs:enumeration value="Failure" />
 * </xs:restriction>
 * </xs:simpleType>
 * <xs:complexType name="ExtensionPointMerchantInitialization">
 * <xs:sequence>
 * <xs:any maxOccurs="unbounded" processContents="lax" namespace="##any" />
 * </xs:sequence>
 * <xs:anyAttribute />
 * </xs:complexType>
 * </xs:schema>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"merchantTransactions", "extensionPoint"})
@XmlRootElement(name = "MerchantTransactions")
public class SwitchPostbackRequest {

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
