package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.WorkItem;

public class DetailViewActivity extends AppCompatActivity {
    private final static String EXTRA_WORKITEM_ID = "detailWorkItem";
    private static final String TEAM_ID = "teamid";
    private static final String EXTRA_UPDATE_WORKITEM = "updateworkitem";

    public static Intent createIntentWithWorkItem(Context context, WorkItem workItem) {
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(EXTRA_WORKITEM_ID, workItem);
        return intent;
    }

    public static Intent createIntentWithTeam(Context context, long id) {
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(TEAM_ID, id);
        return intent;
    }

    public static Intent createIntentForUpdate(Context context , WorkItem workItem){
        Intent intent = new Intent(context, DetailViewActivity.class);
        intent.putExtra(EXTRA_UPDATE_WORKITEM,workItem);
        return intent;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_item_detail_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        WorkItem workItem = bundle.getParcelable(EXTRA_WORKITEM_ID);
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
        WorkItem updateWorkitem = bundle.getParcelable(EXTRA_UPDATE_WORKITEM);
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
                fragment = TeamDetailsFragment.newInstance(id);
                fm.beginTransaction()
                        .add(R.id.details_container, fragment)
                        .commit();
            }
        }
    }
}
