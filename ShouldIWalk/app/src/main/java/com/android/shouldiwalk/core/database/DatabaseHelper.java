package com.android.shouldiwalk.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.exceptions.BadXMLFormatException;
import com.android.shouldiwalk.utils.ErrorUtils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import static com.android.shouldiwalk.core.database.SqlQuery.Type.Create;
import static com.android.shouldiwalk.core.database.SqlQuery.Type.Remove;
import static com.android.shouldiwalk.core.database.SqlQueryNames.CreateLocationsTable;
import static com.android.shouldiwalk.core.database.SqlQueryNames.CreateLocationsTableCoordinatesIndex;
import static com.android.shouldiwalk.core.database.SqlQueryNames.CreateTripDataTable;
import static com.android.shouldiwalk.core.database.SqlQueryNames.CreateUserDataTable;
import static com.android.shouldiwalk.core.database.SqlQueryNames.DeleteUserDataTable;
import static com.android.shouldiwalk.core.database.SqlStatements.getAllQueriesFromCategory;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CLASS_TAG = DatabaseHelper.class.getCanonicalName() + "-TAG";
    private static final String DATABASE_IDENTIFIER = "ShouldIWalkDatabase";
    private static final int DATABASE_VERSION = 3;
    private final Context context;

    public DatabaseHelper(Context context, String databaseName, int version) throws IOException {
        super(context, databaseName, null, version);
        this.context = context;
    }

    public static String getDatabaseIdentifier() {
        return DATABASE_IDENTIFIER;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            InputStream queriesStream = getDatabaseQueriesStream(this.context);
            Map<String, SqlQuery> createQueries = getAllQueriesFromCategory(Create, queriesStream);

            db.execSQL(createQueries.get(CreateUserDataTable.getQueryName())
                    .getQueryString());
            db.execSQL(createQueries.get(CreateLocationsTable.getQueryName())
                    .getQueryString());
            db.execSQL(createQueries.get(CreateTripDataTable.getQueryName())
                    .getQueryString());
            db.execSQL(createQueries.get(CreateLocationsTableCoordinatesIndex.getQueryName())
                    .getQueryString());

            Log.i(CLASS_TAG, "Executed database initialization code.");
        } catch (ParserConfigurationException | BadXMLFormatException | SAXException | IOException e) {
            ErrorUtils.logException(CLASS_TAG, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            InputStream queriesStream = getDatabaseQueriesStream(this.context);
            Map<String, SqlQuery> removeQueries = getAllQueriesFromCategory(Remove, queriesStream);
            db.execSQL(removeQueries.get(DeleteUserDataTable.getQueryName()).getQueryString());

            Log.i(CLASS_TAG, "Executed database upgrade code.");
            onCreate(db);
        } catch (ParserConfigurationException | BadXMLFormatException | SAXException | IOException e) {
            ErrorUtils.logException(CLASS_TAG, e);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static InputStream getDatabaseQueriesStream(Context context) throws IOException {
        return context.getResources().getAssets().open(context.getString(R.string.dbQueriesResource));
    }
}
