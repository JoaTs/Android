package se.rejjd.taskmanager.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpResponse;
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

    private final HttpUserRepository httpUserRepository;
    private final UserRepository sqlUserRepository;

    private final HttpTeamRepository httpTeamRepository;
    private final TeamRepository sqlTeamRepository;

    private final HttpWorkItemRepository httpWorkItemRepository;
    private final WorkItemRepository sqlWorkItemRepository;

    //TODO. ONLY ONE INSTANCE OF THIS SHOULD BE ALLOWED
    //CREATE GET INSTANCE OF. STATIC LOGGED IN

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
            Log.d("johanSqlLoader", "You got Internet!!!!");
             //TODO REMOVE OLD DATA ON SQLite
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.TEAM_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.USERS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.WORK_ITEMS_TABLE_NAME);
            database.execSQL("DELETE FROM " + DatabaseContract.ModelEntry.ISSUES_TABLE_NAME);

            update();
            return true;
        } else {
            Log.d("johanSqlLoader", "Your NOT on the Internet!!!!");
        }
        return false;
    }

    private void update(){
                httpUserRepository.getUser(String.valueOf(userLoggedIn), new GetTask.OnResultListener() {
            @Override
            public void onResult(HttpResponse httpResult) {
                User user = httpUserRepository.parserUser(httpResult.getResponseAsString());
                sqlUserRepository.addUser(user);

            }
        });
        final User user = sqlUserRepository.getUser(userLoggedIn);

        httpTeamRepository.getTeam(String.valueOf(user.getTeamId()), new GetTask.OnResultListener() {
            @Override
            public void onResult(HttpResponse result) {
                Team team = httpTeamRepository.parserTeam(result.getResponseAsString());
                sqlTeamRepository.addTeam(team);
            }
        });


          //TODO Team dose not load on SQLite

        List<WorkItem> workitemList = null;

        httpWorkItemRepository.getWorkItemsFromTeam(user.getTeamId(), new GetTask.OnResultListener() {
            @Override
            public void onResult(HttpResponse result) {
                List<WorkItem> workitemList = httpWorkItemRepository.parserWorkItems(String.valueOf(user.getTeamId()));
                for(WorkItem w : workitemList){
                    sqlWorkItemRepository.addWorkItem(w);
                }
            }
        });



        httpUserRepository.getUsersFromTeam(user.getTeamId(), new GetTask.OnResultListener() {
            @Override
            public void onResult(HttpResponse result) {
                List<User> userList = httpUserRepository.parserUsers(result.getResponseAsString());
                for(User u : userList){
                    sqlUserRepository.addUser(u);
                }
            }
        });

    }
}
