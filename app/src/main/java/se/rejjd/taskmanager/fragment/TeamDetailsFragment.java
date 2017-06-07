package se.rejjd.taskmanager.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.HomeScreenActivity;
import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.TeamAddUserActivity;
import se.rejjd.taskmanager.TeamUpdateActivity;
import se.rejjd.taskmanager.adapter.UserAdapter;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.service.AppStatus;
import se.rejjd.taskmanager.service.SqlLoader;

public final class TeamDetailsFragment extends Fragment {
    private static final String BUNDLE_TEAM_ID = "team_id";
    private static final int REQUEST_CODE_UPDATE_TEAM = 568;
    private static final int REQUEST_CODE_UPDATE_TEAM_WITH_USERS = 964;

    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private Team team;
    private TextView tvTitle;
    private SqlLoader sqlLoader;
    private String userLoggedIn;
    private AppStatus appStatus;

    public static Fragment newInstance(long id, String userLoggedIn) {
        Fragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_TEAM_ID, id);
        bundle.putString(HomeScreenActivity.USER_ID, userLoggedIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLoggedIn = getActivity().getIntent().getExtras().getString(HomeScreenActivity.USER_ID);

        sqlLoader = new SqlLoader(getContext(), userLoggedIn);

        Log.d("johan", "" + getContext());
        //TODO TEST
        userRepository = SqlUserRepository.getInstance(getContext());
        teamRepository = SqlTeamRepository.getInstance(getContext());
        Log.d("johanTeamDetails row47", "" + teamRepository.getTeams());
        String teamId = String.valueOf(getArguments().getLong(BUNDLE_TEAM_ID));
        team = teamRepository.getTeam(teamId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_details_fragment, container, false);
        tvTitle = (TextView) view.findViewById(R.id.tv_team_detail_title);
        tvTitle.setText(team.getTeamName());
        RecyclerView rvUsers = (RecyclerView) view.findViewById(R.id.rv_users);
        List<User> users = userRepository.getUsers();

        TextView textView = (TextView) view.findViewById(R.id.tv_team_detail_title);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (AppStatus.isOnline(getContext())) {
                    Intent intent = TeamUpdateActivity.createUpdateTeamIntent(getContext(), team.getId(), userLoggedIn);
                    startActivityForResult(intent, REQUEST_CODE_UPDATE_TEAM);
                } else {
                    runAlert();
                }
                return false;
            }
        });


        TextView tvAddUsersToTeam = (TextView) view.findViewById(R.id.tv_addmember);

        tvAddUsersToTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.isOnline(getContext())) {
                    Intent intent = TeamAddUserActivity.createIntent(getContext(), team.getId(),userLoggedIn);
                    startActivityForResult(intent, REQUEST_CODE_UPDATE_TEAM_WITH_USERS);
                } else {
                    runAlert();
                }
            }
        });

        UserAdapter userAdapter = new UserAdapter();
        userAdapter.setData(users);
        rvUsers.setAdapter(userAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle(team.getTeamName());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_UPDATE_TEAM){
            if(resultCode == RESULT_OK){

            }
        }
        if(requestCode == REQUEST_CODE_UPDATE_TEAM_WITH_USERS){
            if(resultCode == RESULT_OK){

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sqlLoader.updateSqlFromHttp();
        team = teamRepository.getTeams().get(0);
        tvTitle.setText(team.getTeamName());
    }

    private void runAlert() {
        AlertDialog.Builder alertWindow = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert);
        alertWindow.setMessage("Please connect to the internet");
        alertWindow.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertWindow.show();
    }
}
