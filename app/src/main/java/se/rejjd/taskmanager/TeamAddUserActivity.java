package se.rejjd.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;

public class TeamAddUserActivity extends AppCompatActivity {

    private static final String EXTRA_TEAM_ID = "teamId";
    private long teamId;
    private HttpUserRepository httpUserRepository;
    private SqlUserRepository sqlUserRepository;
    private RecyclerView rvUsers;


    public static Intent createIntent(Context context, long teamId) {
        Intent intent = new Intent(context, TeamAddUserActivity.class);
        intent.putExtra(EXTRA_TEAM_ID, teamId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_add_user);

        httpUserRepository = new HttpUserRepository();
        sqlUserRepository = SqlUserRepository.getInstance(this);
        teamId = getIntent().getExtras().getLong(EXTRA_TEAM_ID);

        List<User> users = httpUserRepository.getUsers();


        rvUsers = (RecyclerView) findViewById(R.id.rv_users_add_to_team);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        updateAdapter();
    }




    private void updateAdapter() {
        rvUsers.setAdapter(new UserListAdapter(httpUserRepository.getUsers(),sqlUserRepository.getUsers()));
    }




    private static class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private final List<User> users;
        private final List<User> inTeamList;

        UserListAdapter(List<User> users, List<User> inTeamList) {
            this.users = users;
            this.inTeamList = inTeamList;
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
            holder.bindView(user);
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

    private static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvUsername;
        private List<User> inTeamList;
        private final TextView tvAdded;

        public UserViewHolder(View itemView, List<User> inTeamList) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tv_firstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tv_lastName);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvAdded = (TextView) itemView.findViewById(R.id.tv_added);
            this.inTeamList = inTeamList;
            itemView.setOnLongClickListener(this);
        }

        public void bindView(User user) {
            tvFirstName.setText(user.getFirstname());
            tvLastName.setText(user.getLastname());
            tvUsername.setText(user.getUsername());
            if(inTeamList.contains(user)){
                tvAdded.setVisibility(View.VISIBLE);
            }else{
                tvAdded.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            v.showContextMenu();
            TextView tvAdded = (TextView) v.findViewById(R.id.tv_added);
            if (tvAdded.getVisibility() == View.VISIBLE) {
                tvAdded.setVisibility(View.INVISIBLE);
            } else {
                tvAdded.setVisibility(View.VISIBLE);
            }
            return true;
        }
    }


}
