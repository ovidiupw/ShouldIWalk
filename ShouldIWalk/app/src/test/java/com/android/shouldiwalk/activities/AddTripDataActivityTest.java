package com.android.shouldiwalk.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.R;
import com.android.shouldiwalk.activities.fragments.ShouldIWalkFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddTripDataActivityTest {

    private Logger LOG = Logger.getLogger(AddTripDataActivityTest.class.getCanonicalName());

    @Test
    public void when_onCreateCalled_then_orderOfPagesInitializedProperly() {
        AddTripDataActivity addTripDataActivity = Robolectric.setupActivity(AddTripDataActivity.class);
        SparseIntArray orderOfPages = addTripDataActivity.getOrderOfPages();

        assertThat(orderOfPages.get(0), is(R.id.gotoStartLocationScreenButton));
        assertThat(orderOfPages.get(1), is(R.id.gotoStartDateTimeScreenButton));
        assertThat(orderOfPages.get(2), is(R.id.gotoEndLocationScreenButton));
        assertThat(orderOfPages.get(3), is(R.id.gotoEndDateTimeScreenButton));
        assertThat(orderOfPages.get(4), is(R.id.gotoWeatherScreenButton));
        assertThat(orderOfPages.get(5), is(R.id.gotoMeanOfTransportScreenButton));
        assertThat(orderOfPages.get(6), is(R.id.gotoTrafficLevelScreenButton));
        assertThat(orderOfPages.get(7), is(R.id.gotoEffortLevelScreenButton));
        assertThat(orderOfPages.get(8), is(R.id.gotoRushLevelScreenButton));
        assertThat(orderOfPages.get(9), is(R.id.gotoSatisfactionLevelScreenButton));
    }

    @Test
    public void when_activeFragmentChanged_then_fragmentsAddedToFragmentPageAdapter() throws InterruptedException {
        AddTripDataActivity addTripDataActivity = Robolectric.setupActivity(AddTripDataActivity.class);
        List<Fragment> fragments = addTripDataActivity.getSupportFragmentManager().getFragments();
        assertThat(fragments.size(), is(10));
        assertThat(addTripDataActivity.getInstanceData().getActiveScreenIndex(), is(0));

        List<Integer> fragmentIdsCycle = Lists.newArrayList(
                R.id.gotoStartLocationScreenButton,
                R.id.gotoStartDateTimeScreenButton,
                R.id.gotoEndLocationScreenButton,
                R.id.gotoEndDateTimeScreenButton
        );

        for (Integer fragmentId : fragmentIdsCycle) {
            ShadowLooper.pauseMainLooper();
            int fragmentIndex = addTripDataActivity.getOrderOfPages().indexOfValue(fragmentId);
            addTripDataActivity.setActiveScreenAndTab(fragmentIndex);
            addTripDataActivity.getFragmentManager().executePendingTransactions();
            ShadowApplication.getInstance().getForegroundThreadScheduler().advanceToLastPostedRunnable();

            assertThat(addTripDataActivity.getInstanceData().getActiveScreenIndex(), is(fragmentIndex));
        }
    }

    @Test
    public void when_activityLifecycleRestored_then_stateRestoredOnResume() {
        ActivityController<AddTripDataActivity> activityController = Robolectric.buildActivity(AddTripDataActivity.class);
        activityController.create();

        AddTripDataActivity addTripDataActivity = activityController.get();

        addTripDataActivity.getInstanceData().setActiveScreenIndex(100);
        addTripDataActivity.getInstanceData().setStartDate(new Date(0));
        addTripDataActivity.getInstanceData().setEndDate(new Date(1));
        addTripDataActivity.getInstanceData().setStartLocation(new LatLng(0, 0));
        addTripDataActivity.getInstanceData().setEndLocation(new LatLng(1, 1));

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                activityController.pause();
                activityController.resume();
                LOG.info("Activity paused and then resumed!");
            } else if (i == 1) {
                activityController.stop();
                activityController.start();
                LOG.info("Activity paused and then resumed!");
            }

            assertThat(addTripDataActivity.getInstanceData().getActiveScreenIndex(), is(100));
            assertThat(addTripDataActivity.getInstanceData().getStartDate(), is(new Date(0)));
            assertThat(addTripDataActivity.getInstanceData().getEndDate(), is(new Date(1)));
            assertThat(addTripDataActivity.getInstanceData().getStartLocation(), is(new LatLng(0, 0)));
            assertThat(addTripDataActivity.getInstanceData().getEndLocation(), is(new LatLng(1, 1)));
        }
    }

    @Test
    public void when_tabClicked_then_activeFragmentIndexChangedToIndexAsOfOrderOfPages() {
        AddTripDataActivity addTripDataActivity = Robolectric.setupActivity(AddTripDataActivity.class);
        for (int pageIndex = 0; pageIndex < addTripDataActivity.getOrderOfPages().size(); ++pageIndex) {
            Button tabButton
                    = (Button) addTripDataActivity.findViewById(addTripDataActivity.getOrderOfPages().get(pageIndex));

            if (pageIndex != 0) {
                assertThat(
                        ((ColorDrawable) tabButton.getBackground()).getColor(),
                        is(addTripDataActivity.getColor(R.color.appBackground)));
            } else {
                assertThat(
                        ((ColorDrawable) tabButton.getBackground()).getColor(),
                        is(addTripDataActivity.getColor(R.color.activeTab)));
            }

            ShadowLooper.pauseMainLooper();
            tabButton.performClick();
            ShadowApplication.getInstance().getForegroundThreadScheduler().advanceToLastPostedRunnable();

            assertThat(addTripDataActivity.getInstanceData().getActiveScreenIndex(), is(pageIndex));
            assertThat(
                    ((ColorDrawable) tabButton.getBackground()).getColor(),
                    is(addTripDataActivity.getColor(R.color.activeTab)));
        }
    }
}