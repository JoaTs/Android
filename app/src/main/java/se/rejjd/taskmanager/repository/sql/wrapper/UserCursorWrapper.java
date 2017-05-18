package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.sql.DatabaseContract;

public class UserCursorWrapper extends CursorWrapper{

    public User getUser() {

        long id = getLong(getColumnIndex(DatabaseContract.ModelEntry._ID));
        String username = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USERNAME));
        String firstname = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_FIRSTNAME));
        String lastname = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_LASTNAME));
        String userId = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USER_ID));
        boolean activeUser = getInt(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_ACTIVE_USER)) > 0;

        return new User(id, username, firstname, lastname, userId, activeUser);
    }

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getFirstUser() {
        moveToFirst();
        return getUser();
    }
}
