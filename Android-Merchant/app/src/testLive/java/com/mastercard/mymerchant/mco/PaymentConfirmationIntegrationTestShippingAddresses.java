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

package com.mastercard.mymerchant.mco;

import android.location.Address;
import android.test.mock.MockContext;

import com.activeandroid.Model;
import com.mastercard.masterpass.core.MasterPassAddress;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.model.DbAddressModel;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import static mockit.Deencapsulation.newInstance;
import static org.junit.Assert.assertEquals;


@RunWith(JMockit.class)
public class PaymentConfirmationIntegrationTestShippingAddresses {

    private static PaymentConfirmationIntegration pci = null;

    @BeforeClass
    public static void setUp() {

        new MockUp<Model>() {
            @Mock
            public void $init() {
            }

        };
        new MockUp<DbAddressModel>() {
            @Mock
            public void $init() {
            }

            @Mock
            public String toString() {
                return "";
            }

        };
        pci = new PaymentConfirmationIntegration(new MockContext());

    }

    @AfterClass
    public static void tearDown() {
        pci = null;
    }

    @Test
    public void testGetShippingAddressesList(@Mocked final ShippingAddressesManager mock) {

        // number of iteration, number of times that mocked method are expected to be invoked
        new Expectations(2) {
            {
                DbAddressModel myDbModel = new DbAddressModel();
                myDbModel.firstName = "John";
                myDbModel.lastName = "Bee";
                myDbModel.addressLine1 = "Purchase";
                myDbModel.addressLine2 = "11";
                myDbModel.city = "New York";
                myDbModel.isDefault = false;
                myDbModel.postcode = "87786";
                myDbModel.state = "NY";
                DbAddressModel myDbModel2 = new DbAddressModel();
                myDbModel2.firstName = "John";
                myDbModel2.lastName = "Bee";
                myDbModel2.addressLine1 = "Purchase";
                myDbModel2.addressLine2 = "11";
                myDbModel2.city = "New York";
                myDbModel2.isDefault = false;
                myDbModel2.postcode = "87786";
                myDbModel2.state = "NY";
                ArrayList<DbAddressModel> dbAddressModelList = new ArrayList<DbAddressModel>();
                dbAddressModelList.add(myDbModel);
                dbAddressModelList.add(myDbModel2);
                mock.getAddresses();
                result = dbAddressModelList;

                List<MasterPassAddress> addresses = new ArrayList<>();
                MasterPassAddress o = newInstance("com.mastercard.masterpass.core.MasterPassAddress", Locale.US);
                o.setPostalCode("87786");
                Deencapsulation.setField(o, "firstName", "John");
                addresses.add(o);
                MasterPassAddress o2 = newInstance("com.mastercard.masterpass.core.MasterPassAddress", Locale.US);
                o2.setPostalCode("87786");

                addresses.add(o2);
                mock.dbAddressListToMasterPassAddressList((List<DbAddressModel>) any);
                result = addresses;


            }
        };
        new MockUp<MasterPassAddress>() {
            @Mock
            public void $init(Locale locale) {
                System.out.println("Mock constructor called 2");
            }
        };


        List<DbAddressModel> addresses = mock.getAddresses();
        assertEquals(2, addresses.size());
        List<MasterPassAddress> masterPassAddresses = mock.dbAddressListToMasterPassAddressList(addresses);
        assertEquals(2, masterPassAddresses.size());

        List<MasterPassAddress> shippingAddressesList = pci.getShippingAddressesList(false);
        assertEquals(2, shippingAddressesList.size());
        assertEquals("John", shippingAddressesList.get(0).getFirstName());
    }

    public final class MockAddress extends MockUp<Address> {
        private String adminArea;
        private HashMap<Integer, String> mAddressLines;
        private int mMaxAddressLineIndex = -1;
        private String mPostalCode;

        @Mock
        public void $init(Locale locale) {
            System.out.println("Mock constructor called MasterPassAddress");
        }

        @Mock
        public void setAdminArea(String adminArea) {
            this.adminArea = adminArea;
        }

        @Mock
        public String getAdminArea() {
            return adminArea;
        }

        public String getPostalCode() {
            return mPostalCode;
        }

        public void setPostalCode(String postalCode) {
            mPostalCode = postalCode;
        }

        public String getAddressLine(int index) {
            if (index < 0) {
                throw new IllegalArgumentException("index = " + index + " < 0");
            }
            return mAddressLines == null ? null : mAddressLines.get(index);
        }

        public void setAddressLine(int index, String line) {
            if (index < 0) {
                throw new IllegalArgumentException("index = " + index + " < 0");
            }
            if (mAddressLines == null) {
                mAddressLines = new HashMap<Integer, String>();
            }
            mAddressLines.put(index, line);

            if (line == null) {
                // We've eliminated a line, recompute the max index
                mMaxAddressLineIndex = -1;
                for (Integer i : mAddressLines.keySet()) {
                    mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, i);
                }
            } else {
                mMaxAddressLineIndex = Math.max(mMaxAddressLineIndex, index);
            }
        }
    }

    public final class MockMasterPassAddress extends MockUp<MasterPassAddress> {
        private String adminArea;
        private String firstName;
        private String lastName;

        @Mock
        public void $init(Locale locale) {
            System.out.println("Mock constructor called MasterPassAddress");
        }

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

        @Mock
        public void setAdminArea(String adminArea) {
            this.adminArea = adminArea;
        }

        @Mock
        public String getAdminArea() {
            return adminArea;
        }

        @Mock
        public String getId() {
            String addressIdSource = this.firstName.toUpperCase() + this.lastName.toUpperCase()/* + this.getAddressLine(0).toUpperCase() + this.getPostalCode().toUpperCase()*/;
            byte[] bytes = addressIdSource.getBytes();
            String var10000 = getSHA256Hash(bytes);
            return var10000;
        }

        private String getSHA256Hash(byte[] message) {
            String SHA256 = "SHA-256";

            String var10000;
            try {

                byte[] e = MessageDigest.getInstance("SHA-256").digest(message);
                var10000 = bytesToHex(e);
            } catch (Exception var4) {
                var4.printStackTrace();

                return null;
            }


            return var10000;
        }

        private String bytesToHex(byte[] bytes) {
            char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            int j = 0;

            for (; j < bytes.length; ) {
                int v = bytes[j] & 255;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
                ++j;
            }

            String var10000 = new String(hexChars);
            return var10000;
        }
    }

    @Test
    public void testGetDefaultShippingAddressId(@Mocked final ShippingAddressesManager mock) throws Exception {


        // Prepare MasterPassAddress
        MockMasterPassAddress mockMasterPassAddress = new MockMasterPassAddress();
        final MasterPassAddress masterPassAddress = newInstance("com.mastercard.masterpass.core.MasterPassAddress", Locale.US);
        masterPassAddress.setPostalCode("2345");
        masterPassAddress.setLocality("City");
        masterPassAddress.setAddressLine(0, "addr");
        masterPassAddress.setAddressLine(1, "addr 2");
        masterPassAddress.setCountryCode("US");
        masterPassAddress.setCountryName("US");
        masterPassAddress.setAdminArea("NY");
        mockMasterPassAddress.setFirstName("John");
        mockMasterPassAddress.setLastName("Dee");



        new Expectations(1) {
            {
                mock.getDefaultShippingAddress();
                result = masterPassAddress;
            }
        };
        String defaultShippingAddressId = pci.getDefaultShippingAddressId();
        assertEquals("8F7F7A0F087348BDF1F9E2BCF044148EAE6589885FF4DCC6389BAB11691E066D", defaultShippingAddressId);

        new Expectations(1) {
            {
                mock.getDefaultShippingAddress();
                result = null;
            }
        };
        defaultShippingAddressId = pci.getDefaultShippingAddressId();
        assertEquals(null, defaultShippingAddressId);

    }

}