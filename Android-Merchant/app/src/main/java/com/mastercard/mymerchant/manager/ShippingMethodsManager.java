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

package com.mastercard.mymerchant.manager;

import com.mastercard.masterpass.core.MasterPassException;
import com.mastercard.masterpass.merchant.ShippingMethod;

import java.util.ArrayList;
import java.util.List;


/**
 * Shipping addresses management helper.
 * In current version it uses hardcoded list of shipping methods
 */
public enum ShippingMethodsManager {
    INSTANCE; // singleton

    /**
     * TAG used for logging.
     */
    private static final String TAG = ShippingMethodsManager.class.getName();

    public ShippingMethod getShippingMethodById(String id) {

        List<ShippingMethod> shippingMethodList = getSupportedShippingMethods();
        if (shippingMethodList != null) {
            for (ShippingMethod shippingMethod : shippingMethodList) {
                if (id.equals(shippingMethod.getId())) {
                    return shippingMethod;
                }
            }
        }
        return null;
    }

    /**
     * Used to retrieve all shipping methods.
     *
     * @return Currently returns hard-coded arrayList of shipping methods.
     */
    public ArrayList<ShippingMethod> getSupportedShippingMethods() {

        ArrayList<ShippingMethod> shippingMethodList = new ArrayList<>();
        try {
            shippingMethodList.add(new ShippingMethod("1", "2-day Shipping", "2-day Shipping", "200"));
            shippingMethodList.add(new ShippingMethod("2", "Overnight Shipping", "Overnight Shipping", "500"));
        } catch (MasterPassException ex) {
            ex.printStackTrace();
            return shippingMethodList;
        }

        return shippingMethodList;
    }

    /**
     * Used by the MCO SDK to retrieve the current default shipping method ID.
     */
    public long getDefaultShippingMethodId() {
        // Make 2-day Shipping default one
        return 1;
    }

}
