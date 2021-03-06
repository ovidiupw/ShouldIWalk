package com.android.shouldiwalk.core.model;

import com.android.shouldiwalk.core.annotations.SQLiteTable;

import java.util.Objects;

@SQLiteTable(name = "Locations")
public class Location {
    private int id;
    private double latitude;
    private double longitude;

    public static String getIdDBIdentifier() {
        return "id";
    }

    public static String getLatitudeDBIdentifier() {
        return "latitude";
    }

    public static String getLongitudeDBIdentifier() {
        return "longitude";
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(int id, double latitude, double longitude) {
        this(latitude, longitude);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return id == location.id &&
                Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
