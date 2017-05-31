package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.fragment.WorkItemListFragment;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.SqlLoader;


public class SearchActivity extends AppCompatActivity implements WorkItemListFragment.CallBacks {

    public static final int SEARCH_RESULT = 15;
    WorkItemListFragment workItemListFragment;
     List<WorkItem> workItemList = new ArrayList<>();
    EditText etSearchValue;
    WorkItemRepository sqlWorkItemRepository = SqlWorkItemRepository.getInstance(this);
    private String userLoggedIn;
    SqlLoader sqlLoader = new SqlLoader(this, userLoggedIn);

    public static Intent getIntent(Context context, String userLoggedIn) {

        Intent intent = new Intent(context, SearchActivity.class);

        intent.putExtra(HomeScreenActivity.USER_ID, userLoggedIn);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        userLoggedIn = bundle.getString(HomeScreenActivity.USER_ID);

        sqlLoader = new SqlLoader(this, userLoggedIn);

        workItemList = sqlWorkItemRepository.getWorkItems();

        etSearchValue = (EditText) findViewById(R.id.et_search_value);

        FragmentManager fm = getSupportFragmentManager();

        workItemListFragment= (WorkItemListFragment) WorkItemListFragment.newInstance();

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
                String value = etSearchValue.getText().toString();
                Toast.makeText(SearchActivity.this, "Searching" + value, Toast.LENGTH_SHORT).show();
                List<WorkItem> result = new ArrayList<>();
                for(WorkItem w:  workItemList){
                    if(w.getTitle().toLowerCase().contains(value.toLowerCase()) ||
                            w.getDescription().toLowerCase().contains(value.toLowerCase())){
                        result.add(w);
                    }
                }

                workItemListFragment.updateAdapter(result);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        sqlLoader.updateSqlFromHttp();
        workItemListFragment.updateAdapter(sqlWorkItemRepository.getWorkItems());
    }

    @Override
    public void onListItemClicked(WorkItem workItem) {
        Intent intent =  DetailViewActivity.createIntentWithWorkItem(SearchActivity.this,workItem);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClicked(WorkItem workItem) {
        Intent intent = DetailViewActivity.createIntentForUpdate(SearchActivity.this, workItem);
        startActivity(intent);
    }
}
