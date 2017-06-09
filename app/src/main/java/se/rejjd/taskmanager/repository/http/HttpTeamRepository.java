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
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.TeamRepository;

public final class HttpTeamRepository extends HttpHelper implements TeamRepository{
    private final String URL = "http://10.0.2.2:8080/";

    @Override
    public List<Team> getTeams() {
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams");
                }
            }).execute().get();
            return parserTeams(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Team getTeam(final String id) {
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams/" + id);
                }
            }).execute().get();
            return parserTeam(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long addTeam(Team team) {
        final String body =
                "{"+
                "\"id\": -1,"+
                "\"teamName\": \""+team.getTeamName()+"\","+
                "\"activeTeam\": "+ team.isActiveTeam()+
                "}";
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "teams", body);
                }
            }).execute().get();

            if (httpResponse.getStatusCode() == 201) {
                String[] splitArray = httpResponse.getHeaders().get("Location").get(0).split("/");
                String returnValue = splitArray[splitArray.length - 1];
                return Long.valueOf(returnValue);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    @Override
    public Team updateTeam(final Team team) {
        final String body =
                        "{"+
                        "\"id\": "+team.getId()+","+
                        "\"teamName\": \""+team.getTeamName()+"\","+
                        "\"activeTeam\": "+ team.isActiveTeam()+
                        "}";
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "teams/" + team.getId() , body);
                }
            }).execute().get();

            return (httpResponse.getStatusCode() == 200)? team : null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUserToTeam(final User user, final Team team) {
        final String body = "{\"user\":{"+
                "\"id\": "+user.getId()+","+
                "\"userId\": \""+user.getUserId()+"\"" +
                "}," +
                "\"team\" :{"+
                "\"id\": "+team.getId()+
                "}}";
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "teams/" + team.getId() + "/users/" + user.getId(), body);

                }
            }).execute().get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        return false;
    }

    private Team parserTeam(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String teamName = jsonObject.getString("teamName");

            return new Team(id, teamName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Team> parserTeams(String jsonString) {
        try {
            List<Team> teamItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                long id = jsonObject.getLong("id");
                String teamName = jsonObject.getString("teamName");

                teamItems.add(new Team(id, teamName));
            }

            return teamItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}