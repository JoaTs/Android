package se.rejjd.taskmanager.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.rejjd.taskmanager.model.WorkItem;

public class InMemoryRepository implements WorkItemRepository {

    private static final Map<String, WorkItem> workitems = new HashMap<>();

    static {
        for(int i = 0; i < 10; i++) {
            WorkItem workItem = new WorkItem("test","test","test");
            workitems.put(workItem.getId(), workItem);
        }
    }

    @Override
    public List<WorkItem> getWorkItems() {
        return new ArrayList<>(workitems.values());
    }

    @Override
    public WorkItem getWorkItem(String id) {
        return workitems.get(id);
    }

    @Override
    public String addWorkItem(WorkItem workItem) {
        workitems.put(workItem.getId(), workItem);
        return workItem.getId();
    }
}
