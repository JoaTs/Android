package se.rejjd.taskmanager.repository.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.sql.WorkItemDbHelper;
import se.rejjd.taskmanager.sql.DatabaseContract.ModelEntry;

public class SqlWorkItemRepository implements WorkItemRepository{

    private static SqlWorkItemRepository instance;

    public static synchronized SqlWorkItemRepository getInstance(Context context) {
        if(instance == null) {
            instance = new SqlWorkItemRepository(context);
        }

        return instance;
    }

    private final SQLiteDatabase database;

    private SqlWorkItemRepository(Context context) {
        database = WorkItemDbHelper.getInstance(context).getWritableDatabase();
    }

    @Override
    public List<WorkItem> getWorkItems() {
        WorkItemCursorWrapper cursor = queryWorkItems(null, null);

        List<WorkItem> workItems = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                workItems.add(cursor.getWorkItem());
            }
        }
        cursor.close();
        return workItems;
    }

    @Override
    public WorkItem getWorkItem(String id) {
        WorkItemCursorWrapper cursor = queryWorkItems(ModelEntry._ID + " = ?", new String[]{id});
        if(cursor.getCount() > 0){
            WorkItem workItem = cursor.getFirstWorkItem();
            cursor.close();
            return workItem;

        }
        cursor.close();
        return null;
    }

    @Override
    public Long addWorkItem(WorkItem workItem) {
        ContentValues cv = getContentValues(workItem);
        return database.insert(ModelEntry.WORK_ITEMS_TABLE_NAME, null, cv);
    }

    private ContentValues getContentValues(WorkItem workItem) {
        ContentValues cv = new ContentValues();
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_TITLE, workItem.getTitle());
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_DESCRIPTION, workItem.getDescription());

        return cv;
    }

    private WorkItemCursorWrapper queryWorkItems(String where, String[] whereArg) {
        @SuppressLint("Recycle")
        Cursor cursor = database.query(ModelEntry.WORK_ITEMS_TABLE_NAME,
                null,
                where,
                whereArg,
                null,
                null,
                null
        );
        return new WorkItemCursorWrapper(cursor);
    }
}
