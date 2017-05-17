package se.rejjd.taskmanager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import se.rejjd.taskmanager.sql.DatabaseQueries;

public class WorkItemDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskmanager_database";
    private static final int DB_VERSION = 1;
    private static WorkItemDbHelper instance;

    public WorkItemDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized WorkItemDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WorkItemDbHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseQueries.CREATE_TABLE_WORK_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseQueries.DROP_TABLE_USERS);
        db.execSQL(DatabaseQueries.DROP_TABLE_WORK_ITEMS);
        db.execSQL(DatabaseQueries.DROP_TABLE_ISSUES);
        db.execSQL(DatabaseQueries.DROP_TABLE_TEAMS);
        onCreate(db);
    }
}
