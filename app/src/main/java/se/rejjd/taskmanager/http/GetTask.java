package se.rejjd.taskmanager.http;

import android.os.AsyncTask;

public  class GetTask extends AsyncTask<Void, Void, HttpResponse> {
    HttpHelperCommand httpHelperCommand;

    public GetTask(HttpHelperCommand httpHelperCommand) {
        this.httpHelperCommand = httpHelperCommand;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        return httpHelperCommand.execute();
    }

}
