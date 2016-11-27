package com.android.shouldiwalk.activities;

import android.view.View;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.core.model.UserData;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import helpers.EmptyActivity;
import helpers.TestHelper;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    public static final int ONE_SECOND_IN_MILLIS = 1000;

    @Test
    public void when_onCreateCalled_then_databaseInitialized() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        DatabaseHelper dbHelper = mainActivity.getDatabaseHelper();
        assertThat(
                dbHelper.getDatabaseName(),
                is(DatabaseHelper.getDatabaseIdentifier()));
        assertThat(
                dbHelper.getReadableDatabase().getVersion(),
                is(DatabaseHelper.getDatabaseVersion()));
    }

    @Test
    public void when_userDataMissing_then_firstTimeHelpShown() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        assertThat(
                mainActivity.findViewById(R.id.firstTimeWelcomeTableLayout).getVisibility(),
                is(View.VISIBLE));
        assertThat(
                mainActivity.findViewById(R.id.dismissFirstTimeWelcomeButton).getVisibility(),
                is(View.VISIBLE));
        assertThat(
                mainActivity.findViewById(R.id.learnMoreFirstTimeWelcomeButton).getVisibility(),
                is(View.VISIBLE));
        assertThat(
                mainActivity.findViewById(R.id.firstTimeWelcomeTextView).getVisibility(),
                is(View.VISIBLE));
    }

    @Test
    public void when_userDataInDatabase_andHelpDismissed_then_firstTimeHelpNOTShown() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).get();
        mainActivity.initializeDatabase();
        TestHelper.insertUserDataRecord(mainActivity.getDatabase());
        UserData userData = UserData.loadFromDatabase(mainActivity, mainActivity.getDatabase());
        userData.setFirstTimeInfoDismissed(1);
        userData.updateDatabaseItem(mainActivity, mainActivity.getDatabase());
        mainActivity.onCreate(null);

        assertThat(
                mainActivity.findViewById(R.id.firstTimeWelcomeTableLayout).getVisibility(),
                is(View.GONE));
    }

    @Test
    public void when_userDataMissing_then_creatingDatabaseRecordForCurrentUser() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        UserData userData = UserData.loadFromDatabase(mainActivity, mainActivity.getDatabase());

        assertThat(userData.getName(), nullValue());
        assertThat(userData.getId(), is(1));
        assertThat(
                userData.getLastLoginMillis(),
                greaterThan(new Date().getTime() - ONE_SECOND_IN_MILLIS));
    }

    @Test
    public void when_userDataNotMissing_then_updatingDatabaseUserDataRecordOnCreate() throws Exception {
        EmptyActivity emptyActivity = Robolectric.setupActivity(EmptyActivity.class);

        UserData userData = new UserData();
        userData.setLastLoginMillis(150);
        userData.setName("Dev");
        userData.insertInDatabase(emptyActivity, emptyActivity.getDatabase());

        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        userData = UserData.loadFromDatabase(mainActivity, mainActivity.getDatabase());

        assertThat(userData.getName(), is("Dev"));
        assertThat(userData.getId(), is(1));
        assertThat(
                userData.getLastLoginMillis(),
                greaterThan(new Date().getTime() - ONE_SECOND_IN_MILLIS));
    }
}