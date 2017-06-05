package se.rejjd.taskmanager.http;

import android.os.AsyncTask;

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
        return httpHelperCommand.execute();
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        listener.onResult(result);
    }
}
