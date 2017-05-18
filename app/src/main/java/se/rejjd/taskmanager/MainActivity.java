package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkItemRepository repository = SqlWorkItemRepository.getInstance(this);

        for (int i = 0; i <= 10; i++) {
            repository.addWorkItem(new WorkItem(i, "test","test"));
        }
        Log.d(TAG, "onCreate: TAG" + repository.getWorkItems().toString());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new WorkItemAdapter(repository.getWorkItems()));
        
//        WorkItem workItemDB = httpWorkItemRepository.getWorkItem(41);

//        Toast.makeText(this, "" + workItemDB.toString(),Toast.LENGTH_LONG).show();

//        WorkItem workItem = new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?");
//        int result = httpWorkItemRepository.addWorkItem(new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?"));

//        Toast.makeText(this, "" + result,Toast.LENGTH_LONG).show();

        Toast.makeText(this,"welvome", Toast.LENGTH_LONG).show();

        updateAdapter();

    }

    private void updateAdapter() {
//        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

