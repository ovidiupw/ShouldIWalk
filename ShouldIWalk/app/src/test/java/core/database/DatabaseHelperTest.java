package core.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.activities.MainActivity;
import com.android.shouldiwalk.core.annotations.SQLiteTable;
import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.core.model.UserData;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseHelperTest {

    private static final Logger LOG = Logger.getLogger(DatabaseHelperTest.class.getCanonicalName());
    private static final String DUMMY_DB = "DummyDbNotForActualUse";
    private static final String GET_TABLES = "select name from sqlite_master where type='table'";

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void when_constructorCalled_andDatabaseNotCreated_then_createsDatabase() throws IOException {
        DatabaseHelper sqLiteOpenHelper = new DatabaseHelper(mainActivity, DUMMY_DB, 1);
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();

        assertTrue(database.getPath().contains(DUMMY_DB));
        assertEquals(1, database.getVersion());

        List<String> dbTables = new ArrayList<>();
        try (Cursor cursor = database.rawQuery(GET_TABLES, null)) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    dbTables.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
        }
        assertThat(dbTables, hasItem(UserData.class.getAnnotation(SQLiteTable.class).name()));

        LOG.info("DB Tables: " + dbTables);
        LOG.info("DB Path: " + database.getPath());
    }

    public void tearDown() throws Exception {
        mainActivity.deleteDatabase(DUMMY_DB);
    }
}