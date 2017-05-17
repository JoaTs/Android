package se.rejjd.taskmanager.repository.http;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.http.HttpHelper;
import se.rejjd.taskmanager.http.HttpHelperCommand;
import se.rejjd.taskmanager.http.HttpResponse;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;

public class HttpWorkItemRepository extends HttpHelper implements WorkItemRepository {

    private final String URL = "http://127.0.0.1:8080/";

    @Override
    public List<WorkItem> getWorkItems() {

        HttpResponse response = null;
               GetTask task = new GetTask(new HttpHelperCommand() {
            @Override
            public HttpResponse execute() {
                Log.d("johan", "Exeuted get");
                return get("http://10.0.2.2:8080/workitems");
            }
        }, response);
        task.execute();
        response = task.getResponse();

        if(response == null)
            Log.d("johan","failed!!");



//        return parsrWorkItem(response.getResponseAsString());

        return null;
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
        public static HttpResponse httpResponseResult;

        public HttpResponse getResponse(){
            return httpResponseResult;
        }

        public GetTask(HttpHelperCommand httpHelperCommand, HttpResponse response) {
            this.httpHelperCommand = httpHelperCommand;
            httpResponseResult = response;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            Log.d("johan", "begor doInBackgroundget" + HttpHelper.get("http://10.0.2.2:8080/workitems").toString());
            httpResponseResult = httpHelperCommand.execute();
            return httpResponseResult;
        }

        interface OnResultListener {
            HttpResponse onResult();
        }
    }

}

