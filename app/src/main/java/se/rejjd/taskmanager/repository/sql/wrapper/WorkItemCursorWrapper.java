package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.sql.DatabaseContract.ModelEntry;


public class WorkItemCursorWrapper extends CursorWrapper{

    public WorkItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WorkItem getWorkItem() {

        long id = getLong(getColumnIndex(ModelEntry._ID));
        String title = getString(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_TITLE));
        String description = getString(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_DESCRIPTION));
        long userId = getLong(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_USER_ID));

        return new WorkItem(id, title, description,userId);
    }
    public WorkItem getFirstWorkItem() {
        moveToFirst();
        return getWorkItem();
    }
}
