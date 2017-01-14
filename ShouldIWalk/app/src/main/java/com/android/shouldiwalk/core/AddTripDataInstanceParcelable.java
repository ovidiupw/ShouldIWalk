package com.android.shouldiwalk.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.shouldiwalk.activities.AddTripDataActivity;
import com.android.shouldiwalk.core.model.MeanOfTransport;
import com.android.shouldiwalk.core.model.WeatherStatus;
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
    private MeanOfTransport meanOfTransport;
    private int trafficLevel;
    private int effortLevel;
    private int rushLevel;
    private int satisfactionLevel;

    private boolean errorOnDeserializing;
    private int temperature;
    private WeatherStatus weatherStatus;

    public static String getIdentifier() {
        return "AddTripDataInstanceParcelable";
    }

    public AddTripDataInstanceParcelable() {
        /* Intentionally left blank */
    }

    protected AddTripDataInstanceParcelable(Parcel in) {
        try {
            this.satisfactionLevel = in.readInt();
            this.rushLevel = in.readInt();
            this.effortLevel = in.readInt();
            this.trafficLevel = in.readInt();
            this.temperature = in.readInt();
            this.weatherStatus = WeatherStatus.valueOf(in.readString());
            this.meanOfTransport = MeanOfTransport.valueOf(in.readString());
            this.endDate = (Date) in.readSerializable();
            this.startDate = (Date) in.readSerializable();
            this.endLocation = in.readParcelable(LatLng.class.getClassLoader());
            this.startLocation = in.readParcelable(LatLng.class.getClassLoader());
            this.activeScreenIndex = in.readInt();
            Log.i(CLASS_TAG, "Successfully loaded trip data instance parcelable: " + this);
        } catch (RuntimeException e) {
            Log.e(CLASS_TAG, e.getMessage());
            this.errorOnDeserializing = true;
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(activeScreenIndex);
        dest.writeParcelable(startLocation, flags);
        dest.writeParcelable(endLocation, flags);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
        dest.writeString(meanOfTransport.toString());
        dest.writeString(weatherStatus.toString());
        dest.writeInt(temperature);
        dest.writeInt(trafficLevel);
        dest.writeInt(effortLevel);
        dest.writeInt(rushLevel);
        dest.writeInt(satisfactionLevel);
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

    public int getEffortLevel() {
        return effortLevel;
    }

    public void setEffortLevel(int effortLevel) {
        this.effortLevel = effortLevel;
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

    public MeanOfTransport getMeanOfTransport() {
        return meanOfTransport;
    }

    public void setMeanOfTransport(MeanOfTransport meanOfTransport) {
        this.meanOfTransport = meanOfTransport;
    }

    public int getTemperature() {
        return temperature;
    }

    public WeatherStatus getWeatherStatus() {
        return weatherStatus;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(int trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public void setWeatherStatus(WeatherStatus weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddTripDataInstanceParcelable that = (AddTripDataInstanceParcelable) o;
        return activeScreenIndex == that.activeScreenIndex &&
                trafficLevel == that.trafficLevel &&
                effortLevel == that.effortLevel &&
                rushLevel == that.rushLevel &&
                satisfactionLevel == that.satisfactionLevel &&
                errorOnDeserializing == that.errorOnDeserializing &&
                temperature == that.temperature &&
                Objects.equals(startLocation, that.startLocation) &&
                Objects.equals(endLocation, that.endLocation) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                meanOfTransport == that.meanOfTransport &&
                weatherStatus == that.weatherStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeScreenIndex, startLocation, endLocation, startDate, endDate, meanOfTransport, trafficLevel, effortLevel, rushLevel, satisfactionLevel, errorOnDeserializing, temperature, weatherStatus);
    }

    @Override
    public String toString() {
        return "AddTripDataInstanceParcelable{" +
                "activeScreenIndex=" + activeScreenIndex +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", meanOfTransport=" + meanOfTransport +
                ", trafficLevel=" + trafficLevel +
                ", effortLevel=" + effortLevel +
                ", rushLevel=" + rushLevel +
                ", satisfactionLevel=" + satisfactionLevel +
                ", errorOnDeserializing=" + errorOnDeserializing +
                ", temperature=" + temperature +
                ", weatherStatus=" + weatherStatus +
                '}';
    }

    public boolean isErrorOnDeserializing() {
        return errorOnDeserializing;
    }
}
