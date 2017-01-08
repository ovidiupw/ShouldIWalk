package com.android.shouldiwalk.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.utils.AlertDialogs;

import java.lang.ref.WeakReference;

import static com.android.shouldiwalk.utils.AlertDialogs.buildInfoDialog;

public class ShouldIWalkActivity extends AppCompatActivity {

    private static final String CLASS_TAG = ShouldIWalkActivity.class.getCanonicalName() + "-TAG";
    private static final int ONE_SECOND_IN_MILLIS = 1000;

    private Handler networkConnectivityCheckerHandler;
    private ConnectivityManager connectivityManager;
    private AlertDialog dialog;

    private final WeakReference<ShouldIWalkActivity> activityWeakReference = new WeakReference<>(this);

    private Runnable networkConnectivityChecker = new Runnable() {
        private boolean wasNetworkAvailable = true;
        private boolean isDialogOpen = false;

        @Override
        public void run() {
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                boolean isNetworkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
                activityWeakReference.get().isNetworkConnectionAvailable = isNetworkAvailable;

                if (!isNetworkAvailable && wasNetworkAvailable) {
                    wasNetworkAvailable = false;
                    Log.i(CLASS_TAG, "Network was available but no longer is...");

                    if (activityWeakReference.get() == null
                            || activityWeakReference.get().isFinishing()
                            || isDialogOpen) {
                        return;
                    }

                    dialog = buildInfoDialog(activityWeakReference.get(), new AlertDialogs.InfoDialogData()
                            .withAlertTitle(R.string.noNetworkConnectionDialogTitle)
                            .withAlertMessage(R.string.noNetworkConnectionDialogMessage));

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            isDialogOpen = false;
                            dialog.dismiss();

                        }
                    });

                    //dialog.show();
                    //isDialogOpen = true;

                } else if (isNetworkAvailable && !wasNetworkAvailable) {
                    wasNetworkAvailable = true;
                    Log.i(CLASS_TAG, "Network was not available but has become available!");
                }
            } finally {
                networkConnectivityCheckerHandler.postDelayed(networkConnectivityChecker, 2 * ONE_SECOND_IN_MILLIS);
            }
        }
    };

    protected boolean isNetworkConnectionAvailable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkConnectivityCheckerHandler = new Handler();
        startPeriodicCheckForConnectivity();
        Log.v(CLASS_TAG, "Started periodic check for internet connectivity!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkConnectivityCheckerHandler = new Handler();
        stopPeriodicCheckForConnectivity();
        Log.v(CLASS_TAG, "Stopped periodic check for internet connectivity!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dialog.dismiss();
    }

    private void startPeriodicCheckForConnectivity() {
        networkConnectivityChecker.run();
    }

    private void stopPeriodicCheckForConnectivity() {
        networkConnectivityCheckerHandler.removeCallbacks(networkConnectivityChecker);
    }

    public boolean isNetworkConnectionAvailable() {
        return isNetworkConnectionAvailable;
    }
}
