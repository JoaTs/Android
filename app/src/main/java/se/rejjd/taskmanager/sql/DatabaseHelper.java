package se.rejjd.taskmanager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskmanager_database";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseQueries.CREATE_TABLE_WORK_ITEMS + DatabaseQueries.CREATE_TABLES_USERS +
                DatabaseQueries.CREATE_TABLE_TEAMS + DatabaseQueries.CREATE_TABLE_ISSUES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseQueries.DROP_TABLE_TEAMS + DatabaseQueries.DROP_TABLE_USERS +
                DatabaseQueries.DROP_TABLE_WORK_ITEMS + DatabaseQueries.DROP_TABLE_ISSUES);
        onCreate(db);
    }
}
