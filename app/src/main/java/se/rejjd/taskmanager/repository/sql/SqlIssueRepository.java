package se.rejjd.taskmanager.repository.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.model.Issue;
import se.rejjd.taskmanager.repository.IssueRepository;
import se.rejjd.taskmanager.repository.sql.wrapper.IssuesCursorWrapper;
import se.rejjd.taskmanager.sql.DatabaseContract;
import se.rejjd.taskmanager.sql.DatabaseHelper;

public class SqlIssueRepository implements IssueRepository {

    private static SqlIssueRepository instance;

    public static synchronized SqlIssueRepository getInstance(Context context) {
        if(instance == null) {
            instance = new SqlIssueRepository(context);
        }

        return instance;
    }

    private final SQLiteDatabase database;

    private SqlIssueRepository(Context context) {
        database = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    @Override
    public List<Issue> getIssues() {
        IssuesCursorWrapper cursor = queryIssues(null, null);

        List<Issue> issues = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                issues.add(cursor.getIssue());
            }
        }
        cursor.close();
        return issues;
    }

    @Override
    public Issue getIssue(String id) {
        IssuesCursorWrapper cursor = queryIssues(DatabaseContract.ModelEntry._ID + " = ?", new String[]{id});
        if(cursor.getCount() > 0){
            Issue issue = cursor.getFirstIssue();
            cursor.close();
            return issue;

        }
        cursor.close();
        return null;
    }

    @Override
    public Long addIssue(Issue issue) {
        //        TODO: IF PERSIST
        ContentValues cv = getContentValues(issue);
        return database.insert(DatabaseContract.ModelEntry.ISSUES_TABLE_NAME, null, cv);
    }

    private ContentValues getContentValues(Issue issue) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_DESCRIPTION, issue.getDescription());
        cv.put(DatabaseContract.ModelEntry.ISSUES_COLUMN_NAME_OPEN_ISSUE, issue.isOpenIssue());

        return cv;
    }

    private IssuesCursorWrapper queryIssues(String where, String[] whereArg) {
        @SuppressLint("Recycle")
        Cursor cursor = database.query(DatabaseContract.ModelEntry.ISSUES_TABLE_NAME,
                null,
                where,
                whereArg,
                null,
                null,
                null
        );
        return new IssuesCursorWrapper(cursor);
    }
}
