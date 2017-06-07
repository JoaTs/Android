package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import se.rejjd.taskmanager.fragment.TeamDetailsFragment;
import se.rejjd.taskmanager.fragment.WorkItemDetailFragment;
import se.rejjd.taskmanager.fragment.WorkItemUpdateFragment;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class DetailViewActivity extends AppCompatActivity {

    private static final String USER_ID = "userId";
    private static final String EXTRA_WORKITEM_ID = "detailWorkItem";
    private static final String TEAM_ID = "teamid";
    private static final String EXTRA_UPDATE_WORKITEM = "updateworkitem";
    private WorkItemRepository workItemRepository;

    public static Intent createIntentWithWorkItem(Context context, long workitemId) {
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(EXTRA_WORKITEM_ID, workitemId);
        return intent;
    }

    public static Intent createIntentWithTeam(Context context, long id, String userLoggedIn) {
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(TEAM_ID, id);
        intent.putExtra(USER_ID,userLoggedIn);
        return intent;
    }

    public static Intent createIntentForUpdate(Context context , long workItemId){
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(EXTRA_UPDATE_WORKITEM,workItemId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        workItemRepository = SqlWorkItemRepository.getInstance(this);

        String userLoggedIn = getIntent().getExtras().getString(USER_ID);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        WorkItem workItem = workItemRepository.getWorkItem(String.valueOf(bundle.getLong(EXTRA_WORKITEM_ID)));

        if (workItem != null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.details_container);
            if (fragment == null) {
                fragment = WorkItemDetailFragment.newInstance(workItem.getId());
                fm.beginTransaction()
                        .add(R.id.details_container, fragment)
                        .commit();
            }

        }
        WorkItem updateWorkitem = workItemRepository.getWorkItem(String.valueOf(bundle.getLong(EXTRA_UPDATE_WORKITEM)));
        if(updateWorkitem != null){
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.details_container);
            if(fragment == null){
                fragment = WorkItemUpdateFragment.getInstance(updateWorkitem.getId());
                fm.beginTransaction()
                        .add(R.id.details_container,fragment)
                        .commit();
            }
        }
        Long id = bundle.getLong(TEAM_ID);
        if(id != 0) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.details_container);
            if (fragment == null) {
                fragment = TeamDetailsFragment.newInstance(id,userLoggedIn);
                fm.beginTransaction()
                        .add(R.id.details_container, fragment)
                        .commit();
            }
        }
    }
}