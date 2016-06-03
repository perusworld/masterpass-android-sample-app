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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.model.Product;
import com.mastercard.mymerchant.util.CurrencyUtils;

import java.util.ArrayList;

/**
 * Adapter for products list used on ProductsActivity
 */
public class ProductListAdapter extends ArrayAdapter<Product> {
    /**
     * TAG used for logging.
     */
    private static final String TAG = ProductListAdapter.class.getName();

    private String mCurrencyCode;

    public ProductListAdapter(Context context, ArrayList<Product> products, String currencyCode) {
        super(context, R.layout.list_item_product, products);
        mCurrencyCode = currencyCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductHolder holder = null;

        // If we've not been passed a view (i.e. not recycling) we need to inflate one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_product, null);

            // Create a new holder and tag it against the view
            holder = new ProductHolder(convertView);
            convertView.setTag(holder);
        } else {
            // Grab the current tag
            holder = (ProductHolder) convertView.getTag();
        }

        // Populate the view with the data from the product in the current position
        holder.populate(getItem(position));

        // Return the populate view
        return convertView;
    }

    /**
     * View Holder pattern
     */
    private class ProductHolder {
        private final ImageView mImage;
        private final TextView mTxtTitle;
        private final TextView mTxtDescription;
        private final RatingBar mRatingBar;
        private final TextView mTxtPrice;
        private final Button mBtnAddToCart;
        private final TextView mTxtNoShipping;

        private ProductHolder(View container) {
            mImage = (ImageView) container.findViewById(R.id.img_image);
            mTxtTitle = (TextView) container.findViewById(R.id.txt_name);
            mTxtDescription = (TextView) container.findViewById(R.id.txt_description);
            mRatingBar = (RatingBar) container.findViewById(R.id.rating_bar);
            mTxtPrice = (TextView) container.findViewById(R.id.txt_price);
            mTxtNoShipping = (TextView) container.findViewById(R.id.txt_shipping);
            mBtnAddToCart = (Button) container.findViewById(R.id.btn_add_to_cart);
            mBtnAddToCart.setClickable(false);
            mBtnAddToCart.setFocusable(false);

            // Change the colour of the rating bar to red
            LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }

        private void populate(final Product product) {
            mImage.setImageResource(product.mImageResource);
            mTxtTitle.setText(product.mName);
            mTxtDescription.setText(product.mDescription);
            mRatingBar.setRating(product.mRating);
            mTxtPrice.setText(CurrencyUtils.convertToText(product.mPrice, mCurrencyCode, true));
            if(product.mIsDigital){
                mTxtNoShipping.setVisibility(View.VISIBLE);
            }else{
                mTxtNoShipping.setVisibility(View.INVISIBLE);
            }
        }
    }
}
