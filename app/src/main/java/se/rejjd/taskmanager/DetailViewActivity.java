package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import se.rejjd.taskmanager.model.WorkItem;

public class DetailViewActivity extends AppCompatActivity {
    private final static String EXTRA_WORKITEM_ID = "userid";

    public static Intent createIntent(Context context, WorkItem workItem){
        Intent intent = new Intent(context,DetailViewActivity.class);
        intent.putExtra(EXTRA_WORKITEM_ID,workItem);
        Log.d("hej", "createIntent: " +workItem.toString());
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_item_detail_view);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        WorkItem workItem = bundle.getParcelable(EXTRA_WORKITEM_ID);
        TextView tvTitle = (TextView) findViewById(R.id.tv_detail_view_title);
        TextView tvDescription = (TextView) findViewById(R.id.tv_detail_view_description);
        tvTitle.setText(workItem.getTitle());
        tvDescription.setText(workItem.getDescription());
    }
}
