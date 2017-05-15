package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import se.rejjd.taskmanager.repository.InMemoryRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;

public class MainActivity extends AppCompatActivity {

    private WorkItemRepository workItemRepository;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            workItemRepository = new InMemoryRepository();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            
            updateAdapter();

    }

    private void updateAdapter() {
        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

