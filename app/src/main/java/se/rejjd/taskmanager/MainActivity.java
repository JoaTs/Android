package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import se.rejjd.taskmanager.repository.InMemoryRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

public class MainActivity extends AppCompatActivity {
    private WorkItemRepository workItemRepository;
    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            workItemRepository = new InMemoryRepository();

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(this, httpWorkItemRepository.getWorkItems().toString(), Toast.LENGTH_LONG).show();


            updateAdapter();

    }

    private void updateAdapter() {
        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

