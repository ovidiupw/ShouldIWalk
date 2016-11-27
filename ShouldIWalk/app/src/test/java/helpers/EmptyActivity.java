package helpers;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.widget.Toast;

import com.android.shouldiwalk.core.database.DatabaseHelper;
import com.android.shouldiwalk.utils.ErrorUtils;
import com.android.shouldiwalk.utils.ToastUtils;

import java.io.IOException;


public class EmptyActivity extends Activity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabaseForTest();
    }

    @VisibleForTesting
    private void initializeDatabaseForTest() {
        try {
            databaseHelper = new DatabaseHelper(
                    this,
                    DatabaseHelper.getDatabaseIdentifier(),
                    DatabaseHelper.getDatabaseVersion()
            );
            database = databaseHelper.getWritableDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
