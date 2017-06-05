package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.rejjd.taskmanager.fragment.TeamDetailsFragment;
import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpResponse;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

public final class AddWorkitemActivity extends AppCompatActivity {

    private HttpUserRepository userRepository;
    private HttpWorkItemRepository workItemRepository;
    private String userLoggedIn;

    public static Intent getIntent(Context context, String userId) {
        Intent intent = new Intent(context, AddWorkitemActivity.class);
        intent.putExtra(HomeScreenActivity.USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_item);

        userLoggedIn = getIntent().getExtras().getString(HomeScreenActivity.USER_ID);

        workItemRepository = new HttpWorkItemRepository();
        userRepository = new HttpUserRepository();

        final EditText title = (EditText) findViewById(R.id.edit_workitem_title);
        final EditText description = (EditText) findViewById(R.id.edit_workitem_description);
        Button button = (Button) findViewById(R.id.add_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workitemTitle = title.getText().toString();
                String workitemDescription = description.getText().toString();

                WorkItem workItem = new WorkItem(-1L, workitemTitle, workitemDescription, Long.valueOf(userLoggedIn));
                Log.d("johanAddWorkItem56","Kommer vi hit? " + workItem.toString());
                workItemRepository.addWorkItem(workItem, new GetTask.OnResultListener() {
                    @Override
                    public void onResult(HttpResponse httpResult) {
                        long newId = workItemRepository.getIdFromLocation(httpResult);//TODO This is wrong
                        userRepository.addUserToWorkItem(userLoggedIn, newId, new GetTask.OnResultListener() {
                            @Override
                            public void onResult(HttpResponse httpResult) {
                                if (httpResult.getStatusCode() == 200) {
                                    Log.d("johanAddWorkItem65","userAddedToTeam");
                                } else {
                                    Log.d("johanAddWorkItem65","userNotAddedTeam");
                                }
                            }
                        });
                    }
                });

                finish();
            }
        });
    }

}
