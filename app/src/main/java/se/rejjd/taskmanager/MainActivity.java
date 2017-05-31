package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.SqlLoader;

public class MainActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks, ChartFragment.CallBacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private SqlUserRepository sqlUserRepository;
    private RecyclerView recyclerView;
    private SqlLoader sqlLoader;
    private String userLoggedIn;
    private FragmentManager fm;


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
        fm = getSupportFragmentManager();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userLoggedIn = bundle.getString(USER_ID);

        sqlUserRepository = SqlUserRepository.getInstance(this);

        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment == null){
            fragment = WorkItemListFragment.newInstance();
            Fragment chartFragment = ChartFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.workitem_list_fragment,fragment)
                    .add(R.id.chart_fragment, chartFragment)
                    .commit();
        }
        updateAdapter();

        //TODO TEST TO UPDATE SQLite
//        if(sqlLoader == null) {
            new SqlLoader(this, userLoggedIn).updateSqlFromHttp();
//        }



    }

    //temp Solution
    @Override
    protected void onResume() {
        super.onResume();

        //TODO Update WorkItemAdapter
//        WorkItemListFragment.updateAdapter();

        new SqlLoader(this, userLoggedIn).updateSqlFromHttp();
        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment != null){
            Fragment listFragment = WorkItemListFragment.newInstance();
            Fragment chartFragment = ChartFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.workitem_list_fragment,listFragment)
                    .replace(R.id.chart_fragment, chartFragment)
                    .commit();

        }

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
                User user = sqlUserRepository.getUser(userLoggedIn);
                long teamId = user.getTeamId();
                Intent intent = DetailViewActivity.createIntentWithTeam(this,teamId);//TODO
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

    @Override
    public void onListItemLongClicked(WorkItem workItem) {
        Intent intent = DetailViewActivity.createIntentForUpdate(MainActivity.this, workItem);
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

    @Override
    public void onListItemClicked() {
        Toast.makeText(this, "Hello world!", Toast.LENGTH_LONG).show();
    }
}

