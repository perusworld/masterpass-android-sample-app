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

package com.mastercard.mymerchant.helpers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.mastercard.mymerchant.R;

/**
 * Helper class for displaying dialogs.
 */
public class DialogHelper {
    private static AlertDialog dialog;
    private static ProgressDialog progressDialog;

    public static void emptyBasket(Context context) {
        emptyBasket(context, null);
    }

    public static void emptyBasket(Context context, DialogInterface.OnClickListener listener) {
        showDialog(context, context.getString(R.string.dialog_title_empty_basket), context.getString(R.string.dialog_message_empty_basket), listener);
    }

    public static void closeDialog(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Create the dialog box
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, listener);

        // Build and show the dialog
        dialog = builder.create();
        dialog.show();
    }

    public static void closeProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static void showProgressDialog(Context context, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
