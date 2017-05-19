package se.rejjd.taskmanager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class MainActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        WorkItem workItemDB = httpWorkItemRepository.getWorkItem(41);

//        Toast.makeText(this, "" + workItemDB.toString(),Toast.LENGTH_LONG).show();

//        WorkItem workItem = new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?");
//        int result = httpWorkItemRepository.addWorkItem(new WorkItem(-1, "en tiitle", "vadskaviskrivahärdå?"));

//        Toast.makeText(this, "" + result,Toast.LENGTH_LONG).show();

        Toast.makeText(this,"welvome", Toast.LENGTH_LONG).show();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment == null){
            fragment = WorkItemListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.workitem_list_container,fragment)
                    .commit();
        }



        updateAdapter();

    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent =  DetailViewActivity.createIntent(MainActivity.this,workItem);
        startActivity(intent);
    }

    private void updateAdapter() {
//        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

