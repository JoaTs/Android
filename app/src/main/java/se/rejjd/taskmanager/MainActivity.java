package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository workItemRepository;
    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        workItemRepository = new InMemoryRepository();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        WorkItem workItemDB = httpWorkItemRepository.getWorkItem(41);

//        Toast.makeText(this, "" + workItemDB.toString(),Toast.LENGTH_LONG).show();

//        WorkItem workItem = new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?");
//        int result = httpWorkItemRepository.addWorkItem(new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?"));

//        Toast.makeText(this, "" + result,Toast.LENGTH_LONG).show();

        Toast.makeText(this, httpWorkItemRepository.getWorkItems().toString(), Toast.LENGTH_LONG).show();


        updateAdapter();

    }

    private void updateAdapter() {
        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

