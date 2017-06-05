package se.rejjd.taskmanager.http;

import android.os.AsyncTask;
import android.util.Log;

public final class GetTask extends AsyncTask<Void, Void, HttpResponse> {
    private final HttpHelperCommand httpHelperCommand;
    private final OnResultListener listener;

    public GetTask(HttpHelperCommand httpHelperCommand, OnResultListener listener) {
        this.httpHelperCommand = httpHelperCommand;
        this.listener = listener;
    }

    public interface OnResultListener {
        void onResult(HttpResponse result);
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        Log.d("johanGetTask21","doInBackground");
        HttpResponse httpResponse = httpHelperCommand.execute();
        listener.onResult(httpResponse);
        return httpResponse;
    }

//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected void onPostExecute(HttpResponse result) {
//        Log.d("johanGetTask21","onPostExecute");
//        listener.onResult(result);
//    }
}
