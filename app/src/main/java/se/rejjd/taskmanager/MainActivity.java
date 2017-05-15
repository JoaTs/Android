package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpHelper;

public class MainActivity extends AppCompatActivity {
private String TAG = "hello";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.tv);

        new GetTask(new GetTask.OnResultListener() {
            @Override
            public void onResult(List<String> string) {
                textView.setText(string.toString());
                Log.d(TAG, "onResult: " + string.toString());
            }
        }).execute();
    }
}
