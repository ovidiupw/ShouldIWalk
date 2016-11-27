package com.android.shouldiwalk.core.model;

import java.util.Objects;

public class TripData {
    private int id;
    private int startLocationId;
    private int endLocationId;
    private int startMillis;
    private int endMillis;
    private WeatherStatus weatherStatus;
    private int temperatureCelsius;
    private int trafficLevel;
    private int tripDifficulty;
    private int timeEstimation;
    private int rushLevel;
    private int meanOfTransport;
    private int satisfactionLevel;

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

    public int getTimeEstimation() {
        return timeEstimation;
    }

    public void setTimeEstimation(int timeEstimation) {
        this.timeEstimation = timeEstimation;
    }

    public int getRushLevel() {
        return rushLevel;
    }

    public void setRushLevel(int rushLevel) {
        this.rushLevel = rushLevel;
    }

    public int getMeanOfTransport() {
        return meanOfTransport;
    }

    public void setMeanOfTransport(int meanOfTransport) {
        this.meanOfTransport = meanOfTransport;
    }

    public int getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(int satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
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
                timeEstimation == tripData.timeEstimation &&
                rushLevel == tripData.rushLevel &&
                meanOfTransport == tripData.meanOfTransport &&
                satisfactionLevel == tripData.satisfactionLevel &&
                weatherStatus == tripData.weatherStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startLocationId, endLocationId, startMillis, endMillis, weatherStatus, temperatureCelsius, trafficLevel, tripDifficulty, timeEstimation, rushLevel, meanOfTransport, satisfactionLevel);
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
                ", timeEstimation=" + timeEstimation +
                ", rushLevel=" + rushLevel +
                ", meanOfTransport=" + meanOfTransport +
                ", satisfactionLevel=" + satisfactionLevel +
                '}';
    }
}
