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

public final class TeamDetailsFragment extends Fragment{
    private static final String BUNDLE_TEAM_ID = "team_id";
    private Team team;

    public static Fragment newInstance(long id){
        Fragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_TEAM_ID , id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TeamRepository teamRepository = SqlTeamRepository.getInstance(getContext());
        long id = getArguments().getLong(BUNDLE_TEAM_ID);
        Team team = new Team(10L, "hej",true);
        teamRepository.addTeam(team);
        Log.d("hej", "onCreate: " + teamRepository.getTeams().toString());
        team = teamRepository.getTeam(String.valueOf(id));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_details_fragment,container,false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_team_detail_title);
        tvTitle.setText(team.getTeamName());
        return view;
    }
}
