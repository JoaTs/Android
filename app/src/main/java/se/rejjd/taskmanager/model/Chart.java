package se.rejjd.taskmanager.model;

import android.widget.ProgressBar;
import android.widget.TextView;

public final class Chart {
    private final ProgressBar progressBar;
    private final TextView title;
    private final TextView number;

    public Chart(ProgressBar progressBar,TextView title, TextView number){
        this.progressBar = progressBar;
        this.title = title;
        this.number = number;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getNumber() {
        return number;
    }
}
