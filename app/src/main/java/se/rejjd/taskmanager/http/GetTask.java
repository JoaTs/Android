package se.rejjd.taskmanager.http;

import android.os.AsyncTask;

public final class GetTask extends AsyncTask<Void, Void, HttpResponse> {
    private final HttpHelperCommand httpHelperCommand;

    public GetTask(HttpHelperCommand httpHelperCommand) {
        this.httpHelperCommand = httpHelperCommand;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        return httpHelperCommand.execute();
    }

}
