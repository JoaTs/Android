package se.rejjd.taskmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.Toast;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.AppStatus;
import se.rejjd.taskmanager.service.SqlLoader;

public class MainActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
    private RecyclerView recyclerView;
    private Team team1;
    private SqlLoader sqlLoader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,"welcome", Toast.LENGTH_LONG).show();

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        if(AppStatus.getInstance(this).isOnline()){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AddWorkitemActivity.getIntent(MainActivity.this);
                    startActivity(intent);
                }
            });

        }else{
            button.setClickable(false);
        }


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
            new SqlLoader(this, 10001L).updateSqlFromHttp();
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

    private void updateAdapter() {
//        recyclerView.setAdapter(new WorkItemAdapter(workItemRepository.getWorkItems()));
    }
}

