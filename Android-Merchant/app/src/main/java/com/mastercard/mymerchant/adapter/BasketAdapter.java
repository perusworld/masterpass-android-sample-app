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
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.model.Basket;
import com.mastercard.mymerchant.model.BasketItem;
import com.mastercard.mymerchant.util.CurrencyUtils;

/**
 * Adapter for list of products in current basket.
 * Used on CheckoutActivity and on CompleteActivity
 */
public class BasketAdapter  extends ArrayAdapter<BasketItem> {
    /**
     * TAG used for logging.
     */
    private static final String TAG = BasketAdapter.class.getName();

    /**
     * Flag indicating if basket contents can be removed.
     * Adapter is re-used on Complete screen which does not have option to remove
     */
    private boolean bCanRemove = true;

    private String mCurrencyCode;

    /**
     * Ctor
     * @param context current context
     * @param basket current basket
     */
    public BasketAdapter(Context context, Basket basket, String currency) {
        super(context, R.layout.list_item_basket_item, basket.mBasketItems);
        mCurrencyCode = currency;
    }

    /**
     * Ctor
     * @param context current context
     * @param basket current basket
     * @param canRemove Flag indicating if basket contents can be removed.
     */
    public BasketAdapter(Context context, Basket basket, boolean canRemove) {
        super(context, R.layout.list_item_basket_item, basket.mBasketItems);
        bCanRemove = canRemove;
        mCurrencyCode = basket.getCurrencyCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BasketItemHolder holder;

        // If we've not been passed a view (i.e. not recycling) we need to inflate one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_basket_item, null);

            // Create a new holder and tag it against the view
            holder = new BasketItemHolder(convertView, bCanRemove);
            convertView.setTag(holder);

        } else {

            // Grab the current tag
            holder = (BasketItemHolder) convertView.getTag();
        }

        // Populate the view with the data from the product in the current position
        holder.populate(getItem(position));

        // Return the populate view
        return convertView;
    }

    /**
     * Holder class to the re-use of views.
     */
    private class BasketItemHolder {
        // Widgets
        private final ImageView mImage;
        private final TextView mTxtTitle;
        private final TextView mTxtPrice;
        private final TextView mTxtQuantity;

        private BasketItemHolder(View container, boolean canRemove) {
            mImage = (ImageView) container.findViewById(R.id.img_image);
            mTxtTitle = (TextView) container.findViewById(R.id.txt_name);
            mTxtPrice = (TextView) container.findViewById(R.id.txt_price);
            mTxtQuantity = (TextView) container.findViewById(R.id.txt_quantity);
            TextView txtRemove = (TextView) container.findViewById(R.id.txt_remove);
            if (canRemove) {
                txtRemove.setPaintFlags(txtRemove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                txtRemove.setVisibility(View.GONE);
            }
        }

        private void populate(final BasketItem item) {
            mImage.setImageResource(item.mProduct.mImageResource);
            mTxtTitle.setText(item.mProduct.mName);
            mTxtPrice.setText(CurrencyUtils.convertToText(item.mProduct.mPrice, mCurrencyCode, true));
            mTxtQuantity.setText(String.valueOf(item.mQuantity));
        }
    }
}
