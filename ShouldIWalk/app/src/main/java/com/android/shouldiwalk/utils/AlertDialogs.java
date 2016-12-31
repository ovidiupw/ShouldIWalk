package com.android.shouldiwalk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.android.shouldiwalk.R;

public class AlertDialogs {

    public static AlertDialog buildInfoDialog(Context context, InfoDialogData alertInfoDialogData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(alertInfoDialogData.getAlertTitle());
        builder.setMessage(alertInfoDialogData.getAlertMessage());

        builder.setPositiveButton(
                R.string.infoAlertDismissButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }


    public static class InfoDialogData {
        private int alertMessage;
        private int alertTitle;

        public InfoDialogData withAlertMessage(@StringRes int alertMessage) {
            this.alertMessage = alertMessage;
            return this;
        }

        public InfoDialogData withAlertTitle(@StringRes int alertTitle) {
            this.alertTitle = alertTitle;
            return this;
        }

        public int getAlertMessage() {
            return alertMessage;
        }

        public int getAlertTitle() {
            return alertTitle;
        }
    }
}
