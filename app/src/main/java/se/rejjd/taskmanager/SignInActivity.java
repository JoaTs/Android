package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private final static int REQUEST_CODE_SIGN_IN = 1003;
    private final static String IS_LOGGED_IN = "loggedin";
    private String accountEmail;
    private GoogleApiClient googleApiClient;

    public static Intent getSignInIntent(Context context, boolean isLoggedout){
        Intent intent = new Intent(context,SignInActivity.class);
        intent.putExtra(IS_LOGGED_IN,isLoggedout);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_sign_in);

        SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                if(account != null){
                    accountEmail = account.getEmail();
                    Intent intent = HomeScreenActivity.createIntentMainActivity(SignInActivity.this, accountEmail);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}