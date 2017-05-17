package se.rejjd.taskmanager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "taskmanager_database";
    private static final int DB_VERSION = 1;
    private static UserDbHelper instance;

    public UserDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized UserDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseQueries.CREATE_TABLES_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseQueries.DROP_TABLE_USERS);
        onCreate(db);
    }
}
