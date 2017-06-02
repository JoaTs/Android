package se.rejjd.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.fragment.WorkItemListFragment;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.AppStatus;
import se.rejjd.taskmanager.service.SqlLoader;

public class SearchActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks {

    public static final int SEARCH_RESULT = 15;
    private WorkItemListFragment workItemListFragment;
    private List<WorkItem> workItemList = new ArrayList<>();
    private List<WorkItem> resultList = new ArrayList<>();
    private EditText etSearchValue;
    private SqlLoader sqlLoader;
    private final WorkItemRepository sqlWorkItemRepository = SqlWorkItemRepository.getInstance(this);

    public static Intent getIntent(Context context, String userLoggedIn) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(HomeScreenActivity.USER_ID, userLoggedIn);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String userLoggedIn = getIntent().getExtras().getString(HomeScreenActivity.USER_ID);

        sqlLoader = new SqlLoader(this, userLoggedIn);

        etSearchValue = (EditText) findViewById(R.id.et_search_value);

        FragmentManager fm = getSupportFragmentManager();

        workItemListFragment = (WorkItemListFragment) WorkItemListFragment.newInstance();

        Fragment fragment = fm.findFragmentById(R.id.workitem_list_search_container);

        if(fragment == null){
            fragment = workItemListFragment;
            fm.beginTransaction()
                    .add(R.id.workitem_list_search_container,fragment)
                    .commit();
        }

        Button searchButton = (Button) findViewById(R.id.search_btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchValue = etSearchValue.getText().toString();
                resultList.clear();
                resultList = getFilteredList(workItemList, searchValue);
                workItemListFragment.updateAdapter(resultList);
            }
        });
    }

    private List<WorkItem> getFilteredList(List<WorkItem> list, String searchValue){
        List<WorkItem> result = new ArrayList<>();
        for(WorkItem w:  list){
            if(w.getTitle().toLowerCase().contains(searchValue.toLowerCase()) ||
                    w.getDescription().toLowerCase().contains(searchValue.toLowerCase())){
                result.add(w);
            }
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqlLoader.updateSqlFromHttp();
        workItemList = sqlWorkItemRepository.getWorkItems();
        List<WorkItem> updatedResultList = new ArrayList<>();
        for(WorkItem w: resultList){
            updatedResultList.add(sqlWorkItemRepository.getWorkItem(String.valueOf(w.getId())));
        }
        workItemListFragment.updateAdapter(updatedResultList);
    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent =  DetailViewActivity.createIntentWithWorkItem(SearchActivity.this,workItem);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClicked(WorkItem workItem) {
        if(AppStatus.isOnline()) {
            Intent intent = DetailViewActivity.createIntentForUpdate(SearchActivity.this, workItem);
            startActivity(intent);
        }else{
            runAlert();
        }
    }

    private void runAlert() {
        AlertDialog.Builder alertWindow = new AlertDialog.Builder(this);
        alertWindow.setMessage("Please connect to the internet");
        alertWindow.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertWindow.show();
    }
}