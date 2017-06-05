package se.rejjd.taskmanager.repository.http;

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
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.UserRepository;

public final class HttpUserRepository extends HttpHelper {
    private final String URL = "http://10.0.2.2:8080/";

    public void getUsers(GetTask.OnResultListener listener) {
        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "users");
                }
            },listener).execute();
//            return parserUsers(httpResponse.getResponseAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getUsersFromTeam(final long teamId, GetTask.OnResultListener listener) {
        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams/" + teamId + "/users");
                }
            },listener).execute();
//            return parserUsers(httpResponse.getResponseAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUser(final String id, GetTask.OnResultListener listener) {
        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "users/" + id);
                }
            },listener).execute();
//            return parserUser(httpResponse.getResponseAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user, GetTask.OnResultListener listener) {
        final String body =
                "{"+
                "\"id\": "+user.getId()+","+
                "\"username\": \""+user.getUsername()+"\","+
                "\"firstname\": \""+user.getFirstname()+"\","+
                "\"lastname\": \""+user.getLastname()+"\","+
                "\"userId\": \""+user.getUserId()+"\","+
                "\"activeUser\": "+ user.isActiveUser() +
                "}" ;

        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "users", body);
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

    public void addUserToWorkItem(final String userId,final long workItemId, GetTask.OnResultListener listener){

        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "users/"+ userId +"/workitems/" + workItemId, null);
                }
            },listener).execute();
//            return httpResponse.getStatusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateUser(final User user, GetTask.OnResultListener listener) {
        final String body =
                "{"+
                        "\"id\": "+user.getId()+","+
                        "\"username\": \""+user.getUsername()+"\","+
                        "\"firstname\": \""+user.getFirstname()+"\","+
                        "\"lastname\": \""+user.getLastname()+"\","+
                        "\"userId\": \""+user.getUserId()+"\","+
                        "\"activeUser\": "+ user.isActiveUser() +
                        "}" ;

        try {
            new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "users/" + user.getUserId() , body);
                }
            },listener).execute().get();

//            return (httpResponse.getStatusCode() == 200)? user : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public User parserUser(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String username = jsonObject.getString("username");
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String userId = jsonObject.getString("userId");
            boolean activeUser = jsonObject.getBoolean("activeUser");

            long teamId = 0;

            if(!jsonObject.isNull("team")){
                JSONObject jsonTeamObject = new JSONObject(jsonObject.getString("team"));
                teamId = jsonTeamObject.getLong("id");
            }

            User user = new User(id,username,firstname,lastname,userId,activeUser, teamId);

            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> parserUsers(String jsonString) {
        try {
            List<User> userItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                long id = jsonObject.getLong("id");
                String username = jsonObject.getString("username");
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String userId = jsonObject.getString("userId");
                boolean activeUser = jsonObject.getBoolean("activeUser");


                long teamId = 0;

                if(!jsonObject.isNull("team")){
                    JSONObject jsonTeamObject = new JSONObject(jsonObject.getString("team"));
                     teamId = jsonTeamObject.getLong("id");
                }

                User user = new User(id,username,firstname,lastname,userId,activeUser, teamId);

                userItems.add(user);
            }

            return userItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
