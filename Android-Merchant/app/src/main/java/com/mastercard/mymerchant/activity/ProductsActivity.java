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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastercard.mymerchant.Constants;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.helpers.DialogHelper;
import com.mastercard.mymerchant.model.Product;
import com.mastercard.mymerchant.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity with Products list that can be added to the basket.
 */
public class ProductsActivity extends Activity implements AdapterView.OnItemClickListener {

    /**
     * TAG used for logging.
     */
    private static final String TAG = ProductsActivity.class.getName();

    /**
     * List of available products
     */
    private final ArrayList<Product> mProducts = new ArrayList<>(Arrays.asList(Constants.PRODUCTS));

    /** UI Elements. */
    /**
     * TextView for the number of products currently in the basket.
     */
    private FrameLayout mFrameContainer;
    private TextView mTxtProductBadge;
    private ProductListAdapter mProductListAdapter;

    /**
     * Animation/position helpers.
     */
    private int mListViewOffset = 0;
    private int mCheckoutX = 0;
    private int mCheckoutY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mFrameContainer = (FrameLayout) findViewById(R.id.container);

        // Get the currency code from the basket
        String currencyCode = DataManager.getInstance().mBasket.getCurrencyCode();

        // Setup the list of products
        mProductListAdapter = new ProductListAdapter(this, mProducts, currencyCode);
        ListView listProducts = (ListView) findViewById(android.R.id.list);
        listProducts.setAdapter(mProductListAdapter);
        listProducts.setItemsCanFocus(true);
        listProducts.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // When resuming, update the badge with the current total (can be updated on the checkout page)
        updateBadge(DataManager.getInstance().mBasket.mTotalItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);

        // Grab the checkout menu item
        final MenuItem checkoutMenu = menu.findItem(R.id.action_checkout);

        // Grab the custom action view for this menu item
        RelativeLayout badgeLayout = (RelativeLayout) checkoutMenu.getActionView();

        // Keep hold of a reference to the badge so we can update it
        mTxtProductBadge = (TextView) badgeLayout.findViewById(R.id.txt_product_count);

        // Initialise the badge text
        updateBadge(DataManager.getInstance().mBasket.mTotalItems);

        // Unfortunately, because we are using a custom action view this way, we lose the standard
        // onOptionsItemSelected event, so we need to bind our own event (that just calls the
        // onOptionsItemSelected event)
        ImageView img = (ImageView) badgeLayout.findViewById(R.id.img_checkout);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsActivity.this.onOptionsItemSelected(checkoutMenu);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_checkout:
                // Check the basket actually has some items in it
                if (DataManager.getInstance().mBasket.mTotalItems > 0) {
                    // Go to the checkout activity
                    Intent intent = new Intent(this, CheckoutActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                } else {
                    DialogHelper.emptyBasket(this);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Update the badge for number of products in basket.
     *
     * @param newCount New number to show in the badge, can use 0 to remove the badge.
     */
    private void updateBadge(int newCount) {
        // Make sure we have the reference to the badge
        if (mTxtProductBadge != null) {
            // Check the new count, if it's zero or less then hide the bade, otherwise make sure
            // it's visible
            if (newCount <= 0) {
                mTxtProductBadge.setVisibility(View.GONE);
            } else {
                mTxtProductBadge.setText(String.valueOf(newCount));
                mTxtProductBadge.setVisibility(View.VISIBLE);
            }
        } else {
            Log.w(TAG, "Trying to set badge value without reference to the badge");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = mProductListAdapter.getItem(position);

        int numProductsInBasket = DataManager.getInstance().mBasket.addProduct(product);
        updateBadge(numProductsInBasket);

        // Get the offset position of the List View if we don't have it already
        if (mListViewOffset == 0) {
            mListViewOffset = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        }

        // Get the position of the badge if we don't have it already
        if (mCheckoutX == 0 && mCheckoutY == 0) {
            int[] pos = new int[2];
            mTxtProductBadge.getLocationOnScreen(pos);
            mCheckoutX = pos[0];
            mCheckoutY = pos[1] - mListViewOffset;
        }

        // Find the product image within the view
        ImageView imgView = (ImageView) view.findViewById(R.id.img_image);

        // We need the dimensions and position on the screen of this image
        int width = imgView.getWidth();
        int height = imgView.getHeight();
        int[] pos = new int[2];
        imgView.getLocationInWindow(pos);
        int x = pos[0];
        int y = pos[1] - mListViewOffset;

        // Create a "clone" of the product image the same size and in the same position
        final ImageView clone = new ImageView(this);
        clone.setImageResource(product.mImageResource);
        clone.setAlpha(0.8f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = x;
        params.topMargin = y;
        clone.setLayoutParams(params);

        // Add the image to our container
        mFrameContainer.addView(clone);

        // Build the translate animation
        TranslateAnimation translate = new TranslateAnimation(0, mCheckoutX - x, 0, mCheckoutY - y);
        translate.setDuration(1000);

        // Build the scale animation
        ScaleAnimation scale = new ScaleAnimation(1.0f, 0.33f, 1.0f, 0.33f);
        scale.setDuration(1000);

        // Build the fade animation
        AlphaAnimation alpha = new AlphaAnimation(0.8f, 0.0f);
        alpha.setDuration(800);

        // Combine the animations into a set of animations
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scale);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setFillAfter(true);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Intentional no-op
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Ensure the image view is removed after the animation is complete
                mFrameContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFrameContainer.removeView(clone);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Intentional no-op
            }
        });

        // Run the animation
        clone.startAnimation(set);
    }
}
