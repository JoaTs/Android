package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository workItemRepository;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workItemRepository = SqlWorkItemRepository.getInstance(this);


            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d(TAG, "onCreate: " + workItemRepository.getWorkItem("3"));
            recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
            updateAdapter();

    }

    private void updateAdapter() {
        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

