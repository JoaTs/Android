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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.fragment.ChartFragment;
import se.rejjd.taskmanager.fragment.WorkItemListFragment;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.AppStatus;
import se.rejjd.taskmanager.service.SqlLoader;

public class HomeScreenActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks, ChartFragment.CallBacks {
    private static final String USER_ID = "userId";
    private static final int REQUEST_CODE_UPDATE_WORKITEM = 101;
    private static final int REQUEST_CODE_ADD_WORKITEM = 100;
    private static final int REQUEST_CODE_SEARCH = 102;
    private SqlUserRepository sqlUserRepository;
    private String userLoggedIn;
    private FragmentManager fm;
    private ChartFragment chartFragment;
    private List<Fragment> fragments = new ArrayList<>();
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
        User user = sqlUserRepository.getUser(userLoggedIn);
        if (user != null) {
            userLoggedIn = getIntent().getExtras().getString(USER_ID);
            SqlLoader sqlLoader = new SqlLoader(this, userLoggedIn);
            sqlLoader.updateSqlFromHttp();
            fragments.add(WorkItemListFragment.newInstanceWithWorkItemStatus("UNSTARTED"));
            fragments.add(WorkItemListFragment.newInstanceWithWorkItemStatus("STARTED"));
            fragments.add(WorkItemListFragment.newInstanceWithWorkItemStatus("DONE"));
            fragments.add(WorkItemListFragment.newInstanceWithUserId(String.valueOf(user.getId())));
            Fragment fragment = fm.findFragmentById(R.id.workitem_list_container);

            if (fragment == null) {
                chartFragment = (ChartFragment) ChartFragment.newInstance(userLoggedIn);
                fm.beginTransaction()
                        .replace(R.id.chart_fragment, chartFragment)
                        .commit();
            }
        }

        viewPager = (ViewPager) findViewById(R.id.vp_workitem_list);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
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
                if (AppStatus.isOnline(HomeScreenActivity.this)) {
                    User user = sqlUserRepository.getUser(userLoggedIn);
                    WorkItemRepository workItemRepository = SqlWorkItemRepository.getInstance(HomeScreenActivity.this);
                    int workitemAmount = workItemRepository.getWorkItemsByUser(String.valueOf(user.getId())).size();
                    if(workitemAmount < 5){
                        Intent intent = AddWorkitemActivity.getIntent(HomeScreenActivity.this, userLoggedIn);
                        startActivityForResult(intent, REQUEST_CODE_ADD_WORKITEM);
                    }else{
                        runAlert("You cannot have more than 5 workitems");

                    }

                } else {
                    runAlert("Please connect to the internet");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
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

        switch (item.getItemId()) {
            case R.id.team_view:
                User user = sqlUserRepository.getUser(userLoggedIn);
                if (user != null) {
                    long teamId = user.getTeamId();
                    Intent intent = DetailViewActivity.createIntentWithTeam(this, teamId, userLoggedIn);
                    startActivity(intent);
                }
                break;
            case R.id.search:
                Intent intentSearch = SearchActivity.getIntent(this, userLoggedIn);
                startActivityForResult(intentSearch, REQUEST_CODE_SEARCH);
                break;
        }
        return true;
    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent = DetailViewActivity.createIntentWithWorkItem(HomeScreenActivity.this, workItem.getId());
        startActivity(intent);
    }

    @Override
    public void onListItemLongClicked(WorkItem workItem) {
        if (AppStatus.isOnline(HomeScreenActivity.this)) {
            Intent intent = DetailViewActivity.createIntentForUpdate(HomeScreenActivity.this, workItem.getId());
            startActivityForResult(intent, REQUEST_CODE_UPDATE_WORKITEM);
        } else {
            runAlert("Please connect to the internet");
        }
    }

    @Override
    public void onChartClicked(int position) {
        viewPager.setCurrentItem(position);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragments;

        ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void runAlert(String alertMessage) {
        AlertDialog.Builder alertWindow = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        alertWindow.setMessage(alertMessage);
        alertWindow.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertWindow.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_WORKITEM) {
            if (resultCode == RESULT_OK) {
                updateInformation();
            }
        }
        if (requestCode == REQUEST_CODE_UPDATE_WORKITEM) {
            if (resultCode == RESULT_OK) {
                updateInformation();
            }
        }
        if(requestCode == REQUEST_CODE_SEARCH){
            if (resultCode == RESULT_OK){
                updateInformation();
            }
        }
    }

    private void updateInformation() {
        chartFragment = (ChartFragment) ChartFragment.newInstance(userLoggedIn);
        fm.beginTransaction()
                .replace(R.id.chart_fragment, chartFragment)
                .commit();
        new SqlLoader(this, userLoggedIn).updateSqlFromHttp();
        viewPager.setAdapter(pagerAdapter);
    }
}