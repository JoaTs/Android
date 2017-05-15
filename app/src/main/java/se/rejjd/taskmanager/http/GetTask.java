package se.rejjd.taskmanager.http;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public final class GetTask extends AsyncTask<Void, Void, List<String>> {
    private final OnResultListener listener;

    public GetTask(OnResultListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        String data = HttpHelper.get("http://10.0.2.2:8080/workitems/issues");
        List<String> strings = new ArrayList<>();
        strings.add(data);

        return strings;
    }

    @Override
    protected void onPostExecute(List<String> messages) {
        listener.onResult(messages);
    }

    public interface OnResultListener {
        void onResult(List<String> result);
    }
}