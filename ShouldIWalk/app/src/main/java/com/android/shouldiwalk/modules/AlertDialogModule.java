package com.android.shouldiwalk.modules;

import android.app.Activity;
import android.app.AlertDialog;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.utils.AlertDialogs;

import dagger.Module;
import dagger.Provides;

import static com.android.shouldiwalk.utils.AlertDialogs.buildInfoDialog;

@Module
public class AlertDialogModule {
    private final Activity activity;
    private final int titleResId;
    private final int messageResId;

    public AlertDialogModule(Activity activity, int titleResId, int messageResId) {
        this.activity = activity;
        this.titleResId = titleResId;
        this.messageResId = messageResId;
    }

    @Provides
    AlertDialog provideAlertDialog() {
        return buildInfoDialog(
                activity,
                new AlertDialogs.InfoDialogData()
                        .withAlertTitle(titleResId)
                        .withAlertMessage(messageResId));
    }
}
