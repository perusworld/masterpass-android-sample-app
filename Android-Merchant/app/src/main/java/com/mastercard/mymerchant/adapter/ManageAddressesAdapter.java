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

package com.mastercard.mymerchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.model.DbAddressModel;

import java.util.List;

/**
 * Adapter for addresses list used on ManageShippingAddressesActivity
 */
public class ManageAddressesAdapter extends BaseAdapter {

    /**
     * List of addresses
     */
    private final List<DbAddressModel> mAddressList;

    /**
     * Current context
     */
    private final Context mContext;

    public ManageAddressesAdapter(
            Context context,
            List<DbAddressModel> addressList) {

        mContext = context;
        mAddressList = addressList;
    }

    @Override
    public int getCount() {
        return mAddressList.size();
    }

    @Override
    public DbAddressModel getItem(int position) {
        return mAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.list_item_manage_addresses, null);

            viewHolder.alias = (TextView)view.findViewById(R.id.txt_title);
            viewHolder.defaultText = (TextView)view.findViewById(R.id.txt_default);
            viewHolder.addressLine = (TextView)view.findViewById(R.id.txt_subtitle);
            viewHolder.btnEdit = (TextView)view.findViewById(R.id.btn_edit);
            viewHolder.btnDelete = (TextView)view.findViewById(R.id.btn_delete);

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder)view.getTag();
        }

        DbAddressModel address = getItem(position);

        viewHolder.alias.setText(address.firstName + " " + address.lastName);
        viewHolder.addressLine.setText(
                    (TextUtils.isEmpty(address.addressLine1) ? "" :
                            address.addressLine1 + ", ") +
                    (TextUtils.isEmpty(address.addressLine2) ? "" :
                            address.addressLine2 + ", ") +
                    (TextUtils.isEmpty(address.city) ? "" :
                            address.city + ", ") +
                    (TextUtils.isEmpty(address.postcode) ? "" :
                            address.postcode)
        );

        viewHolder.btnEdit.setTag(position);
        viewHolder.btnDelete.setTag(position);

        if (address.isDefault) {
            viewHolder.defaultText.setText("(Default)");
        } else {
            viewHolder.defaultText.setText("");
        }

        return view;
    }

    /**
     * View Holder pattern
     */
    private static class ViewHolder {
        TextView alias;
        TextView defaultText;
        TextView addressLine;
        TextView btnEdit;
        TextView btnDelete;
    }
}
