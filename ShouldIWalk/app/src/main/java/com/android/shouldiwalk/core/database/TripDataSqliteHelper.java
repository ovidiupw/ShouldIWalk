package com.android.shouldiwalk.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.shouldiwalk.core.DataValidator;
import com.android.shouldiwalk.core.annotations.SQLiteTable;
import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.Location;
import com.android.shouldiwalk.core.model.MeanOfTransport;
import com.android.shouldiwalk.core.model.TripData;
import com.android.shouldiwalk.core.model.WeatherStatus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.android.shouldiwalk.core.database.SqlQuery.Type.Create;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Display;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Update;
import static com.android.shouldiwalk.core.database.SqlQueryNames.GetTripDataItemCount;
import static com.android.shouldiwalk.core.database.SqlStatements.getAllQueriesFromCategory;

public class TripDataSqliteHelper implements TripDataDBHelper {

    private final Context context;
    private final SQLiteDatabase database;

    public TripDataSqliteHelper(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    @Override
    public void insert(TripData tripData) throws InvalidDataException, DatabaseCommFailure {
        DataValidator.validateTripData(tripData);
        final String CREATE_TRIP_DATA = SqlQueryNames.CreateTripData.getQueryName();

        try {
            Map<String, SqlQuery> queries = getAllQueriesFromCategory(Create, context);
            String createQuery = queries.get(CREATE_TRIP_DATA).getQueryString();

            database.execSQL(createQuery, new String[] {
                    String.valueOf(tripData.getStartLocationId()),
                    String.valueOf(tripData.getEndLocationId()),
                    String.valueOf(tripData.getStartMillis()),
                    String.valueOf(tripData.getEndMillis()),
                    String.valueOf(tripData.getWeatherStatus()),
                    String.valueOf(tripData.getTemperatureCelsius()),
                    String.valueOf(tripData.getTrafficLevel()),
                    String.valueOf(tripData.getTripDifficulty()),
                    String.valueOf(tripData.getRushLevel()),
                    String.valueOf(tripData.getMeanOfTransport()),
                    String.valueOf(tripData.getSatisfactionLevel()),
            });

        } catch (Throwable t) {
            throw new DatabaseCommFailure(t);
        }
    }

    @Override
    public void update(TripData tripData) throws InvalidDataException, DatabaseCommFailure {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<TripData> loadItems(QueryData queryData) throws DatabaseCommFailure {
        try (Cursor cursor = database.query(
                TripData.class.getAnnotation(SQLiteTable.class).name(),
                null, // all columns
                queryData.buildQueryString(),
                null, // no positional arguments
                null, // ignore group by
                null, // ignore having
                null)// ignore order by
        ) {
            List<TripData> dbTripsData = new ArrayList<>();

            int ID = cursor.getColumnIndex(TripData.getIdDBIdentifier());
            int START_LOCATION_ID = cursor.getColumnIndex(TripData.getStartLocationIdDBIdentifier());
            int END_LOCATION_ID = cursor.getColumnIndex(TripData.getEndLocationIdDBIdentifier());
            int START_MILLIS = cursor.getColumnIndex(TripData.getStartMillisDBIdentifier());
            int END_MILLIS = cursor.getColumnIndex(TripData.getEndMillisDBIdentifier());
            int WEATHER_STATUS = cursor.getColumnIndex(TripData.getWeatherStatusDBIdentifier());
            int TEMPERATURE_CELSIUS = cursor.getColumnIndex(TripData.getTemperatureCelsiusDBIdentifier());
            int TRAFFIC_LEVEL = cursor.getColumnIndex(TripData.getTrafficLevelDBIdentifier());
            int TRIP_DIFFICULTY = cursor.getColumnIndex(TripData.getTripDifficultyDBIdentifier());
            int RUSH_LEVEL = cursor.getColumnIndex(TripData.getRushLevelDBIdentifier());
            int MEAN_OF_TRANSPORT = cursor.getColumnIndex(TripData.getMeanOfTransportDBIdentifier());
            int SATISFACTION_LEVEL = cursor.getColumnIndex(TripData.getSatisfactionLevelDBIdentifier());

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    TripData tripData = new TripData();
                    tripData.setId(cursor.getInt(ID));
                    tripData.setStartLocationId(cursor.getInt(START_LOCATION_ID));
                    tripData.setEndLocationId(cursor.getInt(END_LOCATION_ID));
                    tripData.setStartMillis(cursor.getInt(START_MILLIS));
                    tripData.setEndMillis(cursor.getInt(END_MILLIS));
                    tripData.setWeatherStatus(WeatherStatus.valueOf(cursor.getString(WEATHER_STATUS)));
                    tripData.setTemperatureCelsius(cursor.getInt(TEMPERATURE_CELSIUS));
                    tripData.setTrafficLevel(cursor.getInt(TRAFFIC_LEVEL));
                    tripData.setTripDifficulty(cursor.getInt(TRIP_DIFFICULTY));
                    tripData.setRushLevel(cursor.getInt(RUSH_LEVEL));
                    tripData.setMeanOfTransport(MeanOfTransport.valueOf(cursor.getString(MEAN_OF_TRANSPORT)));
                    tripData.setSatisfactionLevel(cursor.getInt(SATISFACTION_LEVEL));

                    dbTripsData.add(tripData);
                    cursor.moveToNext();
                }
            }

            return dbTripsData;
        } catch (Throwable t) {
            throw new DatabaseCommFailure(t);
        }
    }

    @Override
    public int countRecords() {
        final String COUNT_QUERY_NAME = GetTripDataItemCount.getQueryName();
        Cursor cursor = null;

        try {
            Map<String, SqlQuery> updateQueries = getAllQueriesFromCategory(Display, context);
            String itemCountQuery = updateQueries.get(COUNT_QUERY_NAME).getQueryString();

            cursor = database.rawQuery(itemCountQuery, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            throw new RuntimeException("Should not have reached this. Count query failed.");

        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
