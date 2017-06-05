package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.Issue;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.sql.DatabaseContract;

public final class IssuesCursorWrapper extends CursorWrapper {

    public IssuesCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Issue getIssue() {

        long id = getLong(getColumnIndex(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_ID));
        String description = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_DESCRIPTION));
        boolean openIssue = getInt(getColumnIndexOrThrow(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_OPEN_ISSUE)) > 0;

        return new Issue(id, description, openIssue);
    }

    public Issue getFirstIssue() {
        moveToFirst();
        return getIssue();
    }
}
