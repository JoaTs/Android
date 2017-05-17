package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;

public interface WorkItemRepository {

        List<WorkItem> getWorkItems();

        WorkItem getWorkItem(int id);

        int addWorkItem(WorkItem workItem);

    }

