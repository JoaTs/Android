package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.sql.DatabaseContract;

public final class UserCursorWrapper extends CursorWrapper{

    public User getUser() {

        long id = getLong(getColumnIndex(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_ID));
        String username = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USERNAME));
        String firstName = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_FIRSTNAME));
        String lastName = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_LASTNAME));
        String userId = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_USER_ID));
        long teamId = getLong(getColumnIndexOrThrow(DatabaseContract.ModelEntry.USERS_COLUMN_NAME_TEAM_ID));

        return new User(id, username, firstName, lastName, userId, teamId);
    }

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getFirstUser() {
        moveToFirst();
        return getUser();
    }
}
