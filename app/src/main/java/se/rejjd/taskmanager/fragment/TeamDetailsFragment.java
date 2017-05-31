package se.rejjd.taskmanager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.adapter.UserAdapter;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;

public final class TeamDetailsFragment extends Fragment {
    private static final String BUNDLE_TEAM_ID = "team_id";

    private UserRepository userRepository;
    private TeamRepository teamRepository;
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

        Log.d("johan", "" + getContext());
        //TODO TEST
        userRepository = SqlUserRepository.getInstance(getContext());
        teamRepository = SqlTeamRepository.getInstance(getContext());
        Log.d("johanTeamDetails row47", "" +  teamRepository.getTeams());
        String teamId = String.valueOf(getArguments().getLong(BUNDLE_TEAM_ID));
        team = teamRepository.getTeam(teamId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_details_fragment, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_team_detail_title);
        tvTitle.setText(team.getTeamName());
        RecyclerView rvUsers = (RecyclerView) view.findViewById(R.id.rv_users);
        List<User> users = userRepository.getUsers();
        UserAdapter userAdapter = new UserAdapter();
        userAdapter.setData(users);
        rvUsers.setAdapter(userAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle(team.getTeamName());
        return view;
    }
}
