package helpers;

import android.database.sqlite.SQLiteDatabase;

public class TestHelper {
    public static void insertUserDataRecord(SQLiteDatabase database) {
        database.execSQL("insert into UserData (name, last_login_millis) values ('Dev', 1)");
    }
}
