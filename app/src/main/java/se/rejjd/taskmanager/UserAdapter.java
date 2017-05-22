package se.rejjd.taskmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.model.User;

/**
 * Created by emeliemirhagen on 2017-05-22.
 */

public final class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    public void setData(List<User> users){this.users = users;}

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View teamDetails = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(teamDetails);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindView(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static final class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvUsername;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tv_firstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tv_lastName);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);

        }

        public void bindView(User user) {
            tvFirstName.setText(user.getFirstname());
            tvLastName.setText(user.getLastname());
            tvUsername.setText(user.getUsername());
        }
    }
}
