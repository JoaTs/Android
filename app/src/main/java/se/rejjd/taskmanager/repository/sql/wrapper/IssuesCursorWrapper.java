package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.Issue;
import se.rejjd.taskmanager.sql.DatabaseContract;

public final class IssuesCursorWrapper extends CursorWrapper {

    public IssuesCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Issue getIssue() {

        long id = getLong(getColumnIndex(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_ID));
        String description = getString(getColumnIndexOrThrow(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_DESCRIPTION));

        return new Issue(id, description);
    }

    public Issue getFirstIssue() {
        moveToFirst();
        return getIssue();
    }
}