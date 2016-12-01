package helpers;

import android.database.sqlite.SQLiteDatabase;

import com.android.shouldiwalk.core.model.Location;
import com.android.shouldiwalk.core.model.MeanOfTransport;
import com.android.shouldiwalk.core.model.TripData;
import com.android.shouldiwalk.core.model.WeatherStatus;

public class TestHelper {
    public static void insertUserDataRecord(SQLiteDatabase database) {
        database.execSQL("insert into UserData (name, last_login_millis) values ('Dev', 1)");
    }

    public static TripData getSampleTripData(int id, Location startLocation, Location endLocation) {
        TripData tripData = new TripData();
        tripData.setId(id);
        tripData.setStartLocationId(startLocation.getId());
        tripData.setEndLocationId(endLocation.getId());
        tripData.setStartMillis(100);
        tripData.setEndMillis(200);
        tripData.setTemperatureCelsius(25);
        tripData.setSatisfactionLevel(3);
        tripData.setRushLevel(3);
        tripData.setTrafficLevel(3);
        tripData.setTripDifficulty(3);
        tripData.setMeanOfTransport(MeanOfTransport.Car);
        tripData.setWeatherStatus(WeatherStatus.Sunny);

        return tripData;
    }
}
