package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.http.HttpTeamRepository;

public class TeamUpdateActivity extends AppCompatActivity {

    private static final String EXTRA_TEAM_ID = "teamId";
    private TeamRepository teamRepository;
    private Team teamFrDb;

    public static Intent createUpdateTeamIntent(Context context, long teamId) {
        Intent intent = new Intent(context, TeamUpdateActivity.class);
        intent.putExtra(EXTRA_TEAM_ID, teamId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_update);

        teamRepository = new HttpTeamRepository();
        teamFrDb = teamRepository.getTeam(String.valueOf(getIntent().getExtras().getLong(EXTRA_TEAM_ID)));

        final EditText edTitle = (EditText) findViewById(R.id.ed_team_update);
        edTitle.setText(teamFrDb.getTeamName());

        Button updateButton = (Button) findViewById(R.id.team_update_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                teamFrDb.setTeamName(edTitle.getText().toString());
                teamRepository.updateTeam(teamFrDb);
                finish();
            }
        });

    }

}
