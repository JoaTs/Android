package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.model.WorkItem;

public interface WorkItemRepository {

    List<WorkItem> getWorkItems(GetTask.OnResultListener listener);

    WorkItem getWorkItem(String id, GetTask.OnResultListener listener);

    Long addWorkItem(WorkItem workItem, GetTask.OnResultListener listener);

    WorkItem updateWorkItem(WorkItem workItem, GetTask.OnResultListener listener);

    List<WorkItem> getWorkItemByStatus(String status);

    List<WorkItem> getWorkItemsFromTeam(final long teamId, GetTask.OnResultListener listener);

    List<WorkItem> getWorkItemsByUser(String userId);
}

