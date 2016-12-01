package com.android.shouldiwalk.core.model;

import com.android.shouldiwalk.core.annotations.SQLiteTable;

import java.util.Objects;

/**
 * This class encapsulates data about a trip. Please address the field-level java-docs
 * for more information about what each field of this class means.
 */
@SQLiteTable(name = "TripData")
public class TripData {
    /**
     * The database unique identifier for the current TripData item.
     * This field should be set only for database loaded TripData object information.
     */
    private int id;

    /**
     * The start {@link Location} for the current trip, expressed as a unique identifier
     * for the database-saved location item
     */
    private int startLocationId;

    /**
     * The end {@link Location} for the current trip, expressed as a unique identifier
     * for the database-saved location item
     */
    private int endLocationId;

    /**
     * Epoch milliseconds that represent the start time of the trip.
     */
    private int startMillis;

    /**
     * Epoch milliseconds that represent the end time of the trip.
     */
    private int endMillis;

    /**
     * Weather status identifier used to map certain weather conditions to a numeral value.
     */
    private WeatherStatus weatherStatus;

    /**
     * The medium temperature registered across the trip (may be user or automatically recorded).
     */
    private int temperatureCelsius;

    /**
     * Traffic density level, a numerical constant. Higher values mean higher traffic density.
     */
    private int trafficLevel;

    /**
     * User-set trip difficulty level, a numerical constant.
     * Higher values mean higher trip difficulty.
     */
    private int tripDifficulty;

    /**
     * User-set rush level; a numerical constant. The higher the value,
     * the more the user was in a rush.
     */
    private int rushLevel;

    /**
     * The mean of transport used by the user for the trip.
     */
    private MeanOfTransport meanOfTransport;

    /**
     * User satisfaction level. The higher the value, the more satisfied
     * the user felt about the trip.
     */
    private int satisfactionLevel;

    public static String getIdDBIdentifier() {
        return "id";
    }

    public static String getStartLocationIdDBIdentifier() {
        return "start_location_id";
    }

    public static String getEndLocationIdDBIdentifier() {
        return "end_location_id";
    }

    public static String getStartMillisDBIdentifier() {
        return "start_millis";
    }

    public static String getEndMillisDBIdentifier() {
        return "end_millis";
    }

    public static String getWeatherStatusDBIdentifier() {
        return "weather_status";
    }

    public static String getTemperatureCelsiusDBIdentifier() {
        return "temperature_celsius";
    }

    public static String getTrafficLevelDBIdentifier() {
        return "traffic_level";
    }

    public static String getTripDifficultyDBIdentifier() {
        return "trip_difficulty";
    }

    public static String getRushLevelDBIdentifier() {
        return "rush_level";
    }

    public static String getMeanOfTransportDBIdentifier() {
        return "mean_of_transport";
    }

    public static String getSatisfactionLevelDBIdentifier() {
        return "satisfaction_level";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartLocationId() {
        return startLocationId;
    }

    public void setStartLocationId(int startLocationId) {
        this.startLocationId = startLocationId;
    }

    public int getEndLocationId() {
        return endLocationId;
    }

    public void setEndLocationId(int endLocationId) {
        this.endLocationId = endLocationId;
    }

    public int getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(int startMillis) {
        this.startMillis = startMillis;
    }

    public int getEndMillis() {
        return endMillis;
    }

    public void setEndMillis(int endMillis) {
        this.endMillis = endMillis;
    }

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    public int getTemperatureCelsius() {
        return temperatureCelsius;
    }

    @Override
    public String toString() {
        return "TripData{" +
                "id=" + id +
                ", startLocationId=" + startLocationId +
                ", endLocationId=" + endLocationId +
                ", startMillis=" + startMillis +
                ", endMillis=" + endMillis +
                ", weatherStatus=" + weatherStatus +
                ", temperatureCelsius=" + temperatureCelsius +
                ", trafficLevel=" + trafficLevel +
                ", tripDifficulty=" + tripDifficulty +
                ", rushLevel=" + rushLevel +
                ", meanOfTransport=" + meanOfTransport +
                ", satisfactionLevel=" + satisfactionLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripData tripData = (TripData) o;
        return id == tripData.id &&
                startLocationId == tripData.startLocationId &&
                endLocationId == tripData.endLocationId &&
                startMillis == tripData.startMillis &&
                endMillis == tripData.endMillis &&
                temperatureCelsius == tripData.temperatureCelsius &&
                trafficLevel == tripData.trafficLevel &&
                tripDifficulty == tripData.tripDifficulty &&
                rushLevel == tripData.rushLevel &&
                satisfactionLevel == tripData.satisfactionLevel &&
                weatherStatus == tripData.weatherStatus &&
                meanOfTransport == tripData.meanOfTransport;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startLocationId, endLocationId, startMillis, endMillis, weatherStatus, temperatureCelsius, trafficLevel, tripDifficulty, rushLevel, meanOfTransport, satisfactionLevel);
    }

    public void setTemperatureCelsius(int temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public int getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(int trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public int getTripDifficulty() {
        return tripDifficulty;
    }

    public void setTripDifficulty(int tripDifficulty) {
        this.tripDifficulty = tripDifficulty;
    }

    public int getRushLevel() {
        return rushLevel;
    }

    public void setRushLevel(int rushLevel) {
        this.rushLevel = rushLevel;
    }

    public int getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(int satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }

    public MeanOfTransport getMeanOfTransport() {

        return meanOfTransport;
    }

    public void setMeanOfTransport(MeanOfTransport meanOfTransport) {
        this.meanOfTransport = meanOfTransport;
    }

}
