package se.rejjd.taskmanager.repository.sql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.sql.DatabaseContract;
import se.rejjd.taskmanager.sql.WorkItemDbHelper;

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
        database = WorkItemDbHelper.getInstance(context).getWritableDatabase();
    }

    @Override
    public List<User> getUsers() {
        UserCursorWrapper cursor = queryUsers(null, null);

        return null;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public Long addUser(User user) {
        return null;
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
