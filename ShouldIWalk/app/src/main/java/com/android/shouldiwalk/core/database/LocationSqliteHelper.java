package com.android.shouldiwalk.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.shouldiwalk.core.DataValidator;
import com.android.shouldiwalk.core.annotations.SQLiteTable;
import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;
import com.android.shouldiwalk.core.exceptions.InvalidDataException;
import com.android.shouldiwalk.core.model.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.android.shouldiwalk.core.database.SqlQuery.Type.Create;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Display;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Update;
import static com.android.shouldiwalk.core.database.SqlStatements.getAllQueriesFromCategory;

public class LocationSqliteHelper implements LocationDBHelper {

    private final Context context;
    private final SQLiteDatabase database;

    public LocationSqliteHelper(Context context, SQLiteDatabase database) {
        this.context = context;
        this.database = database;
    }

    @Override
    public void insert(Location location) {
        DataValidator.validateLocation(location);
        final String INSERT_QUERY_NAME = SqlQueryNames.CreateLocation.getQueryName();

        try {
            Map<String, SqlQuery> queries = getAllQueriesFromCategory(Create, context);
            String insertQuery = queries.get(INSERT_QUERY_NAME).getQueryString();

            database.execSQL(insertQuery, new String[] {
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude())
            });

        } catch (Throwable t) {
            throw new DatabaseCommFailure(t);
        }
    }

    @Override
    public void update(Location location) {
        DataValidator.validateLocationWithId(location);
        final String UPDATE_QUERY_NAME = SqlQueryNames.UpdateLocation.getQueryName();

        try {
            Map<String, SqlQuery> queries = getAllQueriesFromCategory(Update, context);
            String updateQuery = queries.get(UPDATE_QUERY_NAME).getQueryString();

            database.execSQL(updateQuery, new String[] {
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()),
                    String.valueOf(location.getId())
            });

        } catch (Throwable t) {
            throw new DatabaseCommFailure(t);
        }
    }


    @Override
    public List<Location> loadItems(QueryData queryData) {
        try (Cursor cursor = database.query(
                Location.class.getAnnotation(SQLiteTable.class).name(),
                null, // all columns
                queryData.buildQueryString(),
                null, // no positional arguments
                null, // ignore group by
                null, // ignore having
                null)// ignore order by
        ) {
            List<Location> dbLocations = new ArrayList<>();

            int ID = cursor.getColumnIndex(Location.getIdDBIdentifier());
            int LAT = cursor.getColumnIndex(Location.getLatitudeDBIdentifier());
            int LONG = cursor.getColumnIndex(Location.getLongitudeDBIdentifier());

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Location dbLocation = new Location(
                            cursor.getInt(ID),
                            cursor.getDouble(LAT),
                            cursor.getDouble(LONG));
                    dbLocations.add(dbLocation);
                    cursor.moveToNext();
                }
            }

            return dbLocations;
        } catch (Throwable t) {
            throw new DatabaseCommFailure(t);
        }
    }
}
