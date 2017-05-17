package se.rejjd.taskmanager.sql;

import android.graphics.PorterDuff;

import se.rejjd.taskmanager.sql.DatabaseContract.ModelEntry;

public class DatabaseQueries {

    public static final String CREATE_TABLES_USERS = "CREATE TABLE " + ModelEntry.USERS_TABLE_NAME + " (" + ModelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ModelEntry.USERS_COLUMN_NAME_USERNAME + " TEXT NOT NULL, " +
            ModelEntry.USERS_COLUMN_NAME_FIRSTNAME + " TEXT NOT NULL, " + ModelEntry.USERS_COLUMN_NAME_LASTNAME + " TEXT NOT NULL, " + ModelEntry.USERS_COLUMN_NAME_USER_ID + " TEXT NOT NULL " + ModelEntry.USERS_COLUMN_NAME_ACTIVE_USER + " BIT NOT NULL);";

    public static final String CREATE_TABLE_WORK_ITEMS = "CREATE TABLE " + ModelEntry.WORK_ITEMS_TABLE_NAME + " (" + ModelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ModelEntry.WORK_ITEMS_COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
            ModelEntry.WORK_ITEMS_COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL); ";

    public static final String CREATE_TABLE_ISSUES = "CREATE TABLE " + ModelEntry.ISSUES_TABLE_NAME + " (" + ModelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ModelEntry.ISSUES_COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
            ModelEntry.ISSUES_COLUMN_NAME_OPEN_ISSUE + " BIT NOT NULL);";

    public static final String CREATE_TABLE_TEAMS = "CREATE TABLE " + ModelEntry.TEAM_TABLE_NAME + " (" + ModelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ModelEntry.TEAM_COLUMN_NAME_TEAM_NAME + " TEXT NOT NULL, " +
            ModelEntry.TEAM_COLUMN_NAME_ACTIVE_TEAM + " BIT NOT NULL);";

    public static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + ModelEntry.USERS_TABLE_NAME;

    public static final String DROP_TABLE_WORK_ITEMS = "DROP TABLE IF EXISTS " + ModelEntry.WORK_ITEMS_TABLE_NAME;

    public static final String DROP_TABLE_ISSUES = "DROP TABLE IF EXISTS " + ModelEntry.ISSUES_TABLE_NAME;

    public static final String DROP_TABLE_TEAMS = "DROP TABLE IF EXISTS " + ModelEntry.TEAM_TABLE_NAME;
}
