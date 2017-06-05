package se.rejjd.taskmanager.http;

import android.os.AsyncTask;

import java.util.List;

public final class GetTask extends AsyncTask<Void, Void, HttpResponse> {
    private final HttpHelperCommand httpHelperCommand;
    private final OnResultListener listener;

    public GetTask(HttpHelperCommand httpHelperCommand, OnResultListener listener) {
        this.httpHelperCommand = httpHelperCommand;
        this.listener = listener;
    }

    public interface OnResultListener<T> {
        void onResult(HttpResponse httpResult);
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        return httpHelperCommand.execute();
    }

    @Override
    protected void onPostExecute(HttpResponse httpResponse) {
        listener.onResult(httpResponse);
    }
}
