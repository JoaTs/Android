package se.rejjd.taskmanager;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

public final class AddWorkitemActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private WorkItemRepository workItemRepository;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AddWorkitemActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_item);

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
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
//                String userId = bundle.getString(HomeScreenActivity.USER_ID);
                String userId = "2002";
                        WorkItem workItem = new WorkItem(-1L,workitemTitle,workitemDescription,Long.valueOf(userId));

                long newId =  workItemRepository.addWorkItem(workItem);
//                WorkItem workItemDb  = workItemRepository.getWorkItem(String.valueOf(workItem.getId()));
                userRepository.addUserToWorkItem(userId, newId);

                finish();
            }
        });
    }
}
