package se.rejjd.taskmanager.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;

public final class GetTask extends AsyncTask<Void, Void, List<WorkItem>> {
    private  String TAG = "bye";
    private final OnResultListener listener;

    public GetTask(OnResultListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<WorkItem> doInBackground(Void... params) {
        String data = HttpHelper.get("http://10.0.2.2:8080/workitems");
        List<String> strings = new ArrayList<>();
        strings.add(data);
        try {
            List<WorkItem> workItems = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(data);
            for(int i = 0; i < jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("description");
                WorkItem workitem = new WorkItem(id,title,description);
                workItems.add(workitem);
            }
            Log.d(TAG, "doInBackground: "+ workItems.toString());
            return workItems;
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    protected void onPostExecute(List<WorkItem> messages) {
        listener.onResult(messages);
    }

    public interface OnResultListener {
        void onResult(List<WorkItem> result);
    }
}