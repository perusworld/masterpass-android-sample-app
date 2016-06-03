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

package com.mastercard.mymerchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.adapter.ManageAddressesAdapter;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.model.DbAddressModel;

import java.util.List;

/**
 * Displays list of shipping addresses.
 * From here user can go to Add Address page, Delete address and go to Edit Address page
 */
public class ManageShippingAddressesActivity extends Activity {

    /**
     * Widget with addresses
     */
    private ListView mListAddresses;

    /**
     * Underlying storage wrapper
     */
    private ShippingAddressesManager mShippingAddressesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_shipping_addresses);

        // Grab widgets
        mShippingAddressesManager = ShippingAddressesManager.INSTANCE;
        mListAddresses = (ListView) findViewById(R.id.manage_address_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    /**
     * Rebinds list view to list of addresses
     */
    private void refreshList() {
        List<DbAddressModel> addressList = mShippingAddressesManager.getAddresses();
        mListAddresses.setAdapter(new ManageAddressesAdapter(this, addressList));
    }

    /**
     * Go to add new address page
     *
     * @param view view that invoked action
     */
    public void addNewAddress(View view) {
        Intent intent = new Intent(this, ModifyShippingAddressActivity.class);
        startActivity(intent);
    }

    /**
     * Go to edit address page
     *
     * @param view view that invoked action
     */
    public void edit(View view) {
        int position = (int) view.getTag();
        DbAddressModel address = (DbAddressModel) mListAddresses.getItemAtPosition(position);

        Intent intent = new Intent(this, ModifyShippingAddressActivity.class);
        intent.putExtra(ModifyShippingAddressActivity.KEY_ADDRESS_ID, address.getId());
        startActivity(intent);
    }

    /**
     * Perform address deletion
     *
     * @param view view that invoked action
     */
    public void delete(View view) {
        int position = (int) view.getTag();
        DbAddressModel address = (DbAddressModel) mListAddresses.getItemAtPosition(position);
        mShippingAddressesManager.deleteAddress(address.getId());
        refreshList();
    }
}
