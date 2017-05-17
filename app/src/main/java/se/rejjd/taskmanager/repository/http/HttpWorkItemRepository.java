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

import se.rejjd.taskmanager.http.HttpHelper;
import se.rejjd.taskmanager.http.HttpHelperCommand;
import se.rejjd.taskmanager.http.HttpResponse;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;

public class HttpWorkItemRepository extends HttpHelper implements WorkItemRepository {

    private final String URL = "http://127.0.0.1:8080/";

    @Override
    public List<WorkItem> getWorkItems() {

        HttpResponse httpResponse1 = null;


        try {
            try {
                httpResponse1 = new GetTask(new HttpHelperCommand() {
                    @Override
                    public HttpResponse execute() {
                        Log.d("johan", "Exeuted get");
                        return get("http://10.0.2.2:8080/workitems");
                    }
                }, new GetTask.OnResultListener() {
                    @Override
                    public HttpResponse onResult(HttpResponse httpResponse) {
                        Log.d("johan", " test " + httpResponse.getResponseAsString());
                        return null;
                    }
                }).execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//        return parsrWorkItem(response.getResponseAsString());

        return parsrWorkItem(httpResponse1.getResponseAsString());
    }


    @Override
    public WorkItem getWorkItem(int id) {
        return null;
    }

    @Override
    public int addWorkItem(WorkItem workItem) {
        return 0;
    }


    //PARSER TODO RETURN VALUE
    private List<WorkItem> parsrWorkItem(String jsonString) {
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


    public static class GetTask extends AsyncTask<Void, HttpResponse, HttpResponse> {
        HttpHelperCommand httpHelperCommand;
        private final HttpHelper httpHelper;
        OnResultListener onResultListener;


        public GetTask(HttpHelperCommand httpHelperCommand, OnResultListener onResultListener) {
            this.httpHelperCommand = httpHelperCommand;
            httpHelper = new HttpHelper();
            this.onResultListener =  onResultListener;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
//            Log.d("johan", "begor doInBackgroundget" + httpHelper.get("http://10.0.2.2:8080/workitems").getResponseAsString());
            Log.d("johan", "begor doInBackgroundget" + httpHelperCommand.execute().getResponseAsString());
//            httpResponseResult = httpHelperCommand.execute();
            return httpHelperCommand.execute();
        }

        @Override
        protected void onPostExecute(HttpResponse httpResponse) {
            onResultListener.onResult(httpResponse);
        }

        interface OnResultListener {
            HttpResponse onResult(HttpResponse httpResponse);
        }
    }

}

