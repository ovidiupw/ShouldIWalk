package com.android.shouldiwalk.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.AddTripDataInstanceParcelable;
import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.core.database.LocationDBHelper;
import com.android.shouldiwalk.core.database.LocationSqliteHelper;
import com.android.shouldiwalk.core.database.TripDataDBHelper;
import com.android.shouldiwalk.core.database.TripDataSqliteHelper;
import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.Location;
import com.android.shouldiwalk.core.model.TripData;
import com.android.shouldiwalk.core.model.UserData;
import com.android.shouldiwalk.utils.AlertDialogs;
import com.android.shouldiwalk.utils.ErrorUtils;
import com.android.shouldiwalk.utils.ToastUtils;
import com.android.shouldiwalk.utils.Util;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends ShouldIWalkActivity {

    static final int ADD_TRIP_DATA_ACTIVITY_REQUEST_CODE = 1;

    private static final String CLASS_TAG = MainActivity.class.getCanonicalName() + "-TAG";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();
        loadUserDataFromDatabase();
        showFirstTimeInfoCardIfNeverDismissed();
        updateDatabaseUserData();

        initializeCards();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == ADD_TRIP_DATA_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                AddTripDataInstanceParcelable tripData = Util.getAddTripDataInstanceParcelableFromIntent(intent);

                try {
                    addTripDataToDatabase(tripData);
                    updateProgressCardProgress();
                    AlertDialogs.buildInfoDialog(this, new AlertDialogs.InfoDialogData()
                            .withAlertTitle(R.string.tripDataDatabaseAddSuccessTitle)
                            .withAlertMessage(R.string.R_string_tripDataDatabaseAddSuccessSubtitle));
                } catch (DatabaseCommFailure e) {
                    Log.e(CLASS_TAG, e.getMessage());
                    ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
                } catch (InvalidDataException e) {
                    Log.e(CLASS_TAG, e.getMessage());
                    ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
                }
            }
        }
    }

    private void addTripDataToDatabase(AddTripDataInstanceParcelable userData) {
        LocationDBHelper locationDBHelper = new LocationSqliteHelper(this, database);

        Location startLocation
                = new Location(userData.getStartLocation().latitude, userData.getStartLocation().longitude);
        Location endLocation
                = new Location(userData.getEndLocation().latitude, userData.getEndLocation().longitude);

        int startLocationId = locationDBHelper.insert(startLocation);
        int endLocationId = locationDBHelper.insert(endLocation);

        TripData tripData = Util.getTripDataFromUserDataAndLocationIds(userData, startLocationId, endLocationId);
        TripDataDBHelper tripDataDBHelper = new TripDataSqliteHelper(this, database);
        tripDataDBHelper.insert(tripData);

    }

    @VisibleForTesting
    void initializeDatabase() {
        try {
            databaseHelper = new DatabaseHelper(
                    this,
                    DatabaseHelper.getDatabaseIdentifier(),
                    DatabaseHelper.getDatabaseVersion()
            );
            database = databaseHelper.getWritableDatabase();
        } catch (IOException e) {
            ErrorUtils.logException(CLASS_TAG, e);
            ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
        }
    }

    private void loadUserDataFromDatabase() {
        try {
            userData = UserData.loadFromDatabase(this, database);
        } catch (DatabaseCommFailure e) {
            ErrorUtils.logException(CLASS_TAG, e);
            ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
        }
    }

    private void initializeCards() {
        initializeProgressCard(0);

        // TODO initialize other cards also
    }

    private void initializeProgressCard(int topDownPositionInsideView) {
        LinearLayout cardsListView = (LinearLayout) findViewById(R.id.cardsListView);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout progressCard
                = (RelativeLayout) inflater.inflate(R.layout.progress_card, cardsListView, false);

        cardsListView.addView(progressCard, topDownPositionInsideView);

        TextView cardTitle = (TextView) findViewById(R.id.progressToFirstPredictionCardTitle);
        cardTitle.setText(R.string.progressToFirstPredictionCardTitle);

        updateProgressCardProgress();

        final MainActivity that = this;
        Button recordTripDataButton = (Button) findViewById(R.id.firstPredictionProgressBarAddTripDataButton);
        recordTripDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTripDataActivityIntent = new Intent(that, AddTripDataActivity.class);
                startActivityForResult(addTripDataActivityIntent, ADD_TRIP_DATA_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void updateProgressCardProgress() {
        TripDataDBHelper tripDataDBHelper = new TripDataSqliteHelper(this, database);

        int numberOfTripDataRecords = tripDataDBHelper.countRecords();
        int eventsToAdd = 0;

        if (numberOfTripDataRecords < 10) {
            eventsToAdd = 10 - numberOfTripDataRecords;
        }

        TextView cardSubtitle = (TextView) findViewById(R.id.progressToFirstPredictionCardSubtitle);
        if (eventsToAdd > 0) {
            cardSubtitle.setText(getResources().getString(
                    R.string.progressToFirstPredictionCardInProgressSubtitle, eventsToAdd));
        } else {
            cardSubtitle.setText(getResources().getString(
                    R.string.progressToFirstPredictionCardDoneSubtitle));
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.firstPredictionProgressBar);
        progressBar.setMax(10);
        progressBar.setProgress(numberOfTripDataRecords);
        Log.i(CLASS_TAG, "Set the progress for first prediction to " + numberOfTripDataRecords);
    }

    private void showFirstTimeInfoCardIfNeverDismissed() {
        if (userData == null || userData.getFirstTimeInfoDismissed() == 0) {
            try {
                final TableLayout welcomeLayout = (TableLayout) findViewById(R.id.firstTimeWelcomeTableLayout);
                welcomeLayout.setVisibility(View.VISIBLE);

                Button learnMoreButton = (Button) findViewById(R.id.learnMoreFirstTimeWelcomeButton);
                learnMoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(CLASS_TAG, "First time welcome 'learn more' button clicked!");
                        // TODO Start help activity
                    }
                });

                final MainActivity that = this;
                Button dismissButton = (Button) findViewById(R.id.dismissFirstTimeWelcomeButton);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(CLASS_TAG, "First time welcome 'dismiss' button clicked!");
                        welcomeLayout.setVisibility(View.GONE);
                        userData.setFirstTimeInfoDismissed(1);
                        userData.updateDatabaseItem(that, that.getDatabase());
                    }
                });
            } catch (NullPointerException ignored) {
            }
        }
    }

    private void updateDatabaseUserData() {
        long currentTimeInMillis = new Date().getTime();
        long lastLoginMillis = 0;

        try {
            if (userData == null) {
                userData = new UserData();
                userData.setLastLoginMillis(currentTimeInMillis);
                userData.insertInDatabase(this, database);
            } else {
                lastLoginMillis = userData.getLastLoginMillis();
                userData.setLastLoginMillis(currentTimeInMillis);
                userData.updateDatabaseItem(this, database);
            }
        } catch (DatabaseCommFailure e) {
            ErrorUtils.logException(CLASS_TAG, e);
            ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
        }

        userData.setLastLoginMillis(lastLoginMillis);
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
