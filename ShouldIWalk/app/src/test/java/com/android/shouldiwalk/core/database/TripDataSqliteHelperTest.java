package com.android.shouldiwalk.core.database;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.core.model.Location;
import com.android.shouldiwalk.core.model.TripData;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import helpers.EmptyActivity;
import helpers.TestHelper;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TripDataSqliteHelperTest {

    private TripDataDBHelper tripDataDBHelper;
    private LocationSqliteHelper locationSqliteHelper;

    @Before
    public void setUp() throws Exception {
        EmptyActivity emptyActivity = Robolectric.setupActivity(EmptyActivity.class);
        tripDataDBHelper = new TripDataSqliteHelper(emptyActivity, emptyActivity.getDatabase());
        locationSqliteHelper = new LocationSqliteHelper(emptyActivity, emptyActivity.getDatabase());
    }

    @Test
    public void when_noTripDataRecords_then_getNumberOfRecordsReturns0()  {
        int numberOfRecords = tripDataDBHelper.countRecords();
        assertThat(numberOfRecords, is(0));
    }

    @Test
    public void when_oneTripDataRecord_then_getNumberOfRecordsReturns1() throws Exception {
        Location startLocation = new Location(1, 10, 20);
        Location endLocation = new Location(1, 15, 25);
        locationSqliteHelper.insert(startLocation);
        locationSqliteHelper.insert(endLocation);

        TripData tripData = TestHelper.getSampleTripData(1, startLocation, endLocation);
        tripDataDBHelper.insert(tripData);

        int numberOfRecords = tripDataDBHelper.countRecords();
        assertThat(numberOfRecords, is(1));
    }

    @Test
    public void when_oneTripDataRecord_then_loadReturnsTheRecordInAList() throws Exception {
        Location startLocation = new Location(1, 10, 20);
        Location endLocation = new Location(1, 15, 25);
        locationSqliteHelper.insert(startLocation);
        locationSqliteHelper.insert(endLocation);

        TripData tripData = TestHelper.getSampleTripData(1, startLocation, endLocation);
        tripDataDBHelper.insert(tripData);

        List<TripData> tripDataList = tripDataDBHelper.loadItems(QueryData.ALL_ITEMS());
        assertThat(tripDataList.size(), is(1));
        assertThat(tripDataList, contains(tripData));
    }

    @Test
    public void when_moreTripDataRecords_then_loadReturnsTheRecordsInAList() throws Exception {
        Location startLocation = new Location(1, 10, 20);
        Location endLocation = new Location(1, 15, 25);
        locationSqliteHelper.insert(startLocation);
        locationSqliteHelper.insert(endLocation);

        TripData tripData1 = TestHelper.getSampleTripData(1, startLocation, endLocation);
        TripData tripData2 = TestHelper.getSampleTripData(2, startLocation, endLocation);
        tripDataDBHelper.insert(tripData1);
        tripDataDBHelper.insert(tripData2);

        List<TripData> tripDataList = tripDataDBHelper.loadItems(QueryData.ALL_ITEMS());
        assertThat(tripDataList.size(), is(2));
        assertThat(tripDataList, contains(tripData1, tripData2));
    }
}