package com.android.shouldiwalk.activities;

import android.util.SparseIntArray;
import android.widget.Button;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddTripDataActivityTest {

    private AddTripDataActivity addTripDataActivity;

    @Before
    public void setUp() throws Exception {
        addTripDataActivity = Robolectric.setupActivity(AddTripDataActivity.class);
    }

    @Test
    public void when_onCreateCalled_then_orderOfPagesInitializedProperly() throws Exception {
        SparseIntArray orderOfPages = addTripDataActivity.getOrderOfPages();

        assertThat(orderOfPages.get(0), is(R.id.gotoStartLocationScreenButton));
        assertThat(orderOfPages.get(1), is(R.id.gotoStartDateTimeScreenButton));
        assertThat(orderOfPages.get(2), is(R.id.gotoEndLocationScreenButton));
        assertThat(orderOfPages.get(3), is(R.id.gotoEndDateTimeScreenButton));
        assertThat(orderOfPages.get(4), is(R.id.gotoMeanOfTransportScreenButton));
        assertThat(orderOfPages.get(5), is(R.id.gotoWeatherScreenButton));
        assertThat(orderOfPages.get(6), is(R.id.gotoTrafficLevelScreenButton));
        assertThat(orderOfPages.get(7), is(R.id.gotoEffortLevelScreenButton));
        assertThat(orderOfPages.get(8), is(R.id.gotoRushLevelScreenButton));
        assertThat(orderOfPages.get(9), is(R.id.gotoSatisfactionLevelScreenButton));
    }
}