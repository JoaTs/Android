package se.rejjd.taskmanager.repository.sql;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.wrapper.WorkItemCursorWrapper;
import se.rejjd.taskmanager.sql.DatabaseHelper;
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
        database = DatabaseHelper.getInstance(context).getWritableDatabase();
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
        WorkItemCursorWrapper cursor = queryWorkItems(ModelEntry.WORK_ITEMS_COLUMN_NAME_ID + " = ?", new String[]{id});
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
//        TODO: IF PERSIST
        ContentValues cv = getContentValues(workItem);
        return database.insert(ModelEntry.WORK_ITEMS_TABLE_NAME, null, cv);
    }

    @Override
    public WorkItem updateWorkItem(WorkItem workItem) {
//        TODO: UPDATE
        return null;
    }

    @Override
    public List<WorkItem> getWorkItemByStatus(String status) {
        //TODO: getWorkItemByStatus
        return null;
    }

    @Override
    public List<WorkItem> getWorkItemsFromTeam(long teamId) {
        return null;
    }

    private ContentValues getContentValues(WorkItem workItem) {
        ContentValues cv = new ContentValues();
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_ID, workItem.getId());
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_TITLE, workItem.getTitle());
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_DESCRIPTION, workItem.getDescription());
        cv.put(ModelEntry.WORK_ITEMS_COLUMN_NAME_STATUS, workItem.getStatus());

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
