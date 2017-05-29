package se.rejjd.taskmanager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

public final class AddWorkitemActivity extends AppCompatActivity {

    public static final String MESSAGE_BACK = "message back";
    private static final String WORKITEM_ADDED = "Workitem added";
    private WorkItemRepository repository;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, AddWorkitemActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_item_add_view);

        repository = new HttpWorkItemRepository();

        final EditText title = (EditText) findViewById(R.id.edit_workitem_title);
        final EditText description = (EditText) findViewById(R.id.edit_workitem_description);
        Button button = (Button) findViewById(R.id.add_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workitemTitle = title.getText().toString();
                String workitemDescription = description.getText().toString();

                repository.addWorkItem(new WorkItem(-1, workitemTitle, workitemDescription));

                Intent intent = new Intent();
                intent.putExtra(MESSAGE_BACK, WORKITEM_ADDED);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
