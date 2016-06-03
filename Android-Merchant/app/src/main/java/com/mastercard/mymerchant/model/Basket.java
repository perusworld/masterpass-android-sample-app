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

package com.mastercard.mymerchant.model;

import com.mastercard.mymerchant.Constants;

import java.util.ArrayList;

/**
 * Class to manage the users current shopping basket.
 */
public class Basket {
    /**
     * The list of items in the basket.
     */
    public final ArrayList<BasketItem> mBasketItems = new ArrayList<>();
    /**
     * The current number of items in the basket, tracked by addProduct() and removeProduct().
     */
    public int mTotalItems = 0;
    /**
     * The current sub total of the basket, tracked by addProduct() and removeProduct(). in cents
     */
    public long mSubTotal = 0;
    /**
     * The shipping cost of the basket. in cents
     */
    public long mShippingPrice = 0;
    /**
     * Basket currency code eg USD
     */
    private String mCurrencyCode;

    /**
     * Constructor
     */
    public Basket() {
        mCurrencyCode = Constants.DEFAULT_CURRENCY_CODE;
    }

    public void setCurrencyCode(String currencyCode) {
        this.mCurrencyCode = currencyCode;
    }

    /**
     * @return Returns the currency code used for this basket.
     */
    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    /**
     * Add a product to the basket.
     *
     * @param product The product to add to the basket.
     * @return The total number of items now in the basket.
     */
    public int addProduct(Product product) {
        // Check if we already have this item, if so increment the quantity
        boolean productAlreadyInBasket = false;
        for (BasketItem basketItem : mBasketItems) {
            if (basketItem.mProduct.mName.equals(product.mName)) {
                productAlreadyInBasket = true;
                basketItem.mQuantity++;

                // Can stop looking now
                break;
            }
        }

        // Product wasn't already in our basket so add it
        if (!productAlreadyInBasket) {
            mBasketItems.add(new BasketItem(product));
        }

        // Increment our sub total (saves calculating it later)
        mSubTotal += product.mPrice;

        // Increment our counter of total items (saves calculating it later)
        return ++mTotalItems;
    }

    /**
     * Remove a product (1 count) from the basket, has no functionality if the product isn't
     * already in the basket.
     *
     * @param product The product to remove (single count only).
     * @return The total number of items now in the basket.
     */
    public int removeProduct(Product product) {
        // Find the product in our basket
        for (BasketItem basketItem : mBasketItems) {
            if (basketItem.mProduct.mName.equals(product.mName)) {
                // Check if we should be reducing the quantity or removing it entirely
                if (basketItem.mQuantity > 1) {
                    basketItem.mQuantity--;
                } else {
                    mBasketItems.remove(basketItem);
                }

                // Decrement our sub total
                mSubTotal -= product.mPrice;

                // Decrement our counter of total items
                mTotalItems--;
                break;
            }
        }

        return mTotalItems;
    }

    public boolean doesBasketRequireShipping() {
        for (BasketItem basketItem : mBasketItems) {
            if (!basketItem.mProduct.mIsDigital) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculate the total of the basket, including shipping cost.
     *
     * @return Total value of the basket.
     */
    public long getSubTotal() {
        return mSubTotal;
    }
    /**
     * Calculate the total of the basket, including shipping cost.
     *
     * @return Total value of the basket. in cents
     */
    public long getTotal() {
        return mSubTotal + mShippingPrice;
    }
}
