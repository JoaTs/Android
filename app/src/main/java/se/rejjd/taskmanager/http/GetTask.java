package se.rejjd.taskmanager.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public final class GetTask extends AsyncTask<Void,Void,String> {

    private static final String TAG = GetTask.class.getSimpleName();
    private OnResultListener listener;

    public GetTask(OnResultListener listener) {
        this.listener = listener;
    }

public interface OnResultListener{
        void onResult(String string);
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = HttpHelper.get("http://10.0.2.2:8080/workitems");
        Log.d(TAG, "doInBackground: " + data);
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onResult(s);
    }
}
