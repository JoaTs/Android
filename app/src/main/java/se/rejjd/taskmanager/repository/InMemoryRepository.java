package se.rejjd.taskmanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.rejjd.taskmanager.model.WorkItem;

public class InMemoryRepository implements WorkItemRepository {

    private static final Map<Long, WorkItem> workitems = new HashMap<>();

    @Override
   public List<WorkItem> getWorkItems() {
        return new ArrayList<>(workitems.values());
    }

    @Override
    public WorkItem getWorkItem(String id) {
        return workitems.get(id);
    }

    @Override
    public Long addWorkItem(WorkItem workItem) {
        workitems.put(workItem.get_ID(), workItem);
        return workItem.get_ID();
    }

    @Override
    public WorkItem updateWorkItem(WorkItem workItem) {
        return null;
    }
}
