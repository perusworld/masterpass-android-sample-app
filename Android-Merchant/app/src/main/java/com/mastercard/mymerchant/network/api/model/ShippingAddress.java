/*
 *    ****************************************************************************
 *    Copyright (c) 2015, MasterCard International Incorporated and/or its
 *    affiliates. All rights reserved.
 *    <p/>
 *    The contents of this file may only be used subject to the MasterCard
 *    Mobile Payment SDK for MCBP and/or MasterCard Mobile MPP UI SDK
 *    Materials License.
 *    <p/>
 *    Please refer to the file LICENSE.TXT for full details.
 *    <p/>
 *    TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS", WITHOUT
 *    WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *    WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *    NON INFRINGEMENT. TO THE EXTENT PERMITTED BY LAW, IN NO EVENT SHALL
 *    MASTERCARD OR ITS AFFILIATES BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *    IN THE SOFTWARE.
 *    *****************************************************************************
 */

package com.mastercard.mymerchant.network.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model representing shipping address details
 */
public class ShippingAddress implements Serializable {

    @SerializedName("City")
    private String city;

    @SerializedName("Country")
    private String country;

    @SerializedName("CountrySubdivision")
    private String countrySubdivision;

    @SerializedName("Line1")
    private String line1;

    @SerializedName("Line2")
    private String line2;

    @SerializedName("Line3")
    private String line3;

    @SerializedName("PostalCode")
    private String postalCode;

    @SerializedName("RecipientName")
    private String recipientName;

    @SerializedName("RecipientPhoneNumber")
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
