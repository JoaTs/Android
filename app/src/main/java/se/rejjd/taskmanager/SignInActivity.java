package se.rejjd.taskmanager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity{

    private final static String USER_ID = "2002";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
        bar.hide(); }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_sign_in);

        Button button = (Button) findViewById(R.id.sign_in_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreenActivity.createIntentMainActivity(SignInActivity.this, USER_ID);
                startActivity(intent);
                finish();
            }
        });
    }
}