package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.http.HttpTeamRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.service.SqlLoader;

public class TeamAddUserActivity extends AppCompatActivity {

    private static final String EXTRA_TEAM_ID = "teamId";
    private long teamId;
    private HttpTeamRepository httpTeamRepository;
    private HttpUserRepository httpUserRepository;
    private SqlUserRepository sqlUserRepository;
    private RecyclerView rvUsers;
    private UserListAdapter userListAdapter;
    private SqlLoader sqlLoader;

    public static Intent createIntent(Context context, long teamId,String userId) {
        Intent intent = new Intent(context, TeamAddUserActivity.class);
        intent.putExtra(EXTRA_TEAM_ID, teamId);
        intent.putExtra(HomeScreenActivity.USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_add_user);

        teamId = getIntent().getExtras().getLong(EXTRA_TEAM_ID);
        String userLoggedIn = getIntent().getExtras().getString(HomeScreenActivity.USER_ID);

        httpTeamRepository = new HttpTeamRepository();
        httpUserRepository = new HttpUserRepository();
        sqlUserRepository = SqlUserRepository.getInstance(this);
        sqlLoader = new SqlLoader(this,userLoggedIn);

        rvUsers = (RecyclerView) findViewById(R.id.rv_users_add_to_team);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        userListAdapter = new UserListAdapter();
        userListAdapter.setOnLingClickListener(new UserListAdapter.onLongClickListener() {
            @Override
            public void onLongClickResult(User user) {

                    if(httpTeamRepository.addUserToTeam(String.valueOf(teamId), user.getUserId())){
                        Toast.makeText(TeamAddUserActivity.this, "Added!!!", Toast.LENGTH_SHORT).show();
                        updateUserListAdapter();
                    }
            }
        });
        updateUserListAdapter();
    }

    protected void updateUserListAdapter() {
        sqlLoader.updateSqlFromHttp();
        userListAdapter.updateAdapter(httpUserRepository.getUsers(),sqlUserRepository.getUsers());
        rvUsers.setAdapter(userListAdapter);
    }

    private static class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private List<User> users;
        private List<User> inTeamList;
        private onLongClickListener onLongClickListener;

        void updateAdapter(List<User> users, List<User> inTeamList) {
            this.users = users;
            this.inTeamList = inTeamList;
            notifyDataSetChanged();
        }

        void setOnLingClickListener(onLongClickListener onLongClickListener){
            this.onLongClickListener = onLongClickListener;
        }

        interface onLongClickListener {
            void onLongClickResult(User user);
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.user_list_item, parent, false);

            return new UserViewHolder(v, inTeamList);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            User user = users.get(position);
            holder.bindView(user, onLongClickListener);
        }

        @Override
        public int getItemCount() {
            return this.users.size();
        }

        @Override
        public long getItemId(int position) {
            return users.get(position).getId();
        }
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder  {
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvUsername;
        private List<User> inTeamList;
        private final TextView tvAdded;

        UserViewHolder(View itemView, List<User> inTeamList) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tv_firstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tv_lastName);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvAdded = (TextView) itemView.findViewById(R.id.tv_added);
            this.inTeamList = inTeamList;
        }

        void bindView(final User user, final UserListAdapter.onLongClickListener onLongClickListener) {
            tvFirstName.setText(user.getFirstname());
            tvLastName.setText(user.getLastname());
            tvUsername.setText(user.getUsername());
            if(inTeamList.contains(user)){
                tvAdded.setVisibility(View.VISIBLE);
            }else{
                tvAdded.setVisibility(View.INVISIBLE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.onLongClickResult(user);
                    return true;
                }
            });
        }
    }

}
