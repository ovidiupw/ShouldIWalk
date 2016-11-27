package com.android.shouldiwalk.core.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;

import java.io.InputStream;
import java.util.Map;

import static com.android.shouldiwalk.core.database.SqlQuery.Type.Display;
import static com.android.shouldiwalk.core.database.SqlQueryNames.GetTripDataItemCount;
import static com.android.shouldiwalk.core.database.SqlStatements.getAllQueriesFromCategory;

public class TripDataDBHelper {

    public static int getNumberOfRecords(Context context, SQLiteDatabase database) {
        try {
            InputStream queriesStream = DatabaseHelper.getDatabaseQueriesStream(context);
            Map<String, SqlQuery> updateQueries = getAllQueriesFromCategory(Display, queriesStream);

            String itemCountQuery
                    = updateQueries.get(GetTripDataItemCount.getQueryName()).getQueryString();
            try (Cursor cursor = database.rawQuery(itemCountQuery, null)) {
                if (cursor.moveToFirst()) {
                    return cursor.getInt(0);
                }
            }
            throw new RuntimeException("Should not have reached this. Count query failed.");
        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        }
    }
}
