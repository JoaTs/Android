package se.rejjd.taskmanager.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    private final String userLoggedIn;
    private final Context context;

    private final UserRepository httpUserRepository;
    private final UserRepository sqlUserRepository;

    private final TeamRepository httpTeamRepository;
    private final TeamRepository sqlTeamRepository;

    private final WorkItemRepository httpWorkItemRepository;
    private final WorkItemRepository sqlWorkItemRepository;

    public SqlLoader(Context context, String userLoggedIn) {

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
        if (AppStatus.isOnline(context)) {
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.TEAM_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.USERS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.WORK_ITEMS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.ISSUES_TABLE_NAME);

            update();
            return true;
        }
        return false;
    }

    private void update(){
        User user = httpUserRepository.getUser(String.valueOf(userLoggedIn));

        Team team = httpTeamRepository.getTeam(String.valueOf(user.getTeamId()));
        sqlTeamRepository.addTeam(team);

        List<WorkItem> workitemList = httpWorkItemRepository.getWorkItemsFromTeam(user.getTeamId());
        for(WorkItem w : workitemList){
            sqlWorkItemRepository.addWorkItem(w);
        }

        List<User> userList = httpUserRepository.getUsersFromTeam(team.getId());
        for(User u : userList){
            sqlUserRepository.addUser(u);
        }
    }
}