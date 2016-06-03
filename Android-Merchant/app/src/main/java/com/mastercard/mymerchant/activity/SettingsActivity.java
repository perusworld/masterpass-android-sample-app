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
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mastercard.mymerchant.Constants;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.util.PreferencesHelper;
import com.mastercard.masterpass.core.CryptogramType;
import com.mastercard.masterpass.core.MasterPassAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents settings screen. Option to tweak:
 * - shipping addresses
 * - currency
 * - locale
 * - cryptogram type
 */
public class SettingsActivity extends Activity {

    private TextView mTxtAddressLine1;
    private TextView mTxtAddressLine2;
    private TextView mTxtCity;
    private TextView mTxtState;
    private TextView mTxtPostcode;

    private Button mBtnManageAddresses;
    private Button mBtnSave;

    private Spinner mSpnCryptogramTypes;
    private Spinner mSpnCurrencies;
    private Spinner mSpnLocales;

    private boolean mIsPopulatingSpinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mTxtAddressLine1 = (TextView) findViewById(R.id.default_address_line1);
        mTxtAddressLine2 = (TextView) findViewById(R.id.default_address_line2);
        mTxtCity = (TextView) findViewById(R.id.default_address_city);
        mTxtState = (TextView) findViewById(R.id.default_address_state);
        mTxtPostcode = (TextView) findViewById(R.id.default_address_postcode);
        mBtnManageAddresses = (Button) findViewById(R.id.btn_manage_addresses);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mSpnCryptogramTypes = (Spinner) findViewById(R.id.cryptogram_spinner);
        mSpnCurrencies = (Spinner) findViewById(R.id.currency_spinner);
        mSpnLocales = (Spinner) findViewById(R.id.locale_spinner);

        mBtnManageAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ManageShippingAddressesActivity.class));
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsPopulatingSpinner = true;

        populateDefaultAddress(ShippingAddressesManager.INSTANCE.getDefaultShippingAddress());
        populateCryptogramTypes();
        populateLocales();
        populateCurrencies();

        mIsPopulatingSpinner = false;
    }

    private void populateCryptogramTypes() {
        final Context context = this;

        // Get the existing cryptogramType from shared preferences
        String savedCryptogramType = PreferencesHelper.INSTANCE.getCryptogramTypeString(this);

        // Get values from enumerator into string array
        final List<String> enumValues = new ArrayList<>();
        for (CryptogramType cryptogramType : CryptogramType.values()) {
            enumValues.add(cryptogramType.name());
        }

        populateSpinner(mSpnCryptogramTypes,
                enumValues.toArray(new String[enumValues.size()]),
                savedCryptogramType,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!mIsPopulatingSpinner) {
                            PreferencesHelper.INSTANCE.setCryptogramType(context, enumValues.get(i));
                            warnAboutRestart(context);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
    }


    private void populateCurrencies() {
        final Context context = this;

        // Get the existing currency from shared preferences
        String currency = PreferencesHelper.INSTANCE.getCurrencyString(this, Constants.DEFAULT_CURRENCY_CODE);

        populateSpinner(mSpnCurrencies,
                Constants.SUPPORTED_CURRENCIES,
                currency,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!mIsPopulatingSpinner) {
                            PreferencesHelper.INSTANCE.setCurrency(context, Constants.SUPPORTED_CURRENCIES[i]);
                            promptForRestart();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
    }

    private void populateLocales() {
        final Context context = this;

        // Get the existing locale from shared preferences
        String savedLocale = PreferencesHelper.INSTANCE.getLocaleString(context);

        populateSpinner(mSpnLocales, Constants.SUPPORTED_LOCALE, savedLocale,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!mIsPopulatingSpinner) {
                            PreferencesHelper.INSTANCE.setLocale(context, Constants.SUPPORTED_LOCALE[i]);
                            promptForRestart();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
    }

    private void populateSpinner(
            Spinner spinner,
            String[] values,
            String selectedValue,
            AdapterView.OnItemSelectedListener listener) {

        spinner.setOnItemSelectedListener(null);

        // Populate spinner with string array
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        // Set the selected item in the spinner
        if (selectedValue != null) {
            spinner.setSelection(spinnerArrayAdapter.getPosition(selectedValue), false);
        } else {
            spinner.setSelection(0, false);
        }

        // Set the OnItemSelected listener after an item has been selected
        spinner.setOnItemSelectedListener(listener);
    }

    private void promptForRestart() {
        final Context context = this;
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("This setting requires the app to restart. Would you like to do this now?")
                .create();

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent startIntent = new Intent(context, StartActivity.class);
                startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                int pendingIntentId = new Random(System.currentTimeMillis()).nextInt();
                PendingIntent pendingIntent = PendingIntent.getActivity(context, pendingIntentId,
                        startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 300, pendingIntent);
                ExitActivity.exitApplication(context);
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void warnAboutRestart(Context context) {
        new AlertDialog.Builder(context)
                .setMessage("This setting requires the app to restart. Please kill the app and run it again.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void populateDefaultAddress(MasterPassAddress address) {
        if (address == null) {
            mTxtAddressLine1.setText("No default address");
            mTxtAddressLine2.setText(null);
            mTxtAddressLine2.setVisibility(View.GONE);
            mTxtCity.setText(null);
            mTxtCity.setVisibility(View.GONE);
            mTxtState.setText(null);
            mTxtState.setVisibility(View.GONE);
            mTxtPostcode.setText(null);
            mTxtPostcode.setVisibility(View.GONE);
        } else {
            mTxtAddressLine1.setText(address.getAddressLine(0));
            mTxtAddressLine2.setText(address.getAddressLine(1));
            mTxtAddressLine2.setVisibility(View.VISIBLE);
            mTxtCity.setText(address.getLocality());
            mTxtCity.setVisibility(View.VISIBLE);
            mTxtState.setText(address.getAdminArea());
            mTxtState.setVisibility(View.VISIBLE);
            mTxtPostcode.setText(address.getPostalCode());
            mTxtPostcode.setVisibility(View.VISIBLE);
        }
    }
}
