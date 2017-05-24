package se.rejjd.taskmanager.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.TeamRepository;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpTeamRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlTeamRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

import se.rejjd.taskmanager.sql.DatabaseContract;
import se.rejjd.taskmanager.sql.DatabaseHelper;

public final class SqlLoader {

    private final SQLiteDatabase database;

    private final long userLoggedIn;

    private final Context context;

    private final UserRepository httpUserRepository;
    private final UserRepository sqlUserRepository;

    private final TeamRepository httpTeamRepository;
    private final TeamRepository sqlTeamRepository;

    private final WorkItemRepository httpWorkItemRepository;
    private final WorkItemRepository sqlWorkItemRepository;

    //TODO. ONLY ONE INSTANCE OF THIS SHOULD BE ALLOWED
    //CREATE GET INSTANCE OF. STATIC LOGGED IN

    public SqlLoader(Context context, long userLoggedIn) {

        database = DatabaseHelper.getInstance(context).getWritableDatabase();

        this.userLoggedIn = userLoggedIn;
        this.context = context;

        httpUserRepository = new HttpUserRepository();
        sqlUserRepository = SqlUserRepository.getInstance(context);

        httpTeamRepository = new HttpTeamRepository();
        sqlTeamRepository = SqlTeamRepository.getInstance(context);

        httpWorkItemRepository = new HttpWorkItemRepository();
        sqlWorkItemRepository = SqlWorkItemRepository.getInstance(context);
    }

    public boolean updateSqlFromHttp() {
        if (AppStatus.getInstance(context).isOnline()) {
            Log.d("johanSqlLoader", "You got Internet!!!!");
            //TODO REMOVE OLD DATA ON SQLite
            //TODO BAAAAD PRATIC   I NEED YOR HELP JOAKIM!!
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.TEAM_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.USERS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.WORK_ITEMS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.ISSUES_TABLE_NAME);
            database.execSQL("Delete FROM sqlite_sequence where name='" + DatabaseContract.ModelEntry.USERS_TABLE_NAME + "'");
            database.execSQL("Delete FROM sqlite_sequence where name='" + DatabaseContract.ModelEntry.TEAM_TABLE_NAME + "'");
            database.execSQL("Delete FROM sqlite_sequence where name='" + DatabaseContract.ModelEntry.WORK_ITEMS_TABLE_NAME + "'");
            database.execSQL("Delete FROM sqlite_sequence where name='" + DatabaseContract.ModelEntry.ISSUES_TABLE_NAME + "'");


            update();
            return true;
        } else {
            Log.d("johanSqlLoader", "You NOT on Internet!!!!");
        }
        return false;
    }

    private void update() {
        Toast.makeText(context, "" + userLoggedIn, Toast.LENGTH_SHORT).show();
        User user = httpUserRepository.getUser(String.valueOf(userLoggedIn));
        Log.d("johanSqlLoader", user.toString());

        //TODO LISTS IN USE:
        //One Team
        Team team = httpTeamRepository.getTeam(String.valueOf(user.getTeamId()));
        Log.d("johanSqlLoader", team.toString());
        sqlTeamRepository.addTeam(team);
        Log.d("johanSqlLoaderSQL", sqlTeamRepository.getTeams().toString());

        List<WorkItem> workitemList = httpWorkItemRepository.getWorkItemsFromTeam(user.getTeamId());
        Log.d("johanSqlLoader", workitemList.toString());
        for (WorkItem w : workitemList) {
            sqlWorkItemRepository.addWorkItem(w);
        }
        Log.d("johanSqlLoaderSql", sqlWorkItemRepository.getWorkItems().toString());

        List<User> userList = httpUserRepository.getUsersFromTeam(team.getId());
        Log.d("johanSqlLoader", userList.toString());

        for (User u : userList) {
            sqlUserRepository.addUser(u);
        }
        Log.d("johanSqlLoadesql", sqlUserRepository.getUsers().toString());
    }
}
