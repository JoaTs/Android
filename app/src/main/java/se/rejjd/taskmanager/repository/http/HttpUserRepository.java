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
            return parserWorkItems(httpResponse.getResponseAsString());
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
            return parserWorkItem(httpResponse.getResponseAsString());
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
                "\"createdDate\": null,"+
                "\"createdBy\": null,"+
                "\"lastModifiedDate\": null,"+
                "\"lastModifiedBy\": null,"+
                "\"username\": \""+user.getUsername()+"\","+
                "\"firstname\": \""+user.getFirstaname()+"\","+
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
    public User updateUser(final User user) {
        final String body =
                "{"+
                        "\"id\": "+user.getId()+","+
                        "\"createdDate\": null,"+
                        "\"createdBy\": null,"+
                        "\"lastModifiedDate\": null,"+
                        "\"lastModifiedBy\": null,"+
                        "\"username\": \""+user.getUsername()+"\","+
                        "\"firstname\": \""+user.getFirstaname()+"\","+
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


    private User parserWorkItem(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String username = jsonObject.getString("username");
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String userId = jsonObject.getString("userId");
            boolean activeUser = jsonObject.getBoolean("activeUser");

            return new User(id,username,firstname,lastname,userId,activeUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<User> parserWorkItems(String jsonString) {
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

                userItems.add(new User(id,username,firstname,lastname,userId,activeUser));
            }

            return userItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
