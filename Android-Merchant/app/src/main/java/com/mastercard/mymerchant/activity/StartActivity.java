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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mastercard.mymerchant.BuildConfig;
import com.mastercard.mymerchant.Config;
import com.mastercard.mymerchant.DataManager;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.mco.McoInitializer;
import com.mastercard.mymerchant.util.PreferencesHelper;
import com.mastercard.masterpass.core.MasterPassException;

/**
 * Main entry point for the app. Launcher activity
 */
public class StartActivity extends Activity {

    /**
     * TAG used for logging.
     */
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        // Pull required settings from preferences
        DataManager.getInstance().mBasket.setCurrencyCode(PreferencesHelper.INSTANCE.getCurrencyString(this));

        // Initialize [MCO-SDK]
        initMCO();

        TextView txtVersion = (TextView) findViewById(R.id.txt_version);
        txtVersion.setText(BuildConfig.VERSION_NAME);

        // Start Products activity with 1 sec delay
        txtVersion.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, ProductsActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);//1s
    }

    /**
     * Initialize [MCO-SDK] in the background thread
     */
    private void initMCO() {
        // Create AsyncTask
        new AsyncTask<Void, Void, MasterPassException>() {

            @Override
            protected MasterPassException doInBackground(Void... params) {
                try {
                    McoInitializer mcoInitializer = new McoInitializer();
                    // Initialize with app context so it is consistent throughout the app
                    mcoInitializer.initLiveMCO(getApplicationContext());

                    Log.d(TAG, "MCO SDK successfully initialized in background thread.");
                } catch (MasterPassException masterPassException) {
                    return masterPassException;
                }
                return null;
            }

            @Override
            protected void onPostExecute(MasterPassException masterPassException) {
                super.onPostExecute(masterPassException);
                // Check if we finished with error
                if (masterPassException != null) {
                    DataManager.getInstance().setMcoInitialized(false);
                    Toast.makeText(StartActivity.this, "Unable to initialize MCO SDK. " + masterPassException.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Unable to initialize MCO - " + masterPassException.getMessage());
                    masterPassException.printStackTrace();
                }
                DataManager.getInstance().setMcoInitialized(true);
            }
        }.execute();
    }
}
