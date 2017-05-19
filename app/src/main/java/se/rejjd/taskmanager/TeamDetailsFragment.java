package se.rejjd.taskmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;

public final class TeamDetailsFragment extends Fragment {
    private static final String BUNDLE_TEAM_ID = "team_id";
    private TeamRepository teamRepository = SqlTeamRepository.getInstance(getContext());
    private Team team;

    public static Fragment newInstance(long id) {
        Fragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_TEAM_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Team team1 = new Team(10L, "hello", true);
        teamRepository.addTeam(team1);
        Log.d("test", "onCreate: " + teamRepository.getTeams());
        team = teamRepository.getTeam(String.valueOf(getArguments().getLong(BUNDLE_TEAM_ID)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_details_fragment, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_team_detail_title);
        tvTitle.setText(team.getTeamName());
        return view;
    }
}
