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

import com.mastercard.masterpass.core.MasterPassAddress;
import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.merchant.AmountData;
import com.mastercard.masterpass.merchant.ShippingMethod;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mockit.Mock;
import mockit.MockUp;

import static mockit.Deencapsulation.newInstance;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class PaymentConfirmationIntegrationTest {

    private static PaymentConfirmationIntegration pci = null;

    @BeforeClass
    public static void setUp() {
        pci = new PaymentConfirmationIntegration(new MockContext());
    }

    @AfterClass
    public static void tearDown() {
        pci = null;
    }

    @Test
    public void testGetUpdatedShippingMethodsList() throws Exception {
        List<ShippingMethod> updatedShippingMethodsList = pci.getUpdatedShippingMethodsList(null);
        assertTrue(updatedShippingMethodsList.size() > 0);

        // Check if ids are unique
        HashMap<String, Boolean> exists = new HashMap<>();
        for (ShippingMethod shippingMethod : updatedShippingMethodsList) {
            String id = shippingMethod.getId();
            assertTrue(exists.containsKey(id) == false);
            exists.put(id, true);
        }
    }

    @Test
    public void testGetDefaultShippingMethodId() throws Exception {
        String defaultShippingMethodId = pci.getDefaultShippingMethodId();

        assertEquals("1", defaultShippingMethodId);
    }

    public final class MockAddress extends MockUp<Address>
    {
        private String adminArea;

        @Mock
        public void setAdminArea(String adminArea) {
            this.adminArea = adminArea;
        }

        @Mock
        public String getAdminArea() {
            return adminArea;
        }
    }

    public final class MockMasterPassAddress extends MockUp<MasterPassAddress>
    {
        private String adminArea;

        @Mock
        public void $init(Locale locale)
        {
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


    }


    @Test
    public void testGetFullAmountData() throws Exception {

        try {
            // Null all
            pci.getFullAmountData(null, null, null);
            assertTrue(false);
        } catch (MasterPassException e) {
            assertTrue(true);
        }

        // Null shipping address and shipping method, result amount should be the same as input
        AmountData amountData = new AmountData(Long.parseLong("10"), Long.parseLong("5"), Long.parseLong("15"), Currency.getInstance("USD"));
        AmountData newAmountData = pci.getFullAmountData(null, null, amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(amountData.getTotal(), newAmountData.getTotal());

        // Only null shipping address, result amount should be the same as input
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("5"), Long.parseLong("15"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(null, "1", amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(amountData.getTotal(), newAmountData.getTotal());

        // Currency with 0 fraction digits
        amountData = new AmountData(Long.parseLong("1000"), Long.parseLong("500"), Long.parseLong("1500"), Currency.getInstance("JPY"));
        newAmountData = pci.getFullAmountData(null, "1", amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(amountData.getTotal(), newAmountData.getTotal());

        // Uncomment when MCO starts supporting currencies with 3 fraction digits
        /*amountData = new AmountData("1.000","0.500","1.500", Currency.getInstance("JOD"));
        newAmountData = pci.getFullAmountData(null, "1", amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(amountData.getTotal(), newAmountData.getTotal());*/



        // Prepare MasterPassAddress
        new MockMasterPassAddress();
        MasterPassAddress masterPassAddress = newInstance("com.mastercard.masterpass.core.MasterPassAddress", Locale.US);
        masterPassAddress.setPostalCode("2345");
        masterPassAddress.setLocality("City");
        masterPassAddress.setAddressLine(0, "addr");
        masterPassAddress.setAddressLine(1, "addr 2");
        masterPassAddress.setCountryCode("US");
        masterPassAddress.setCountryName("US");

        // Null shipping method, result amount should be the same as input - only tax calculated
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, null, amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(1, newAmountData.getTax());
        assertEquals(11, newAmountData.getTotal());

        // As above, different tax bucket
        masterPassAddress.setAdminArea("DE");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, null, amountData);
        assertEquals(1, newAmountData.getTax());
        assertEquals(11, newAmountData.getTotal());

        // As above, different tax bucket
        masterPassAddress.setAdminArea("SD");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, null, amountData);
        assertEquals(1, newAmountData.getTax());
        assertEquals(11, newAmountData.getTotal());

        // Empty shipping method, result amount should be the same as input
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, "", amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(amountData.getTotal(), newAmountData.getTotal());

        // Unknown shipping method, MasterPassException
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        try {
            newAmountData = pci.getFullAmountData(masterPassAddress, "123", amountData);
            assertTrue(false);
        }catch (MasterPassException e){
            assertTrue(true);
        }

        // 1st shipping method 2.00
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, "1", amountData);// cost 2.00
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(11, newAmountData.getTotal());

        // 2nd shipping method 5.00
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("10"), Long.parseLong("1"), Long.parseLong("11"), Currency.getInstance("USD"));
        newAmountData = pci.getFullAmountData(masterPassAddress, "2", amountData);// cost 5.00
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(11, newAmountData.getTotal());

        // As above + different currency
        masterPassAddress.setAdminArea("NY");
        amountData = new AmountData(Long.parseLong("1000"), Long.parseLong("50"), Long.parseLong("1050"), Currency.getInstance("JPY"));
        newAmountData = pci.getFullAmountData(masterPassAddress, "2", amountData);
        assertEquals(amountData.getCurrency(), newAmountData.getCurrency());
        assertEquals(amountData.getEstimatedTotal(), newAmountData.getEstimatedTotal());
        assertEquals(amountData.getTax(), newAmountData.getTax());
        assertEquals(1050, newAmountData.getTotal());
    }
}