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

package com.mastercard.mymerchant;

import com.mastercard.mymerchant.model.Basket;
import com.mastercard.mymerchant.network.NetworkManager;

/**
 * Singleton class to handle various data throughout the application.
 */
public class DataManager {
    /**
     * Static reference to itself.
     */
    private static DataManager mInstance;

    /**
     * Current basket.
     */
    public Basket mBasket = new Basket();

    /**
     * Tracks initialization status of the MCO library.
     */
    private boolean mMcoInitialized;

    /**
     * Reference to Network Manager for making API calls
     */
    private NetworkManager mNetworkManager;

    /**
     * Private constructor so only this class can instantiate it.
     */
    private DataManager() {

    }

    /**
     * Create or retrieve the current instance of the DataManager singleton class.
     *
     * @return DataManager
     */
    public static synchronized DataManager getInstance() {
        if (mInstance == null) {
            mInstance = new DataManager();
        }
        return mInstance;
    }

    public void clearBasket() {
        mBasket = new Basket();
    }

    public void setMcoInitialized(boolean isInitialized) {
        mMcoInitialized = isInitialized;
    }

    public boolean isMcoInitialized() {
        return mMcoInitialized;
    }

    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

    public void setNetworkManager(NetworkManager networkManager) {
        this.mNetworkManager = networkManager;
    }
}
