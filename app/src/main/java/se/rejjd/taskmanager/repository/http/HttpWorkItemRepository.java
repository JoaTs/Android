package se.rejjd.taskmanager.repository.http;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;

public class HttpWorkItemRepository extends HttpHelper implements WorkItemRepository {

    private final String URL = "http://10.0.2.2:8080/";

    @Override
    public List<WorkItem> getWorkItems() {

        HttpResponse httpResponse = null;
            try {
                httpResponse = new GetTask(new HttpHelperCommand() {
                    @Override
                    public HttpResponse execute() {
                        return get(URL + "workitems");
                    }
                }).execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        return parserWorkItem(httpResponse.getResponseAsString());
    }


    @Override
    public WorkItem getWorkItem(final int id) {

        HttpResponse httpResponse = null;
        try {
            httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "workitems/" + id);
                }
            }).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return parserWorkItem(httpResponse.getResponseAsString()).get(0);
    }

    @Override
    public int addWorkItem(WorkItem workItem) {
        HttpResponse httpResponse = null;

        final String body =

                "{"+
                "\"id\": " + workItem.getId() + "," +
                "\"title\": \""+ workItem.getTitle()+"\","+
                "\"description\": \""+workItem.getDescription()+"\""+
                "}";

        try {
            httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "/workitems",body);
                }
            }).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("johan", "did we get a Status Code??:" + httpResponse.getStatusCode());

        Log.d("johan", "did we get the ID?:" + httpResponse.getHeaders().get("Location").toString());

        return 0;
    }


    //PARSER TODO RETURN VALUE
    private List<WorkItem> parserWorkItem(String jsonString) {
        try {
            List<WorkItem> workItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                WorkItem workitem = new WorkItem(id, title, description);
                workItems.add(workitem);
            }

            return workItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null; //TODO
    }



}

