package se.rejjd.taskmanager.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.HomeScreenActivity;
import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.TeamUpdateActivity;
import se.rejjd.taskmanager.adapter.UserAdapter;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.http.HttpTeamRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.service.AppStatus;
import se.rejjd.taskmanager.service.SqlLoader;

public final class TeamDetailsFragment extends Fragment {

    private static final String TEAM_ID = "teamId";
    private static final String USER_ID = "userId";
    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private Team team;
    private TextView tvTitle;
    private Button addMemberButton;
    private SqlLoader sqlLoader;

    public static Fragment newInstance(long id, String userLoggedIn) {
        Fragment fragment = new TeamDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(TEAM_ID, id);
        bundle.putString(USER_ID, userLoggedIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userLoggedIn = getActivity().getIntent().getExtras().getString(USER_ID);
        sqlLoader = new SqlLoader(getContext(), userLoggedIn);
        userRepository = SqlUserRepository.getInstance(getContext());
        teamRepository = SqlTeamRepository.getInstance(getContext());
        String teamId = String.valueOf(getArguments().getLong(TEAM_ID));
        team = teamRepository.getTeam(teamId);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_details_fragment, container, false);
        tvTitle = (TextView) view.findViewById(R.id.tv_team_detail_title);
        tvTitle.setText(team.getTeamName());
        addMemberButton = (Button) view.findViewById(R.id.btn_addmember);
        final RecyclerView rvUsers = (RecyclerView) view.findViewById(R.id.rv_users);
        List<User> users = userRepository.getUsers();

        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (AppStatus.isOnline(getContext())) {
                    Intent intent = TeamUpdateActivity.createUpdateTeamIntent(getContext(), team.getId());
                    startActivity(intent);
                } else {
                    runAlert();
                }
                return false;
            }
        });
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                View view = inflater.inflate(R.layout.add_user_to_team,container,false);

                final EditText etFirstname = (EditText) view.findViewById(R.id.et_firstname);

                final EditText etLastname = (EditText) view.findViewById(R.id.et_lastname);

                final EditText etUserName = (EditText) view.findViewById(R.id.et_Username);

                final EditText etUserId = (EditText) view.findViewById(R.id.et_userId);

                alertDialog.setView(view);
                alertDialog.setPositiveButton("Add", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User user = new User(-1, etUserName.getText().toString(), etFirstname.getText().toString(), etLastname.getText().toString(), etUserId.getText().toString(), 0);
                        UserRepository httpUserRepository = new HttpUserRepository();
                        long path = httpUserRepository.addUser(user);
                        User newUser = httpUserRepository.getUser(String.valueOf(path));
                        TeamRepository httpTeamRepostory = new HttpTeamRepository();
                        httpTeamRepostory.addUserToTeam(newUser,team);
                        sqlLoader.updateSqlFromHttp();
                        UserAdapter userAdapter = new UserAdapter();
                        userAdapter.setData(userRepository.getUsers());
                        rvUsers.setAdapter(userAdapter);

                    }
                });

                alertDialog.show();

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
    public void onResume() {
        super.onResume();
        sqlLoader.updateSqlFromHttp();
        team = teamRepository.getTeam(String.valueOf(team.getId()));
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