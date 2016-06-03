package com.mastercard.masterpass.test.merchant.messages.shared;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"firstName", "lastName", "nationalId", "country", "emailAddress", "phoneNumber"})
@XmlRootElement(name = "Contact")
public class Contact {

    @XmlElement(name = "FirstName")
    private String firstName;

    @XmlElement(name = "LastName")
    private String lastName;

    @XmlElement(name = "NationalId")
    private String nationalId;

    @XmlElement(name = "Country")
    private String country;

    @XmlElement(name = "EmailAddress")
    private String emailAddress;

    @XmlElement(name = "PhoneNumber")
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
