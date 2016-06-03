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

package com.mastercard.mymerchant.manager;

import android.util.Log;

import com.activeandroid.query.Select;
import com.mastercard.mymerchant.Config;
import com.mastercard.mymerchant.model.DbAddressModel;
import com.mastercard.masterpass.core.MasterPassAddress;
import com.mastercard.masterpass.core.MasterPassException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Shipping addresses management helper.
 * It performs various db operations
 */
public enum ShippingAddressesManager {
    INSTANCE; // singleton

    /**
     * TAG used for logging.
     */
    private static final String TAG = ShippingAddressesManager.class.getName();

    private MasterPassAddress mTemporaryAddress;

    private ShippingAddressesManager(){

    }

    public MasterPassAddress getDefaultShippingAddress() {

        DbAddressModel dbAddressModel = new Select()
                .from(DbAddressModel.class)
                .where("IsDefault = ?", true)
                .executeSingle();

        return dbModelToMasterPassAddress(dbAddressModel);
    }

    public DbAddressModel getAddressById(long id) {

        return new Select()
                .from(DbAddressModel.class)
                .where("Id = ?", id)
                .executeSingle();
    }

    public DbAddressModel getAddressByMasterPassId(String id) {

        return new Select()
                .from(DbAddressModel.class)
                .where("MasterPassId = ?", id)
                .executeSingle();
    }

    public List<DbAddressModel> getAddresses() {

        return new Select()
                .from(DbAddressModel.class)
                .execute();
    }

    public List<MasterPassAddress> dbAddressListToMasterPassAddressList(List<DbAddressModel> dbAddressModelList) {

        List<MasterPassAddress> addressList = new ArrayList<>();
        for (DbAddressModel dbAddressModel : dbAddressModelList) {
            MasterPassAddress masterPassAddress = dbModelToMasterPassAddress(dbAddressModel);
            if (masterPassAddress != null) {
                addressList.add(masterPassAddress);
            }
        }
        return addressList;
    }


    public DbAddressModel updateAddress(DbAddressModel dbAddressModel) {
        dbAddressModel.save();
        return dbAddressModel;
    }

    public void deleteAddress(long id) {

        DbAddressModel dbAddressModel = DbAddressModel.load(DbAddressModel.class, id);
        dbAddressModel.delete();
    }

    public DbAddressModel insertAddress(DbAddressModel dbAddressModel) {

        dbAddressModel.save();
        return dbAddressModel;
    }

    public MasterPassAddress dbModelToMasterPassAddress(DbAddressModel dbAddressModel) {
        if (dbAddressModel == null) {
            return null;
        }

        try {
            return new MasterPassAddress.Builder(Locale.US)
                    .firstName(dbAddressModel.firstName)
                    .lastName(dbAddressModel.lastName)
                    .addressLine(0, dbAddressModel.addressLine1)
                    .addressLine(1, dbAddressModel.addressLine2)
                    .locality(dbAddressModel.city)
                    .adminArea(dbAddressModel.state)
                    .postalCode(dbAddressModel.postcode)
                    .build();
        } catch (MasterPassException ex) {
            Log.e(TAG, "Unable to build MasterPassAddress object from database entry (DbAddressModel). "
                    + ex.getMessage());
            return null;
        }
    }

    private DbAddressModel masterPassAddressToDbModel(MasterPassAddress masterPassAddress) {
        if (masterPassAddress == null) {
            return null;
        }

        DbAddressModel dbAddressModel = new DbAddressModel();
        dbAddressModel.firstName = masterPassAddress.getFirstName();
        dbAddressModel.lastName = masterPassAddress.getLastName();
        dbAddressModel.addressLine1 = masterPassAddress.getAddressLine(0);
        dbAddressModel.addressLine2 = masterPassAddress.getAddressLine(1);
        dbAddressModel.city = masterPassAddress.getLocality();
        dbAddressModel.state = masterPassAddress.getAdminArea();
        dbAddressModel.postcode = masterPassAddress.getPostalCode();
        dbAddressModel.masterPassId = masterPassAddress.getId();

        return dbAddressModel;

    }

    public DbAddressModel setDefaultShippingAddress(boolean isDefault, DbAddressModel defaultAddress) {

        List<DbAddressModel> dbAddressModelList = getAddresses();
        for (DbAddressModel dbAddressModel : dbAddressModelList) {
            dbAddressModel.isDefault = false;
            dbAddressModel.save();
        }
        defaultAddress.isDefault = isDefault;
        defaultAddress.save();
        return defaultAddress;
    }

    /**
     * Sets a temporary address used for transaction. Final saving address will be done after transaction authorized
     *
     * @param masterPassAddress chosen address
     */
    public void setAddressChosenForTransaction(MasterPassAddress masterPassAddress) {
        mTemporaryAddress = masterPassAddress;
    }

    /**
     * Clears temporary address
     */
    public void clearAddressChosenForTransaction() {
        mTemporaryAddress = null;
    }

    /**
     * Returns address that was used for transaction.
     *
     * @return choosen address
     */
    public MasterPassAddress getAddressChosenForTransaction() {
        return mTemporaryAddress;
    }

    /**
     * Save temporary address used for transaction in our storage.
     * Save address only if it does not exists
     *
     * @return id of the address as kept by MCO (!! not the same as DbAddressModel.getId() )
     */
    public String saveAddressChosenForTransaction() {
        String masterPassAddressId = null;
        if (mTemporaryAddress != null) {
            // Check if address already exists. Try to find it by matching id
            List<DbAddressModel> dbAddressModels = ShippingAddressesManager.INSTANCE.getAddresses();
            List<MasterPassAddress> masterPassAddresses = ShippingAddressesManager.INSTANCE.dbAddressListToMasterPassAddressList(dbAddressModels);
            boolean isFound = false;
            for (MasterPassAddress masterPassAddress : masterPassAddresses) {
                if (masterPassAddress.getId().equalsIgnoreCase(mTemporaryAddress.getId())) {
                    isFound = true;
                    break;
                }
            }

            // Save address to db if not found
            if (!isFound) {
                DbAddressModel newDbAddressModel = masterPassAddressToDbModel(mTemporaryAddress);
                insertAddress(newDbAddressModel);
            }
            masterPassAddressId = mTemporaryAddress.getId();

            // Clear temp only for live. For mock it will be used on Complete page.
            if(!Config.ISMOCK) {
                clearAddressChosenForTransaction();
            }

        }
        return masterPassAddressId;
    }
}
