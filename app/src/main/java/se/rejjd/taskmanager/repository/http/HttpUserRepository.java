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

public final class HttpUserRepository extends HttpHelper implements UserRepository{
    private final String URL = "http://10.0.2.2:8080/";

    @Override
    public List<User> getUsers() {
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "users");
                }
            }).execute().get();
            return parserUsers(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public List<User> getUsersFromTeam(final long teamId) {
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams/" + teamId + "/users");
                }
            }).execute().get();
            return parserUsers(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public User getUser(final String id) {
        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "users/" + id);
                }
            }).execute().get();
            return parserUser(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long addUser(User user) {
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
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "users", body);
                }
            }).execute().get();

            if (httpResponse.getStatusCode() == 201) {
                String[] splitArray = httpResponse.getHeaders().get("Location").get(0).split("/")
                        ;
                String returnValue = splitArray[splitArray.length - 1];
                return Long.valueOf(returnValue);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public boolean addUserToWorkItem(final String userId,final long workItemId){

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "users/"+ userId +"/workitems/" + workItemId, null);
                }
            }).execute().get();
            return httpResponse.getStatusCode() == 200;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public User updateUser(final User user) {
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
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "users/" + user.getUserId() , body);
                }
            }).execute().get();

            return (httpResponse.getStatusCode() == 200)? user : null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private User parserUser(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String username = jsonObject.getString("username");
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String userId = jsonObject.getString("userId");
            boolean activeUser = jsonObject.getBoolean("activeUser");

            long teamId = 0;

            if(!jsonObject.isNull("team"));  //TODO MAKE NICER johan  (ADD TO CONSTRUCTOR)
            {
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

    private List<User> parserUsers(String jsonString) {
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

                if(!jsonObject.isNull("team"));  //TODO MAKE NICER johan  (ADD TO CONSTRUCTOR)
                {
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
