package com.android.shouldiwalk.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.shouldiwalk.core.AddTripDataInstanceParcelable;
import com.android.shouldiwalk.core.model.MeanOfTransport;
import com.android.shouldiwalk.core.model.TripData;
import com.android.shouldiwalk.core.model.WeatherStatus;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Util {

    public static boolean isRoboUnitTest() {
        return "robolectric".equals(Build.FINGERPRINT);
    }

    public static AddTripDataInstanceParcelable getAddTripDataInstanceParcelableFromIntent(Intent intent) {
        AddTripDataInstanceParcelable tripData = new AddTripDataInstanceParcelable();
        tripData.setStartLocation((LatLng) intent.getParcelableExtra(AddTripDataInstanceParcelable.START_LOCATION));
        tripData.setEndLocation((LatLng) intent.getParcelableExtra(AddTripDataInstanceParcelable.END_LOCATION));
        tripData.setStartDate((Date) intent.getSerializableExtra(AddTripDataInstanceParcelable.START_DATE));
        tripData.setEndDate((Date) intent.getSerializableExtra(AddTripDataInstanceParcelable.END_DATE));
        tripData.setWeatherStatus(WeatherStatus.valueOf(intent.getStringExtra(AddTripDataInstanceParcelable.WEATHER_STATUS)));
        tripData.setTemperature(intent.getIntExtra(AddTripDataInstanceParcelable.TEMPERATURE, 0));
        tripData.setMeanOfTransport(MeanOfTransport.valueOf(intent.getStringExtra(AddTripDataInstanceParcelable.MEAN_OF_TRANSPORT)));
        tripData.setEffortLevel(intent.getIntExtra(AddTripDataInstanceParcelable.EFFORT_LEVEL, 0));
        tripData.setTrafficLevel(intent.getIntExtra(AddTripDataInstanceParcelable.TRAFFIC_LEVEL, 0));
        tripData.setRushLevel(intent.getIntExtra(AddTripDataInstanceParcelable.RUSH_LEVEL, 0));
        tripData.setSatisfactionLevel(intent.getIntExtra(AddTripDataInstanceParcelable.SATISFACTION_LEVEL, 0));
        return tripData;
    }

    public static TripData getTripDataFromUserDataAndLocationIds(
            AddTripDataInstanceParcelable userData, int startLocationId, int endLocationId) {

        TripData tripData = new TripData();
        tripData.setStartLocationId(startLocationId);
        tripData.setEndLocationId(endLocationId);
        tripData.setStartMillis(userData.getStartDate().getTime());
        tripData.setEndMillis(userData.getEndDate().getTime());
        tripData.setWeatherStatus(userData.getWeatherStatus());
        tripData.setTemperatureCelsius(userData.getTemperature());
        tripData.setMeanOfTransport(userData.getMeanOfTransport());
        tripData.setTrafficLevel(userData.getTrafficLevel());
        tripData.setTripDifficulty(userData.getEffortLevel());
        tripData.setRushLevel(userData.getRushLevel());
        tripData.setSatisfactionLevel(userData.getSatisfactionLevel());
        return tripData;
    }
}
