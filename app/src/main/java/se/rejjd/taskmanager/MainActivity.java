package se.rejjd.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.tv);
        new GetTask(new GetTask.OnResultListener() {
            @Override
            public void onResult(String string) {
                textView.setText(string);
            }
        }).execute();
    }
}
