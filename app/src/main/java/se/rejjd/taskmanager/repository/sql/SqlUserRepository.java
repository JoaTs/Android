package se.rejjd.taskmanager.repository.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.sql.wrapper.UserCursorWrapper;
import se.rejjd.taskmanager.sql.DatabaseContract;
import se.rejjd.taskmanager.sql.DatabaseHelper;

public class SqlUserRepository implements UserRepository {

    private static SqlUserRepository instance;

    public static synchronized SqlUserRepository getInstance(Context context) {
        if(instance == null) {
            instance = new SqlUserRepository(context);
        }

        return instance;
    }

    private final SQLiteDatabase database;

    private SqlUserRepository(Context context) {
        database = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    @Override
    public List<User> getUsers() {
        UserCursorWrapper cursor = queryUsers(null, null);

        List<User> users = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                users.add(cursor.getUser());
            }
        }
        cursor.close();
        return users;
    }

    @Override
    public User getUser(String id) {
        UserCursorWrapper cursor = queryUsers(DatabaseContract.ModelEntry._ID + " = ?", new String[]{id});
        if(cursor.getCount() > 0){
            User user = cursor.getFirstUser();
            cursor.close();
            return user;

        }
        cursor.close();
        return null;
    }

    @Override
    public Long addUser(User user) {
//        TODO: IF PERSIST
        ContentValues cv = getContentValues(user);
        return database.insert(DatabaseContract.ModelEntry.USERS_TABLE_NAME, null, cv);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean addUserToWorkItem(String userId, long workItemId) {

        return false;
    }

    private ContentValues getContentValues(User user) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_ID, user.getId());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USERNAME, user.getUsername());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_FIRSTNAME, user.getFirstname());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_LASTNAME, user.getLastname());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USER_ID, user.getUserId());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_ACTIVE_USER, user.isActiveUser());
        cv.put(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_TEAM_ID,user.getTeamId());

        return cv;
    }

    private UserCursorWrapper queryUsers(String where, String[] whereArg) {
        @SuppressLint("Recycle")
        Cursor cursor = database.query(DatabaseContract.ModelEntry.USERS_TABLE_NAME,
                null,
                where,
                whereArg,
                null,
                null,
                null
        );
        return new UserCursorWrapper(cursor);
    }
}
