package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.SqlLoader;

public class MainActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;
    private Team team1;
    private SqlLoader sqlLoader;
    private String userLoggedIn;

    public static final String USER_ID = "userId";

    public static Intent createIntentMainActivity(Context context, String userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userLoggedIn = bundle.getString(USER_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment == null){
            fragment = WorkItemListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.workitem_list_container,fragment)
                    .commit();
        }
        updateAdapter();

        //TODO TEST TO UPDATE SQLite
//        if(sqlLoader == null) {
            new SqlLoader(this, userLoggedIn).updateSqlFromHttp();
//        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.team_view:
                Intent intent = DetailViewActivity.createIntentWithTeam(this,1L);
                startActivity(intent);
                break;
        }
        return true;

    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent =  DetailViewActivity.createIntentWithWorkItem(MainActivity.this,workItem);
        startActivity(intent);
    }

    public void onFabClicked(View view) {
        Intent intent = AddWorkitemActivity.getIntent(MainActivity.this);
        startActivity(intent);
    }

    public void updateAdapter() {
//        WorkItemRepository workItemRepository = new HttpWorkItemRepository();
//        recyclerView.setAdapter(workItemRepository.getWorkItems());
    }
}

