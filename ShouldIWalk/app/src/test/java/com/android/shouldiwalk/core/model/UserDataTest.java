package com.android.shouldiwalk.core.model;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.activities.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import helpers.EmptyActivity;
import helpers.TestHelper;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserDataTest {

    private EmptyActivity emptyActivity;

    @Before
    public void setUp() throws Exception {
        emptyActivity = Robolectric.setupActivity(EmptyActivity.class);
    }

    @Test
    public void when_noEntryInUserDataTable_then_loadReturnsNull() {
        UserData userData = UserData.loadFromDatabase(emptyActivity, emptyActivity.getDatabase());
        assertThat(userData, nullValue());
    }

    @Test
    public void when_oneEntryInUserDataTable_then_loadReturnsUserDataWithDatabaseData() {
        TestHelper.insertUserDataRecord(emptyActivity.getDatabase());
        UserData userData = UserData.loadFromDatabase(emptyActivity, emptyActivity.getDatabase());

        assertThat(userData, notNullValue());
        assertThat(userData.getId(), is(1));
        assertThat(userData.getName(), is("Dev"));
        assertThat(userData.getLastLoginMillis(), is(1L));
    }
}