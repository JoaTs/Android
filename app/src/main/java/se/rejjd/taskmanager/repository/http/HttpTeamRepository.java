package se.rejjd.taskmanager.repository.http;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import se.rejjd.taskmanager.http.GetTask;
import se.rejjd.taskmanager.http.HttpHelper;
import se.rejjd.taskmanager.http.HttpHelperCommand;
import se.rejjd.taskmanager.http.HttpResponse;
import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.repository.TeamRepository;

public final class HttpTeamRepository extends HttpHelper {
    private final String URL = "http://10.0.2.2:8080/";

    public void getTeams(GetTask.OnResultListener listener) {
        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams");
                }
            },listener).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getTeam(final String id, GetTask.OnResultListener listener) {
        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams/" + id);
                }
            },listener).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTeam(Team team, GetTask.OnResultListener listener) {
        final String body =
                "{"+
                "\"id\": -1,"+
                "\"teamName\": \""+team.getTeamName()+"\","+
                "\"activeTeam\": "+ team.isActiveTeam()+
                "}";

        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "teams", body);
                }
            },listener).execute();

//            if (httpResponse.getStatusCode() == 201) {
//                String[] splitArray = httpResponse.getHeaders().get("Location").get(0).split("/");
//                String returnValue = splitArray[splitArray.length - 1];
//                return Long.valueOf(returnValue);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO This one must customize => will it update Date and other final values
    public void updateTeam(final Team team, GetTask.OnResultListener listener) {
        final String body =
                        "{"+
                        "\"id\": "+team.getId()+","+
                        "\"teamName\": \""+team.getTeamName()+"\","+
                        "\"activeTeam\": "+ team.isActiveTeam()+
                        "}";

        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "teams/" + team.getId() , body);
                }
            },listener).execute();

//            return (httpResponse.getStatusCode() == 200)? team : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Team parserTeam(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String teamName = jsonObject.getString("teamName");
            boolean activeTeam = jsonObject.getBoolean("activeTeam");

            //(long id, String teamName, boolean activeTeam
            return new Team(id, teamName, activeTeam);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Team> parserTeams(String jsonString) {
        try {
            List<Team> teamItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                long id = jsonObject.getLong("id");
                String teamName = jsonObject.getString("teamName");
                boolean activeTeam = jsonObject.getBoolean("activeTeam");

                teamItems.add(new Team(id, teamName, activeTeam));
            }

            return teamItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
