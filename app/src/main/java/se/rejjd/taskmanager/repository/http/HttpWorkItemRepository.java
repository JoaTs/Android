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
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;

public class HttpWorkItemRepository extends HttpHelper implements WorkItemRepository {

    private final String URL = "http://10.0.2.2:8080/";

    @Override
    public List<WorkItem> getWorkItems() {

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "workitems");
                }
            }).execute().get();
            return parserWorkItems(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    //TODO Should this method be added to Interface????
    public List<WorkItem> getWorkItemsFromTeam(final long teamId) {

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "teams/" + teamId + "/workitems");
                }
            }).execute().get();
            return parserWorkItems(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public WorkItem getWorkItem(final String id) {

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return get(URL + "workitems/" + id);
                }
            }).execute().get();
            return parserWorkItem(httpResponse.getResponseAsString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long addWorkItem(WorkItem workItem) {

        final String body =
                "{" +
                        "\"id\": " + workItem.getId() + "," +
                        "\"title\": \"" + workItem.getTitle() + "\"," +
                        "\"description\": \"" + workItem.getDescription() + "\"" +
                        "}";

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return post(URL + "workitems", body);
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

    //This method update only the workItems status
    @Override
    public WorkItem updateWorkItem(final WorkItem workItem) {

//        final String body =
//                "{" +
//                "\"id\": \"" + ": \"82\","+
//                "\"createdDate\": \"2017-05-17\","+
//                "\"createdBy\": \"DreamierTeam\","+
//                "\"lastModifiedDate\": \"2017-05-17\","+
//                "\"lastModifiedBy\": \"DreamierTeam\","+
//                "\"title\": \"en title\","+
//                "\"description\": \"Uppdate?\","+
//                "\"status\": \""+ workItem.getStatus() +"\","+
//                "\"user\": null,"+
//                "\"dateOfCompletion\": \"\""+
//                "}";

        final String body =
                "{"+
                "\"id\": "+workItem.getId()+","+
                "\"title\": \""+workItem.getTitle()+"\","+
                "\"description\": \""+workItem.getDescription()+"\","+
                "\"status\": \""+workItem.getStatus()+"\","+
                "\"user\": null,"+
                "}";

        Log.d("johanHttpWorkRepRow127",body);

        try {
            HttpResponse httpResponse = new GetTask(new HttpHelperCommand() {
                @Override
                public HttpResponse execute() {
                    return put(URL + "workitems/" + workItem.getId() , body);
                }
            }).execute().get();
            Log.d("johan", "updateWorkItem: " + httpResponse.getStatusCode());
            return (httpResponse.getStatusCode() == 200)? workItem : null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WorkItem parserWorkItem(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            long id = jsonObject.getLong("id");
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");
            String status = jsonObject.getString("status");
            long userLongId = 0;

            if(!jsonObject.isNull("user")) {
                JSONObject jsonUserObject = new JSONObject(jsonObject.getString("user"));
                userLongId = jsonUserObject.getLong("id"); //TODO MAKE NICER johan
            }
            WorkItem workItem = new WorkItem(id, title, description, userLongId);
            workItem.setStatus(status);

            return workItem;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<WorkItem> parserWorkItems(String jsonString) {
        try {
            List<WorkItem> workItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                String status = jsonObject.getString("status");

                long userLongId = 0;

                if(!jsonObject.isNull("user")) {
                    JSONObject jsonUserObject = new JSONObject(jsonObject.getString("user"));
                    userLongId = jsonUserObject.getLong("id"); //TODO MAKE NICER johan
                }
                WorkItem workItem = new WorkItem(id, title, description, userLongId);
                workItem.setStatus(status);

                workItems.add(workItem);
            }

            return workItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

