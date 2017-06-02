package se.rejjd.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.fragment.ChartFragment;
import se.rejjd.taskmanager.fragment.WorkItemListFragment;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.AppStatus;
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
    private User user;
    private ChartFragment chartFragment;


    public static final String USER_ID = "userId";
    private static final int NUM_PAGES = 4;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userLoggedIn = bundle.getString(USER_ID);

        sqlUserRepository = SqlUserRepository.getInstance(this);
        user = sqlUserRepository.getUser(userLoggedIn);
        Log.d(TAG, "onCreate: " + user);
        sqlWorkItemRepository = SqlWorkItemRepository.getInstance(this);
        userLoggedIn = getIntent().getExtras().getString(USER_ID);
        sqlLoader = new SqlLoader(this, userLoggedIn);
        sqlLoader.updateSqlFromHttp();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WorkItemListFragment.newInstance("UNSTARTED"));
        fragments.add(WorkItemListFragment.newInstance("STARTED"));
        fragments.add(WorkItemListFragment.newInstance("DONE"));
        fragments.add(WorkItemListFragment.newInstanceWithUserId(String.valueOf(user.getId())));
        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

        if(fragment == null){
//            fragment = workItemListFragment;
            chartFragment = (ChartFragment) ChartFragment.newInstance();
            fm.beginTransaction()
//                    .add(R.id.workitem_list_fragment,fragment)
                    .add(R.id.chart_fragment, chartFragment)
                    .commit();
        }

        viewPager = (ViewPager) findViewById(R.id.vp_workitem_list);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                chartFragment.setIsActiveCharts(position);
                chartFragment.activeChart();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppStatus.isOnline()) {
                    Intent intent = AddWorkitemActivity.getIntent(HomeScreenActivity.this, userLoggedIn);
                    startActivity(intent);
                }else{
                    runAlert();
                }
            }
        });

        workItemListFragment = (WorkItemListFragment) WorkItemListFragment.newInstance("UNSTARTED");
//        Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

//        if(fragment == null){
//            fragment = workItemListFragment;
//            chartFragment = (ChartFragment) ChartFragment.newInstance();
//            fm.beginTransaction()
//                    .add(R.id.workitem_list_fragment,fragment)
//                    .add(R.id.chart_fragment, chartFragment)
//                    .commit();
//        }

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
        super.onBackPressed();
    } else {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
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
//            Fragment listFragment = WorkItemListFragment.newInstance("STARTED");
            chartFragment = (ChartFragment) ChartFragment.newInstance();
            fm.beginTransaction()
//                    .replace(R.id.workitem_list_fragment,listFragment)
                    .replace(R.id.chart_fragment, chartFragment)
                    .commit();

        }
        sqlLoader.updateSqlFromHttp();
//        workItemListFragment.updateAdapter(sqlWorkItemRepository.getWorkItems());
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
                Intent intent = DetailViewActivity.createIntentWithTeam(this,teamId, userLoggedIn);//TODO
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
        if(AppStatus.isOnline()) {
            Intent intent = DetailViewActivity.createIntentForUpdate(HomeScreenActivity.this, workItem);
            startActivity(intent);
        }else{
            runAlert();
        }
    }

    @Override
    public void onListItemClicked(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onPageChange(int position) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm , List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
            Log.d(TAG, "getItemPosition: " + object);
            return super.getItemPosition(object);

        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: " + position);
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void runAlert() {
        AlertDialog.Builder alertWindow = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        alertWindow.setMessage("Please connect to the internet");
        alertWindow.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertWindow.show();
    }
}

