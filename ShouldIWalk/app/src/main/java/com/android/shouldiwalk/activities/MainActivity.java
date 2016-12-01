package com.android.shouldiwalk.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.core.database.TripDataDBHelper;
import com.android.shouldiwalk.core.database.TripDataSqliteHelper;
import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.model.UserData;
import com.android.shouldiwalk.utils.ErrorUtils;
import com.android.shouldiwalk.utils.ToastUtils;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String CLASS_TAG = MainActivity.class.getCanonicalName() + "-TAG";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDatabase();
        initializeDatabaseUserData();
        showFirstTimeInfoCardIfNeverDismissed();
        updateDatabaseUserData();

        initializeCards();

    }

    private void initializeCards() {
        TripDataDBHelper tripDataDBHelper = new TripDataSqliteHelper(this, database);
        int numberOfTripDataRecords = tripDataDBHelper.countRecords();
        if (numberOfTripDataRecords <= 10) {
            int eventsStillNeededForPrediction = 10 - numberOfTripDataRecords;
            initializeProgressCard(0, eventsStillNeededForPrediction);
        }

        // TODO initialize other cards also
    }

    private void initializeProgressCard(int topDownPositionInsideView, int eventsToAdd) {
        LinearLayout cardsListView = (LinearLayout) findViewById(R.id.cardsListView);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        RelativeLayout progressCard
                = (RelativeLayout) inflater.inflate(R.layout.progress_card, cardsListView, false);

        cardsListView.addView(progressCard, topDownPositionInsideView);

        TextView cardTitle = (TextView) findViewById(R.id.firstPredictionCardTitle);
        cardTitle.setText(R.string.firstPredictionCardTitle);

        TextView cardSubtitle = (TextView) findViewById(R.id.firstPredictionCardSubtitle);
        cardSubtitle.setText(getResources().getString(R.string.firstPredictionCardSubtitle, eventsToAdd));
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

    private void initializeDatabaseUserData() {
        try {
            userData = UserData.loadFromDatabase(this, database);
        } catch (DatabaseCommFailure e) {
            ErrorUtils.logException(CLASS_TAG, e);
            ToastUtils.showToast(this, Toast.LENGTH_LONG, e.getMessage());
        }
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

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
