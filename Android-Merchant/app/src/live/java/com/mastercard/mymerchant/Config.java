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

import android.app.Application;
import android.content.Context;

import com.mastercard.masterpass.core.EnvironmentType;
import com.mastercard.masterpass.merchant.MasterPassMerchantConfig;
import com.mastercard.mymerchant.network.NetworkManager;
import com.mastercard.mymerchant.util.PreferencesHelper;

import java.util.Locale;

/**
 * Class for various config differences between mock and live version of MCO
 */
public class Config {
    /**
     * Indicated if this compilation is mock
     */
    public static boolean ISMOCK = false;

    /**
     * Returns config for mock MCO
     *
     * @param applicationContext app context
     * @return config for MCO
     */
    public static MasterPassMerchantConfig getMasterPassWalletConfig(Context applicationContext) {
        return new MasterPassMerchantConfig(
                Locale.US,// Only US is supported in this version of MCO
                Constants.SUPPORTED_PAYMENT_NETWORKS,
                PreferencesHelper.INSTANCE.getCryptogramTypeEnum(applicationContext),
                false,
                EnvironmentType.SANDBOX);
    }

    /**
     * Returns mock network manager for the live build.
     *
     * @param application application object
     * @return network manager
     */
    public static NetworkManager getNetworkManager(Application application) {
        return new NetworkManager(application);
    }
}