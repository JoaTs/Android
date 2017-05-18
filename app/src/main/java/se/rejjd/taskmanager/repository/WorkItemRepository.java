package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;

public interface WorkItemRepository {

        List<WorkItem> getWorkItems();

        WorkItem getWorkItem(String id);

        Long addWorkItem(WorkItem workItem);

        boolean updateWorkItem(WorkItem workItem);
    }

