package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpResponse;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.http.HttpTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;

public class TeamUpdateActivity extends AppCompatActivity {

    private static final String EXTRA_TEAM_ID = "teamId";
    private HttpTeamRepository teamRepository;
    private TeamRepository sqlTeamRepository;

    public static Intent createUpdateTeamIntent(Context context, long teamId) {
        Intent intent = new Intent(context, TeamUpdateActivity.class);
        intent.putExtra(EXTRA_TEAM_ID, teamId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_update);

        final EditText edTitle = (EditText) findViewById(R.id.ed_team_update);

        sqlTeamRepository = SqlTeamRepository.getInstance(this);

        final Team teamFrDb = sqlTeamRepository.getTeam(String.valueOf(getIntent().getExtras().getLong(EXTRA_TEAM_ID)));
        edTitle.setText(teamFrDb.getTeamName());
        Log.d("johanTeamUpdateAc42", "kom vi hit?");
        Button updateButton = (Button) findViewById(R.id.team_update_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("johanTeamUpdateAc47", "Before Team Update " + teamFrDb.toString());
                teamFrDb.setTeamName(edTitle.getText().toString());
                teamRepository.updateTeam(teamFrDb, new GetTask.OnResultListener() {
                    @Override
                    public void onResult(HttpResponse result) {

                        Log.d("johanTeamUpdateAc52", "Team is now updated" + result.getStatusCode());
                    }
                });

                finish();
            }
        });

    }

}
