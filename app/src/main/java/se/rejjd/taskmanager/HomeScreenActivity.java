package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import se.rejjd.taskmanager.fragment.ChartFragment;
import se.rejjd.taskmanager.fragment.WorkItemListFragment;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.SqlLoader;

public class HomeScreenActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks, ChartFragment.CallBacks {
    private static final String TAG = HomeScreenActivity.class.getSimpleName();

    private SqlWorkItemRepository sqlWorkItemRepository;
    private WorkItemListFragment workItemListFragment;
    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private SqlUserRepository sqlUserRepository;
    private RecyclerView recyclerView;
    private SqlLoader sqlLoader;
    private String userLoggedIn;
    private FragmentManager fm;


    public static final String USER_ID = "userId";

    public static Intent createIntentMainActivity(Context context, String userId) {
        Intent intent = new Intent(context, HomeScreenActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        fm = getSupportFragmentManager();
        sqlUserRepository = SqlUserRepository.getInstance(this);
        sqlWorkItemRepository = SqlWorkItemRepository.getInstance(this);
        userLoggedIn = getIntent().getExtras().getString(USER_ID);
        sqlLoader = new SqlLoader(this, userLoggedIn);
        sqlLoader.updateSqlFromHttp();

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddWorkitemActivity.getIntent(HomeScreenActivity.this, userLoggedIn);
                startActivity(intent);
            }
        });

        workItemListFragment = (WorkItemListFragment) WorkItemListFragment.newInstance();
        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment == null){
            fragment = workItemListFragment;
            Fragment chartFragment = ChartFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.workitem_list_fragment,fragment)
                    .add(R.id.chart_fragment, chartFragment)
                    .commit();
        }

    }

    //temp Solution
    @Override
    protected void onResume() {
        super.onResume();
        sqlLoader.updateSqlFromHttp();
        workItemListFragment.updateAdapter(sqlWorkItemRepository.getWorkItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        inflater.inflate(R.menu.search_menu, menu);
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
            case R.id.search:
                Intent intentSearch = SearchActivity.getIntent(this, userLoggedIn);
                startActivityForResult(intentSearch, SearchActivity.SEARCH_RESULT);
            break;
        }
        return true;
    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent =  DetailViewActivity.createIntentWithWorkItem(HomeScreenActivity.this,workItem);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClicked(WorkItem workItem) {
        Intent intent = DetailViewActivity.createIntentForUpdate(HomeScreenActivity.this, workItem);
        startActivity(intent);
    }

    @Override
    public void onListItemClicked() {
        Toast.makeText(this, "Hello world!", Toast.LENGTH_LONG).show();
    }
}

