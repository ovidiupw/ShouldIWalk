package com.android.shouldiwalk.core.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.shouldiwalk.core.annotations.SQLiteTable;
import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.core.database.SqlQuery;
import com.android.shouldiwalk.core.exceptions.DatabaseCommFailure;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static com.android.shouldiwalk.core.database.SqlQuery.Type.Create;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Display;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Update;
import static com.android.shouldiwalk.core.database.SqlQueryNames.CreateUserData;
import static com.android.shouldiwalk.core.database.SqlQueryNames.FetchUserData;
import static com.android.shouldiwalk.core.database.SqlQueryNames.UpdateUserData;
import static com.android.shouldiwalk.core.database.SqlStatements.getAllQueriesFromCategory;

@SQLiteTable(name = "UserData")
public class UserData {
    private int id;
    private String name;
    private long lastLoginMillis;
    private int firstTimeInfoDismissed;

    public static UserData loadFromDatabase(Context context, SQLiteDatabase database) {
        Map<String, SqlQuery> displayQueries;
        try {
            InputStream queriesStream = DatabaseHelper.getDatabaseQueriesStream(context);
            displayQueries = getAllQueriesFromCategory(Display, queriesStream);
        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        }

        UserData userData = new UserData();
        boolean initialized = false;

        String fetchUserDataQuery = displayQueries.get(FetchUserData.getQueryName()).getQueryString();
        try (Cursor cursor = database.rawQuery(fetchUserDataQuery, null)) {
            if (cursor.moveToFirst()) {
                userData.setId(
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
                userData.setName(
                        cursor.getString(cursor.getColumnIndex("name")));
                userData.setLastLoginMillis(
                        Long.valueOf(cursor.getString(cursor.getColumnIndex("last_login_millis"))));
                userData.setFirstTimeInfoDismissed(
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex("first_time_info_dismissed"))));
                initialized = true;
            }
        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        }

        if (!initialized) {
            return null;
        } else {
            return userData;
        }
    }

    public void updateDatabaseItem(Context context, SQLiteDatabase database) throws DatabaseCommFailure {
        try {
            InputStream queriesStream = DatabaseHelper.getDatabaseQueriesStream(context);
            Map<String, SqlQuery> updateQueries = getAllQueriesFromCategory(Update, queriesStream);

            String updateUserDataQuery = updateQueries.get(UpdateUserData.getQueryName()).getQueryString();
            database.execSQL(updateUserDataQuery, new String[]{
                    this.name,
                    String.valueOf(this.lastLoginMillis),
                    String.valueOf(this.firstTimeInfoDismissed),
                    String.valueOf(this.id)
            });
        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        }
    }

    public void insertInDatabase(Context context, SQLiteDatabase database) throws DatabaseCommFailure {
        try {
            InputStream queriesStream = DatabaseHelper.getDatabaseQueriesStream(context);
            Map<String, SqlQuery> createQueries = getAllQueriesFromCategory(Create, queriesStream);

            String createUserDataQuery = createQueries.get(CreateUserData.getQueryName()).getQueryString();
            database.execSQL(createUserDataQuery, new String[]{
                    this.name,
                    String.valueOf(this.lastLoginMillis),
            });
        } catch (Throwable e) {
            throw new DatabaseCommFailure(e);
        }
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastLoginMillis=" + lastLoginMillis +
                ", firstTimeInfoDismissed=" + firstTimeInfoDismissed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return id == userData.id &&
                lastLoginMillis == userData.lastLoginMillis &&
                firstTimeInfoDismissed == userData.firstTimeInfoDismissed &&
                Objects.equals(name, userData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastLoginMillis, firstTimeInfoDismissed);
    }

    public int getFirstTimeInfoDismissed() {

        return firstTimeInfoDismissed;
    }

    public void setFirstTimeInfoDismissed(int firstTimeInfoDismissed) {
        this.firstTimeInfoDismissed = firstTimeInfoDismissed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastLoginMillis() {
        return lastLoginMillis;
    }

    public void setLastLoginMillis(long lastLoginMillis) {
        this.lastLoginMillis = lastLoginMillis;
    }
}
