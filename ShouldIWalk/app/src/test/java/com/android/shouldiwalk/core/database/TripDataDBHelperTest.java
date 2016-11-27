package com.android.shouldiwalk.core.database;

import com.android.shouldiwalk.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import helpers.EmptyActivity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TripDataDBHelperTest {

    @Test
    public void when_noTripDataRecords_then_getNumberOfRecordsReturns0()  {
        EmptyActivity emptyActivity = Robolectric.setupActivity(EmptyActivity.class);
        int numberOfRecords = TripDataDBHelper.getNumberOfRecords(
                emptyActivity, emptyActivity.getDatabase());
        assertThat(numberOfRecords, is(0));
    }

}