package com.android.shouldiwalk.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.shouldiwalk.activities.AddTripDataActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Objects;

public class AddTripDataInstanceParcelable implements Parcelable {

    private static final String CLASS_TAG = AddTripDataInstanceParcelable.class.getCanonicalName() + "-TAG";

    private int activeScreenIndex;
    private LatLng startLocation;
    private LatLng endLocation;
    private Date startDate;
    private Date endDate;

    public static String getIdentifier() {
        return "AddTripDataInstanceParcelable";
    }

    public AddTripDataInstanceParcelable() {
        /* Intentionally left blank */
    }

    protected AddTripDataInstanceParcelable(Parcel in) {
        this.endDate = (Date) in.readSerializable();
        this.startDate = (Date) in.readSerializable();
        this.endLocation = in.readParcelable(LatLng.class.getClassLoader());
        this.startLocation = in.readParcelable(LatLng.class.getClassLoader());
        this.activeScreenIndex = in.readInt();
        Log.i(CLASS_TAG, "Successfully loaded trip data instance parcelable: " + this);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(activeScreenIndex);
        dest.writeParcelable(startLocation, flags);
        dest.writeParcelable(endLocation, flags);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<AddTripDataInstanceParcelable> CREATOR
            = new Creator<AddTripDataInstanceParcelable>() {
        @Override
        public AddTripDataInstanceParcelable createFromParcel(Parcel in) {
            return new AddTripDataInstanceParcelable(in);
        }

        @Override
        public AddTripDataInstanceParcelable[] newArray(int size) {
            return new AddTripDataInstanceParcelable[size];
        }
    };


    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public int getActiveScreenIndex() {
        return activeScreenIndex;
    }

    public void setActiveScreenIndex(int activeScreenIndex) {
        this.activeScreenIndex = activeScreenIndex;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddTripDataInstanceParcelable that = (AddTripDataInstanceParcelable) o;
        return activeScreenIndex == that.activeScreenIndex &&
                Objects.equals(startLocation, that.startLocation) &&
                Objects.equals(endLocation, that.endLocation) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeScreenIndex, startLocation, endLocation, startDate, endDate);
    }

    @Override
    public String toString() {
        return "AddTripDataInstanceParcelable{" +
                "activeScreenIndex=" + activeScreenIndex +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
