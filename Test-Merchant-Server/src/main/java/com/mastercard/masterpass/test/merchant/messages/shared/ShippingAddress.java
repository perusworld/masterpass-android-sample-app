package com.mastercard.masterpass.test.merchant.messages.shared;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"city", "country", "countrySubdivision", "line1", "line2", "line3", "postalCode", "recipientName", "recipientPhoneNumber"})
@XmlRootElement(name = "ShippingAddress")
public class ShippingAddress {

    @XmlElement(name = "City")
    private String city;

    @XmlElement(name = "Country")
    private String country;

    @XmlElement(name = "CountrySubdivision")
    private String countrySubdivision;

    @XmlElement(name = "Line1")
    private String line1;

    @XmlElement(name = "Line2")
    private String line2;

    @XmlElement(name = "Line3")
    private String line3;

    @XmlElement(name = "PostalCode")
    private String postalCode;

    @XmlElement(name = "RecipientName")
    private String recipientName;

    @XmlElement(name = "RecipientPhoneNumber")
    private String recipientPhoneNumber;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
