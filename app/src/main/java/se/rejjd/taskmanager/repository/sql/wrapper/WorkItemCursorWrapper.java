package se.rejjd.taskmanager.repository.sql.wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.widget.Switch;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.sql.DatabaseContract.ModelEntry;


public final class WorkItemCursorWrapper extends CursorWrapper{

    public WorkItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WorkItem getWorkItem() {

        long id = getLong(getColumnIndex(ModelEntry.WORK_ITEMS_COLUMN_NAME_ID));
        String title = getString(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_TITLE));
        String description = getString(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_DESCRIPTION));
        String status = getString(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_STATUS));
        long userId = getLong(getColumnIndexOrThrow(ModelEntry.WORK_ITEMS_COLUMN_NAME_USER_ID));


        WorkItem workItem = new WorkItem(id, title, description,userId);

        workItem.setStatus(status);

        return workItem;
    }
    public WorkItem getFirstWorkItem() {
        moveToFirst();
        return getWorkItem();
    }
}
