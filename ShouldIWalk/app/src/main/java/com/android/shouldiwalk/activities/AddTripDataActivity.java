package com.android.shouldiwalk.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.activities.fragments.DateTimePickerFragment;
import com.android.shouldiwalk.activities.fragments.DiscreteRateFragment;
import com.android.shouldiwalk.activities.fragments.MapLocationChooserFragment;
import com.android.shouldiwalk.activities.fragments.MeanOfTransportFragment;
import com.android.shouldiwalk.activities.fragments.ShouldIWalkFragment;
import com.android.shouldiwalk.activities.fragments.WeatherFragment;
import com.android.shouldiwalk.core.AddTripDataDefaults;
import com.android.shouldiwalk.core.AddTripDataInstanceParcelable;
import com.android.shouldiwalk.core.model.MeanOfTransport;
import com.android.shouldiwalk.core.model.WeatherStatus;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import static com.android.shouldiwalk.R.id.tabsHorizontalView;

public class AddTripDataActivity extends ShouldIWalkActivity implements
        MapLocationChooserFragment.OnMapMarkerLocationChangeListener,
        DateTimePickerFragment.OnDateChangeListener,
        MeanOfTransportFragment.OnMeanOfTransportChangeListener,
        WeatherFragment.OnWeatherChangeListener,
        DiscreteRateFragment.OnDiscreteRateSeekBarChangeListener {

    private static final String CLASS_TAG = AddTripDataActivity.class.getCanonicalName() + "-TAG";

    public static final String START_LOCATION_FRAGMENT_ID = "StartLocationFragment";
    public static final String END_LOCATION_FRAGMENT_ID = "EndLocationFragment";
    public static final String START_DATE_FRAGMENT_ID = "StartDateFragment";
    public static final String END_DATE_FRAGMENT_ID = "EndDateFragment";
    public static final String MEAN_OF_TRANSPORT_FRAGMENT_ID = "MeanOfTransportFragment";
    public static final String WEATHER_FRAGMENT_ID = "WeatherFragment";
    public static final String TRAFFIC_FRAGMENT_ID = "TrafficFragment";
    public static final String EFFORT_FRAGMENT_ID = "EffortFragment";
    public static final String RUSH_FRAGMENT_ID = "RushFragment";
    public static final String SATISFACTION_FRAGMENT_ID = "SatisfactionFragment";
    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    private TripDataFragmentPageAdapter tripDataFragmentPageAdapter;
    private ViewPager tripDataViewPager;
    private SparseIntArray orderOfPages;
    private HorizontalScrollView scrollView;
    private AddTripDataInstanceParcelable instanceData;
    private int activeScreenIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_data);
        configureActionBarToShowBackButton();
        initializeOrderOfFragmentPages();

        instanceData = getStateFromBundleOrDefaultStateIfBundleNull(savedInstanceState);

        tripDataFragmentPageAdapter = new TripDataFragmentPageAdapter(this, getSupportFragmentManager());
        tripDataViewPager = (ViewPager) findViewById(R.id.tripDataViewPager);
        tripDataViewPager.setAdapter(tripDataFragmentPageAdapter);
        tripDataViewPager.setOffscreenPageLimit(10);

        final AddTripDataActivity that = this;
        tripDataViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                that.setActiveScreenAndTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        scrollView = (HorizontalScrollView) findViewById(tabsHorizontalView);

        initializeTabs();
    }

    private AddTripDataInstanceParcelable getStateFromBundleOrDefaultStateIfBundleNull(Bundle savedInstanceState) {
        AddTripDataInstanceParcelable savedInstanceData = null;

        if (savedInstanceState != null) {
            savedInstanceData = savedInstanceState.getParcelable(AddTripDataInstanceParcelable.getIdentifier());
            Log.d(CLASS_TAG, "Restored instance state from bundle: " + savedInstanceData);
        }

        if (savedInstanceState != null && !savedInstanceData.isErrorOnDeserializing()) {
            return savedInstanceData;
        } else {
            // TODO initialize with default values from user preferences
            savedInstanceData = new AddTripDataInstanceParcelable();
            Log.d(CLASS_TAG, "Instance state bundle was null, initializing with default values.");
        }

        if (savedInstanceData.getStartLocation() == null) {
            savedInstanceData.setStartLocation(AddTripDataDefaults.START_LOCATION);
        }
        if (savedInstanceData.getEndLocation() == null) {
            savedInstanceData.setEndLocation(AddTripDataDefaults.END_LOCATION);
        }
        if (savedInstanceData.getStartDate() == null) {
            savedInstanceData.setStartDate(AddTripDataDefaults.START_DATE);
        }

        if (savedInstanceData.getEndDate() == null) {
            Date tripEndDate = new Date();
            tripEndDate.setTime(AddTripDataDefaults.START_DATE.getTime() + ONE_HOUR_IN_MILLIS);
            savedInstanceData.setEndDate(tripEndDate);
        }
        if (savedInstanceData.getMeanOfTransport() == null) {
            savedInstanceData.setMeanOfTransport(MeanOfTransport.Walk);
        }
        if (savedInstanceData.getTemperature() == 0) {
            savedInstanceData.setTemperature(15);
        }
        if (savedInstanceData.getWeatherStatus() == null) {
            savedInstanceData.setWeatherStatus(WeatherStatus.Sunny);
        }
        if (savedInstanceData.getTrafficLevel() == 0) {
            savedInstanceData.setTrafficLevel(2);
        }
        if (savedInstanceData.getEffortLevel() == 0) {
            savedInstanceData.setEffortLevel(2);
        }
        if (savedInstanceData.getRushLevel() == 0) {
            savedInstanceData.setRushLevel(2);
        }
        if (savedInstanceData.getSatisfactionLevel() == 0) {
            savedInstanceData.setSatisfactionLevel(2);
        }

        return savedInstanceData;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        Log.d(CLASS_TAG, "Prepared instance data for saving as bundle: " + instanceData);

        outState.putParcelable(AddTripDataInstanceParcelable.getIdentifier(), instanceData);
        super.onSaveInstanceState(outState);
    }

    private void configureActionBarToShowBackButton() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Action bar back button pressed
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void initializeOrderOfFragmentPages() {
        orderOfPages = new SparseIntArray();
        orderOfPages.put(0, R.id.gotoStartLocationScreenButton);
        orderOfPages.put(1, R.id.gotoStartDateTimeScreenButton);
        orderOfPages.put(2, R.id.gotoEndLocationScreenButton);
        orderOfPages.put(3, R.id.gotoEndDateTimeScreenButton);
        orderOfPages.put(4, R.id.gotoWeatherScreenButton);
        orderOfPages.put(5, R.id.gotoMeanOfTransportScreenButton);
        orderOfPages.put(6, R.id.gotoTrafficLevelScreenButton);
        orderOfPages.put(7, R.id.gotoEffortLevelScreenButton);
        orderOfPages.put(8, R.id.gotoRushLevelScreenButton);
        orderOfPages.put(9, R.id.gotoSatisfactionLevelScreenButton);
    }

    private void initializeTabs() {
        int activeScreenIndex = this.instanceData.getActiveScreenIndex();
        int nextScreenIndex = activeScreenIndex + 1;
        if (nextScreenIndex >= orderOfPages.size()) {
            nextScreenIndex = orderOfPages.size() - 1;
        }

        Log.v(CLASS_TAG, "Setting active screen to: " + activeScreenIndex);
        Log.v(CLASS_TAG, "Setting next active screen to: " + nextScreenIndex);

        setActiveScreenAndTab(activeScreenIndex);
        linkNextScreenButtonToPageIndex(nextScreenIndex);
        Button button;

        for (int pageIndex = 0; pageIndex < orderOfPages.size(); ++pageIndex) {
            button = (Button) findViewById(orderOfPages.get(pageIndex));
            final int tmpFinalPageIndex = pageIndex;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActiveScreenAndTab(tmpFinalPageIndex);
                }
            });
        }
    }

    public void setActiveScreenAndTab(int tabPositionIndex) {
        Button oldActiveButton
                = (Button) findViewById(orderOfPages.get(this.activeScreenIndex));

        if (oldActiveButton != null) {
            oldActiveButton.setBackgroundColor(getColor(R.color.appBackground));
        }

        this.activeScreenIndex = tabPositionIndex;
        this.instanceData.setActiveScreenIndex(tabPositionIndex);

        Button newActiveButton = (Button) findViewById(orderOfPages.get(tabPositionIndex));
        if (newActiveButton != null) {
            newActiveButton.setBackgroundColor(getColor(R.color.activeTab));
        }

        tripDataViewPager.setCurrentItem(activeScreenIndex, false);
        linkNextScreenButtonToPageIndex(activeScreenIndex + 1);
        onWindowFocusChanged(true); // override call to this method to update tab scroll for the tab to be viewable
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            Button activeTabButton = (Button) findViewById(orderOfPages.get(this.activeScreenIndex));

            Log.v(CLASS_TAG, "Scrolling to active screen tab index: " + activeScreenIndex + ".");
            Log.v(CLASS_TAG, "Button X coordinate: " + activeTabButton.getX());

            ObjectAnimator.ofInt(
                    scrollView,
                    "scrollX",
                    (int) (activeTabButton.getX() - 0.5 * activeTabButton.getWidth()))
                    .setDuration(500).start();

        }
    }

    private void linkNextScreenButtonToPageIndex(final int tabIndexToDepartTo) {
        Button nextScreenButton = (Button) findViewById(R.id.gotoNextScreenButton);
        if (tabIndexToDepartTo == this.orderOfPages.size()) {
            // The last screen (page) is the active page
            nextScreenButton.setText(R.string.gotoNextScreenButtonFinalText);
            nextScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(AddTripDataInstanceParcelable.START_LOCATION, instanceData.getStartLocation());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.END_LOCATION, instanceData.getEndLocation());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.START_DATE, instanceData.getStartDate());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.END_DATE, instanceData.getEndDate());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.WEATHER_STATUS, instanceData.getWeatherStatus().toString());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.TEMPERATURE, instanceData.getTemperature());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.MEAN_OF_TRANSPORT, instanceData.getMeanOfTransport().toString());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.EFFORT_LEVEL, instanceData.getEffortLevel());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.TRAFFIC_LEVEL, instanceData.getTrafficLevel());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.RUSH_LEVEL, instanceData.getRushLevel());
                    returnIntent.putExtra(AddTripDataInstanceParcelable.SATISFACTION_LEVEL, instanceData.getSatisfactionLevel());

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });
        } else {
            nextScreenButton.setText(R.string.gotoNextScreenButtonIntermediaryText);
            nextScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActiveScreenAndTab(tabIndexToDepartTo);
                }
            });
        }
    }

    @Override
    public void onMarkerPositionChanged(String sourceFragment, LatLng position) {
        Log.d(CLASS_TAG, String.format("Received new marker data {%s} from {%s}", position, sourceFragment));
        switch (sourceFragment) {
            case START_LOCATION_FRAGMENT_ID:
                instanceData.setStartLocation(position);
                break;
            case END_LOCATION_FRAGMENT_ID:
                instanceData.setEndLocation(position);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }

    }

    @Override
    public void onDateChange(String sourceFragmentId, Date date) {
        Log.d(CLASS_TAG, String.format("Received new date {%s} from {%s}", date, sourceFragmentId));
        switch (sourceFragmentId) {
            case START_DATE_FRAGMENT_ID:
                instanceData.setStartDate(date);
                break;
            case END_DATE_FRAGMENT_ID:
                instanceData.setEndDate(date);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }

    }

    @Override
    public void onMeanOfTransportChange(String sourceFragmentId, MeanOfTransport meanOfTransport) {
        Log.d(CLASS_TAG, String.format("Received new mean of transport {%s} from {%s}", meanOfTransport, sourceFragmentId));
        switch (sourceFragmentId) {
            case MEAN_OF_TRANSPORT_FRAGMENT_ID:
                instanceData.setMeanOfTransport(meanOfTransport);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }
    }

    @Override
    public void onTemperatureChange(String sourceFragmentId, int temperature) {
        Log.d(CLASS_TAG, String.format("Received new temperature {%d} from {%s}", temperature, sourceFragmentId));
        switch (sourceFragmentId) {
            case WEATHER_FRAGMENT_ID:
                instanceData.setTemperature(temperature);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }
    }

    @Override
    public void onWeatherStatusChange(String sourceFragmentId, WeatherStatus weatherStatus) {
        Log.d(CLASS_TAG, String.format("Received new weather status {%s} from {%s}", weatherStatus, sourceFragmentId));
        switch (sourceFragmentId) {
            case WEATHER_FRAGMENT_ID:
                instanceData.setWeatherStatus(weatherStatus);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }
    }

    @Override
    public void onSeekBarChange(String sourceFragmentId, int seekBarPosition) {
        Log.d(CLASS_TAG, String.format("Received seekbar position {%d} from {%s}", seekBarPosition, sourceFragmentId));
        if (sourceFragmentId == null) {
            return;
        }

        switch (sourceFragmentId) {
            case TRAFFIC_FRAGMENT_ID:
                instanceData.setTrafficLevel(seekBarPosition);
                break;
            case EFFORT_FRAGMENT_ID:
                instanceData.setEffortLevel(seekBarPosition);
                break;
            case RUSH_FRAGMENT_ID:
                instanceData.setRushLevel(seekBarPosition);
                break;
            case SATISFACTION_FRAGMENT_ID:
                instanceData.setSatisfactionLevel(seekBarPosition);
                break;
            default:
                throw new UnsupportedOperationException("Switch branch not implemented!");
        }
    }

    public static class TripDataFragmentPageAdapter extends FragmentPagerAdapter {
        private final AddTripDataActivity addTripDataActivity;

        /**
         * The following list of private variables encompasses fragments inside this view.
         * These fragments may be accessed by the enclosing class of this static class
         * in order to get certain data about their status.
         */
        private MapLocationChooserFragment startLocationMapFragment;
        private MapLocationChooserFragment endLocationMapFragment;
        private DateTimePickerFragment startDateTimePickerFragment;
        private DateTimePickerFragment endDateTimePickerFragment;
        private MeanOfTransportFragment meanOfTransportFragment;
        private WeatherFragment weatherFragment;
        private DiscreteRateFragment trafficFragment;
        private DiscreteRateFragment effortFragment;
        private DiscreteRateFragment rushFragment;
        private DiscreteRateFragment satisfactionFragment;

        TripDataFragmentPageAdapter(AddTripDataActivity addTripDataActivity, FragmentManager fm) {
            super(fm);
            this.addTripDataActivity = addTripDataActivity;

            this.startLocationMapFragment = (MapLocationChooserFragment) initializeLocationChooserFragment(
                    addTripDataActivity.instanceData.getStartLocation(), START_LOCATION_FRAGMENT_ID);
            this.endLocationMapFragment = (MapLocationChooserFragment) initializeLocationChooserFragment(
                    addTripDataActivity.instanceData.getEndLocation(), END_LOCATION_FRAGMENT_ID);

            this.startDateTimePickerFragment = (DateTimePickerFragment) initializeDateTimePickerFragment(
                    addTripDataActivity.instanceData.getStartDate(), START_DATE_FRAGMENT_ID);
            this.endDateTimePickerFragment = (DateTimePickerFragment) initializeDateTimePickerFragment(
                    addTripDataActivity.instanceData.getEndDate(), END_DATE_FRAGMENT_ID);

            this.meanOfTransportFragment = (MeanOfTransportFragment) initializeMeanOfTransportFragment(
                    addTripDataActivity.instanceData.getMeanOfTransport(), MEAN_OF_TRANSPORT_FRAGMENT_ID);

            this.weatherFragment = (WeatherFragment) initializeWeatherFragment(
                    addTripDataActivity.instanceData.getTemperature(),
                    addTripDataActivity.instanceData.getWeatherStatus(),
                    WEATHER_FRAGMENT_ID
            );

            this.trafficFragment = (DiscreteRateFragment) initializeTrafficFragment(
                    addTripDataActivity.instanceData.getTrafficLevel(), 5, TRAFFIC_FRAGMENT_ID);
            this.effortFragment = (DiscreteRateFragment) initializeEffortFragment(
                    addTripDataActivity.instanceData.getEffortLevel(), 5, EFFORT_FRAGMENT_ID);
            this.rushFragment = (DiscreteRateFragment) initializeRushFragment(
                    addTripDataActivity.instanceData.getRushLevel(), 5, RUSH_FRAGMENT_ID);
            this.satisfactionFragment = (DiscreteRateFragment) initializeSatisfactionFragment(
                    addTripDataActivity.instanceData.getSatisfactionLevel(), 5, SATISFACTION_FRAGMENT_ID);
        }


        @Override
        public int getCount() {
            return addTripDataActivity.orderOfPages.size();
        }

        @Override
        public Fragment getItem(int position) {
            switch (this.addTripDataActivity.orderOfPages.get(position)) {
                case R.id.gotoStartLocationScreenButton:
                    return startLocationMapFragment;
                case R.id.gotoEndLocationScreenButton:
                    return endLocationMapFragment;
                case R.id.gotoStartDateTimeScreenButton:
                    return startDateTimePickerFragment;
                case R.id.gotoEndDateTimeScreenButton:
                    return endDateTimePickerFragment;
                case R.id.gotoMeanOfTransportScreenButton:
                    return meanOfTransportFragment;
                case R.id.gotoWeatherScreenButton:
                    return weatherFragment;
                case R.id.gotoTrafficLevelScreenButton:
                    return trafficFragment;
                case R.id.gotoEffortLevelScreenButton:
                    return effortFragment;
                case R.id.gotoRushLevelScreenButton:
                    return rushFragment;
                case R.id.gotoSatisfactionLevelScreenButton:
                    return satisfactionFragment;
                default:
                    throw new UnsupportedOperationException("Fragment not mapped in switch case!");
            }

        }

        private Fragment initializeLocationChooserFragment(LatLng location, String fragmentId) {
            Fragment mapLocationChooserFragment = new MapLocationChooserFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putParcelable(
                    MapLocationChooserFragment.INITIAL_LOCATION,
                    location);
            fragmentArguments.putString(
                    MapLocationChooserFragment.FRAGMENT_ID,
                    fragmentId);

            mapLocationChooserFragment.setArguments(fragmentArguments);
            return mapLocationChooserFragment;
        }

        private Fragment initializeDateTimePickerFragment(Date date, String fragmentId) {
            Fragment dateTimePickerFragment = new DateTimePickerFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putSerializable(
                    DateTimePickerFragment.INITIAL_DATE,
                    date);
            fragmentArguments.putString(
                    DateTimePickerFragment.FRAGMENT_ID,
                    fragmentId);

            switch (fragmentId) {
                case START_DATE_FRAGMENT_ID:
                    fragmentArguments.putString(
                            DateTimePickerFragment.DATE_PICKER_CARD_TITLE,
                            addTripDataActivity.getString(R.string.dateTimePickerStartDateTitle));
                    break;
                case END_DATE_FRAGMENT_ID:
                    fragmentArguments.putString(
                            DateTimePickerFragment.DATE_PICKER_CARD_TITLE,
                            addTripDataActivity.getString(R.string.dateTimePickerEndDateTitle));
                    break;
                default:
                    throw new UnsupportedOperationException("Case not covered!");
            }

            dateTimePickerFragment.setArguments(fragmentArguments);
            return dateTimePickerFragment;
        }

        private Fragment initializeMeanOfTransportFragment(MeanOfTransport meanOfTransport, String fragmentId) {
            Fragment meanOfTransportFragment = new MeanOfTransportFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    MeanOfTransportFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putString(
                    MeanOfTransportFragment.INITIAL_MEAN_OF_TRANSPORT,
                    meanOfTransport.toString());

            meanOfTransportFragment.setArguments(fragmentArguments);
            return meanOfTransportFragment;
        }

        private Fragment initializeWeatherFragment(int temperature, WeatherStatus weatherStatus, String fragmentId) {
            Fragment weatherFragment = new WeatherFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    WeatherFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putInt(
                    WeatherFragment.INITIAL_TEMPERATURE,
                    temperature);
            fragmentArguments.putString(
                    WeatherFragment.INITIAL_WEATHER_STATUS,
                    weatherStatus.toString());

            weatherFragment.setArguments(fragmentArguments);
            return weatherFragment;
        }

        private Fragment initializeTrafficFragment(int trafficLevel, int maxTrafficLevel, String fragmentId) {
            Fragment trafficFragment = new DiscreteRateFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    DiscreteRateFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putInt(
                    DiscreteRateFragment.INITIAL_SEEKBAR_VALUE,
                    trafficLevel);
            fragmentArguments.putInt(
                    DiscreteRateFragment.MAX_SEEKBAR_VALUE,
                    maxTrafficLevel);
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_TITLE,
                    "Traffic level");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_SUBTITLE,
                    "Slide your finger on the seek-bar below to adjust how heavy the traffic was during your trip. " +
                            "Higher values mean more intensive traffic. The highest value should only be used to " +
                            "illustrate severe traffic blocks and long wait times during the trip.");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_UNIT_DESCRIPTION,
                    "Traffic Pain Units");

            trafficFragment.setArguments(fragmentArguments);
            return trafficFragment;
        }

        private Fragment initializeEffortFragment(int effortLevel, int maxEffortLevel, String fragmentId) {
            Fragment effortFragment = new DiscreteRateFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    DiscreteRateFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putInt(
                    DiscreteRateFragment.INITIAL_SEEKBAR_VALUE,
                    effortLevel);
            fragmentArguments.putInt(
                    DiscreteRateFragment.MAX_SEEKBAR_VALUE,
                    maxEffortLevel);
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_TITLE,
                    "Effort level");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_SUBTITLE,
                    "Slide your finger on the seek-bar below to adjust how much effort you put in to make this trip. " +
                            "Higher values mean more effort.");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_UNIT_DESCRIPTION,
                    "Effort Units");

            effortFragment.setArguments(fragmentArguments);
            return effortFragment;
        }

        private Fragment initializeRushFragment(int rushLevel, int maxRushLevel, String fragmentId) {
            Fragment effortFragment = new DiscreteRateFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    DiscreteRateFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putInt(
                    DiscreteRateFragment.INITIAL_SEEKBAR_VALUE,
                    rushLevel);
            fragmentArguments.putInt(
                    DiscreteRateFragment.MAX_SEEKBAR_VALUE,
                    maxRushLevel);
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_TITLE,
                    "Rush level");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_SUBTITLE,
                    "Slide your finger on the seek-bar below to adjust how much you were in a rush during the trip. " +
                            "Higher values mean higher incentives to get to the destination as fast as possible.");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_UNIT_DESCRIPTION,
                    "Rush Points");

            effortFragment.setArguments(fragmentArguments);
            return effortFragment;
        }

        private Fragment initializeSatisfactionFragment(
                int satisfactionLevel, int maxSatisfactionLevel, String fragmentId) {

            Fragment effortFragment = new DiscreteRateFragment();

            Bundle fragmentArguments = new Bundle();
            fragmentArguments.putString(
                    DiscreteRateFragment.FRAGMENT_ID,
                    fragmentId);
            fragmentArguments.putInt(
                    DiscreteRateFragment.INITIAL_SEEKBAR_VALUE,
                    satisfactionLevel);
            fragmentArguments.putInt(
                    DiscreteRateFragment.MAX_SEEKBAR_VALUE,
                    maxSatisfactionLevel);
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_TITLE,
                    "Overall satisfaction level");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_SUBTITLE,
                    "Slide your finger on the seek-bar below to adjust how satisfied you were with making the trip. " +
                            "Higher values mean that you were more satisfied with the trip.");
            fragmentArguments.putString(
                    DiscreteRateFragment.SEEKBAR_UNIT_DESCRIPTION,
                    "Yay Points");

            effortFragment.setArguments(fragmentArguments);
            return effortFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (activeScreenIndex == 0) {
            super.onBackPressed();
        } else {
            setActiveScreenAndTab(activeScreenIndex - 1);
        }
    }

    @VisibleForTesting
    SparseIntArray getOrderOfPages() {
        return orderOfPages;
    }

    @VisibleForTesting
    AddTripDataInstanceParcelable getInstanceData() {
        return instanceData;
    }

    @VisibleForTesting
    TripDataFragmentPageAdapter getTripDataFragmentPageAdapter() {
        return tripDataFragmentPageAdapter;
    }
}
