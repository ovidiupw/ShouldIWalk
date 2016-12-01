package com.android.shouldiwalk.core;

import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.Location;
import com.android.shouldiwalk.core.model.TripData;

public class DataValidator {

    public static void validateLocation(Location location) {
        if (location.getLatitude() > 90. || location.getLatitude() < -90.) {
            throw new InvalidDataException("Latitude must be inside interval [-90, 90].");
        }

        if (location.getLongitude() > 180. || location.getLongitude() < -180.) {
            throw new InvalidDataException("Longitude must be inside interval [-180, 180].");
        }
    }

    public static void validateLocationWithId(Location location) {
        if (location.getId() < 0) {
            throw new InvalidDataException("When updating location in db, its id must be set.");
        }
        validateLocation(location);
    }

    public static void validateTripData(TripData tripData) {
        final long ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

        if (tripData.getStartLocationId() < 0) {
            throw new InvalidDataException("StartLocationId must be a positive integer.");
        }
        if (tripData.getEndLocationId() < 0) {
            throw new InvalidDataException("EndLocationId must be a positive integer.");
        }
        if (tripData.getStartMillis() < 0) {
            throw new InvalidDataException("StartMillis cannot be negative.");
        }
        if (tripData.getEndMillis() < 0) {
            throw new InvalidDataException("EndMillis cannot be negative.");
        }
        if (tripData.getStartMillis() >= tripData.getEndMillis()) {
            throw new InvalidDataException("StartMillis must be lower than EndMillis.");
        }
        if (tripData.getEndMillis() > tripData.getStartMillis() + ONE_DAY_IN_MILLIS) {
            throw new InvalidDataException("Trip must last at most one day.");
        }
        if (tripData.getTemperatureCelsius() > 60 || tripData.getTemperatureCelsius() < -80) {
            throw new InvalidDataException("TemperatureCelsius must be inside interval [-80, 60].");
        }
        if (tripData.getTrafficLevel() > 5 || tripData.getTrafficLevel() < 0) {
            throw new InvalidDataException("TrafficLevel must be a value in interval [0, 5].");
        }
        if (tripData.getTripDifficulty() > 5 || tripData.getTripDifficulty() < 0) {
            throw new InvalidDataException("TripDifficulty must be a value in interval [0, 5].");
        }
        if (tripData.getRushLevel() > 5 || tripData.getRushLevel() < 0) {
            throw new InvalidDataException("RushLevel must be a value in interval [0, 5].");
        }
        if (tripData.getSatisfactionLevel() > 5 || tripData.getSatisfactionLevel() < 0) {
            throw new InvalidDataException("SatisfactionLevel must be a value in interval [0, 5].");
        }

    }
}
