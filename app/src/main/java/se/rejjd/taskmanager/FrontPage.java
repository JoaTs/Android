package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FrontPage extends AppCompatActivity{

    private final String userId = "2002";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.front_page);

        Button button = (Button) findViewById(R.id.sign_in_button);

        setStartupText();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.createIntentMainActivity(FrontPage.this, userId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setStartupText(){
        TextView company = (TextView) findViewById(R.id.tv_startup);
        String companyName = "Task"
                + "<font color=\"#FFA000\"><bold>"
                + "r"
                + "</bold></font>";

        company.setText(Html.fromHtml(companyName));
    }


}
