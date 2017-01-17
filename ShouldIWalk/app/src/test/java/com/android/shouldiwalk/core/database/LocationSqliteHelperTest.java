package com.android.shouldiwalk.core.database;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.core.model.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import helpers.EmptyActivity;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocationSqliteHelperTest {

    private LocationSqliteHelper locationSqliteHelper;

    @Before
    public void setUp() throws Exception {
        EmptyActivity emptyActivity = Robolectric.setupActivity(EmptyActivity.class);
        locationSqliteHelper = new LocationSqliteHelper(emptyActivity, emptyActivity.getDatabase());
    }

    @Test
    public void when_noLocationInDatabase_then_loadingItemsYieldsEmptyList() {
        List<Location> locations = locationSqliteHelper.loadItems(new QueryData());
        assertThat(locations.size(), is(0));
    }

    @Test
    public void when_insertingOneLocation_then_loadingItemsProvidesTheInsertedData() {
        Location location = new Location(1, 12, 10);
        locationSqliteHelper.insert(location);
        List<Location> locations = locationSqliteHelper.loadItems(new QueryData()
                .addBinaryCondition(
                        Location.getLatitudeDBIdentifier(),
                        QueryArithmeticOperator.Equals,
                        String.valueOf(location.getLatitude())
                )
                .addBinaryCondition(
                        Location.getLongitudeDBIdentifier(),
                        QueryArithmeticOperator.Equals,
                        String.valueOf(location.getLongitude())
                )
                .withLogicalOperationBetweenConditions(QueryLogicOperator.AND));

        assertThat(locations, contains(location));
    }

    @Test
    public void when_insertingMoreLocations_then_loadingItemsProvidesTheInsertedData() {
        Location location1 = new Location(1, 12, 10.4);
        Location location2 = new Location(2, 8.3, 2);
        Location location3 = new Location(3, 5, 7.23337);
        Location location4 = new Location(4, 5, -7.23337);

        int location1ID = locationSqliteHelper.insert(location1);
        int location2ID = locationSqliteHelper.insert(location2);
        int location3ID = locationSqliteHelper.insert(location3);
        int location4ID = locationSqliteHelper.insert(location4);

        assertThat(location1ID, is(1));
        assertThat(location2ID, is(2));
        assertThat(location3ID, is(3));
        assertThat(location4ID, is(4));

        List<Location> locations = locationSqliteHelper.loadItems(new QueryData()
                .addBinaryCondition(
                        Location.getLatitudeDBIdentifier(),
                        QueryArithmeticOperator.Greater,
                        String.valueOf(0)
                )
                .addBinaryCondition(
                        Location.getLongitudeDBIdentifier(),
                        QueryArithmeticOperator.Greater,
                        String.valueOf(0)
                )
                .withLogicalOperationBetweenConditions(QueryLogicOperator.AND));

        assertThat(locations, hasItems(location1, location2, location3));
    }

    @Test
    public void when_updatingLocation_then_gettingUpdatedFieldsWhenLoading() {
        Location location = new Location(1, 12, 10.4);
        locationSqliteHelper.insert(location);

        location.setLatitude(14);
        location.setLongitude(12);
        locationSqliteHelper.update(location);

        List<Location> locations = locationSqliteHelper.loadItems(new QueryData()
                .addBinaryCondition(
                        Location.getLatitudeDBIdentifier(),
                        QueryArithmeticOperator.Equals,
                        String.valueOf(location.getLatitude())
                )
                .addBinaryCondition(
                        Location.getLongitudeDBIdentifier(),
                        QueryArithmeticOperator.Equals,
                        String.valueOf(location.getLongitude())
                )
                .withLogicalOperationBetweenConditions(QueryLogicOperator.AND));

        assertThat(locations, contains(location));

    }
}