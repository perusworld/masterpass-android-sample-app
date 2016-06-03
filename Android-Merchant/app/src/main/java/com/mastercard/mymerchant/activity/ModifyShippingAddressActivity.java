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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.mastercard.mymerchant.Constants;
import com.mastercard.mymerchant.R;
import com.mastercard.mymerchant.manager.ShippingAddressesManager;
import com.mastercard.mymerchant.model.DbAddressModel;
import com.mastercard.mymerchant.model.TextValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for adding/editing shipping address
 */
public class ModifyShippingAddressActivity extends Activity {

    /**
     * Key with address id, in case of edit version of the page
     */
    public final static String KEY_ADDRESS_ID = "KEY_ADDRESS_ID";

    /**
     * Underlying storage wrapper
     */
    private ShippingAddressesManager mShippingAddressesManager;

    /**
     * Flag indicating if we are in edit mode
     */
    private boolean mEditMode;

    //Widgets
    private EditText mName;
    private EditText mLastname;
    private EditText mAddressLine1;
    private EditText mAddressLine2;
    private EditText mCity;
    private EditText mPostcode;
    private EditText mState;
    private Switch mDefaultSwitch;

    /**
     * List of fields to validate
     */
    private List<TextValidation> mValidatedText;

    /**
     * Save reference to db address model in case of edit.
     * This is required to modify the same object so it is updated in DB
     */
    private DbAddressModel mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_shipping_address);
        mShippingAddressesManager = ShippingAddressesManager.INSTANCE;
        mValidatedText = new ArrayList<>();

        // Get references to views
        TextView mTextTitle = (TextView) findViewById(R.id.add_addresses_title);
        mName = (EditText) findViewById(R.id.address_name_value);
        mLastname = (EditText) findViewById(R.id.address_lastname_value);
        mAddressLine1 = (EditText) findViewById(R.id.address_line1_value);
        mAddressLine2 = (EditText) findViewById(R.id.address_line2_value);
        mCity = (EditText) findViewById(R.id.address_city_value);
        mPostcode = (EditText) findViewById(R.id.address_postcode_value);
        mState = (EditText) findViewById(R.id.address_state_value);
        mDefaultSwitch = (Switch) findViewById(R.id.add_address_default_switch);

        // Add TextViews to validation set
        mValidatedText.add(new TextValidation(mName, "Name required"));
        mValidatedText.add(new TextValidation(mLastname, "Lastname required"));
        mValidatedText.add(new TextValidation(mAddressLine1, "Address required"));
        mValidatedText.add(new TextValidation(mCity, "City required"));
        mValidatedText.add(new TextValidation(mState, "State required"));
        mValidatedText.add(new TextValidation(mPostcode, "Postcode required"));

        // Read intent for address ID
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_ADDRESS_ID)) {
            long id = intent.getLongExtra(KEY_ADDRESS_ID, -1);
            mAddress = mShippingAddressesManager.getAddressById(id);
        }

        if (mAddress != null) {
            // We have current address added so this is edit mode
            mEditMode = true;
            mTextTitle.setText(this.getString(R.string.lbl_address_edit));
            populateViews(mAddress);

        } else {
            // We are adding new address so create db model
            mEditMode = false;
            mAddress = new DbAddressModel();
            mTextTitle.setText(this.getString(R.string.lbl_address_add));
        }
    }

    /**
     * Fill form fields based on currently editing address
     *
     * @param address current address
     */
    private void populateViews(DbAddressModel address) {
        mName.setText(address.firstName);
        mLastname.setText(address.lastName);
        mAddressLine1.setText(address.addressLine1);
        mAddressLine2.setText(address.addressLine2);
        mCity.setText(address.city);
        mPostcode.setText(address.postcode);
        mState.setText(address.state);
        mDefaultSwitch.setChecked(address.isDefault);
    }

    /**
     * Pull out the values from the UI and store them in a model for use elsewhere.
     */
    private DbAddressModel getAddressFromViews() {
        // Update existing object (works for both add and edit)
        mAddress.firstName = mName.getText().toString();
        mAddress.lastName = mLastname.getText().toString();
        mAddress.addressLine1 = mAddressLine1.getText().toString();
        mAddress.addressLine2 = mAddressLine2.getText().toString();
        mAddress.city = mCity.getText().toString();
        mAddress.state = mState.getText().toString().toUpperCase();
        mAddress.postcode = mPostcode.getText().toString();
        mAddress.isDefault = mDefaultSwitch.isChecked();
        mAddress.masterPassId = ShippingAddressesManager.INSTANCE.dbModelToMasterPassAddress(mAddress).getId();

        return mAddress;
    }

    /**
     * If all views pass validation, save to the database.
     */
    public void save(View view) {

        if (validate() && validateState()) {

            DbAddressModel address = getAddressFromViews();

            if (mEditMode) {
                // Update address in db
                DbAddressModel updatedAddress = ShippingAddressesManager.INSTANCE.updateAddress(address);
                if (updatedAddress.isDefault) {
                    ShippingAddressesManager.INSTANCE.setDefaultShippingAddress(true, updatedAddress);
                }
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

            } else {
                // Insert new address to db
                DbAddressModel insertedAddress = ShippingAddressesManager.INSTANCE.insertAddress(address);
                if (insertedAddress.isDefault) {
                    ShippingAddressesManager.INSTANCE.setDefaultShippingAddress(true, insertedAddress);
                }
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        }
    }

    /**
     * Cancel button was pressed - finish activity.
     */
    public void cancel(View view) {

        finish();
    }

    /**
     * Loops through all TextValidation objects added to the mValidatedText collection.
     *
     * @return Returns true if all text fields are populated, otherwise returns false.
     */
    private boolean validate() {

        boolean isValid = true;

        for (TextValidation textValidation : mValidatedText) {
            if (TextUtils.isEmpty(textValidation.getEditText().getText())) {
                isValid = false;
                textValidation.getEditText().setError(textValidation.getErrorMessage());
            }
        }

        return isValid;
    }

    /**
     * Checks if provided State is valid US State code (2 letter code, uppercased)
     *
     * @return true if State is valid Us State code
     */
    private boolean validateState() {
        String state = mState.getText().toString().toUpperCase();
        if (Constants.STATE_MAP.containsKey(state)) {
            return true;
        } else {
            mState.setError("Invalid US State code \n(2 letters, uppercase)");
            return false;
        }
    }
}
