package com.android.shouldiwalk.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.activities.AddTripDataActivity;
import com.android.shouldiwalk.core.exceptions.InvalidArgumentException;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import helpers.MapLocationChooserFragmentUtil;
import helpers.TestHelper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MapLocationChooserFragmentTest {

    private static final String FRAGMENT_TAG = "MapFragment_TAG";
    private MapLocationChooserFragment mapFragment;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        mapFragment = new MapLocationChooserFragment();
    }

    @Test(expected = ClassCastException.class)
    public void when_enclosingActivityNotImplementingOnMarkerLocationChangeListener_then_classCastException() {
        SupportFragmentTestUtil.startFragment(mapFragment);
    }

    @Test
    public void when_instantiatingFragment_and_fragmentIdNotSuppliedAsArgument_then_exceptionThrown() {
        mapFragment.setArguments(new Bundle());

        expectedException.expect(InvalidArgumentException.class);
        expectedException.expectMessage("The fragment id must be supplied as argument!");
        SupportFragmentTestUtil.startFragment(mapFragment, MapLocationChooserFragmentUtil.class);
    }

    @Test
    public void when_instantiatingFragment_and_initialLocationNotSuppliedAsArgument_then_exceptionThrown() {
        Bundle arguments = new Bundle();
        arguments.putString(MapLocationChooserFragment.FRAGMENT_ID, "MyFragment");
        mapFragment.setArguments(arguments);

        expectedException.expect(InvalidArgumentException.class);
        expectedException.expectMessage("The initial location must be supplied as argument!");
        SupportFragmentTestUtil.startFragment(mapFragment, MapLocationChooserFragmentUtil.class);
    }

    @Test
    public void when_fragmentReinitialized_then_stateRestoredCorrectly() throws Exception {
        FragmentActivity fragmentActivity = Robolectric.setupActivity(AddTripDataActivity.class);

        LatLng markerLatLng = new LatLng(1, 2);
        mapFragment.setMapMarkerLatLng(markerLatLng);

        Bundle arguments = new Bundle();
        arguments.putParcelable(MapLocationChooserFragment.INITIAL_LOCATION, new LatLng(0, 0));
        arguments.putString(MapLocationChooserFragment.FRAGMENT_ID, FRAGMENT_TAG);
        mapFragment.setArguments(arguments);

        TestHelper.addMapFragmentToActivity(fragmentActivity, mapFragment, FRAGMENT_TAG);

        fragmentActivity.recreate();
        MapLocationChooserFragment recreatedFragment
                = (MapLocationChooserFragment) fragmentActivity
                .getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        assertThat(recreatedFragment.getMapMarkerLatLng(), is(markerLatLng));
    }
}