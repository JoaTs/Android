package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;

public interface WorkItemRepository {

    List<WorkItem> getWorkItems();

    WorkItem getWorkItem(String id);

    Long addWorkItem(WorkItem workItem);

    WorkItem updateWorkItem(WorkItem workItem);

    List<WorkItem> getWorkItemByStatus(String status);

    List<WorkItem> getWorkItemsFromTeam(final long teamId);

    List<WorkItem> getWorkItemsByUser(String userId);
}