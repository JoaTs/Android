package se.rejjd.taskmanager;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

/**
 * Created by emeliemirhagen on 2017-05-24.
 */

public class ChartFragment extends Fragment {

    private WorkItemRepository workItemRepository;

    private ChartFragment.CallBacks callBacks;

    public static Fragment newInstance(){
        return new ChartFragment();
    }

    interface CallBacks{
        void onListItemClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callBacks = (ChartFragment.CallBacks) context;

        }catch(ClassCastException e){
            throw new IllegalArgumentException("Hosting activity needs callBacks impl");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workItemRepository = new HttpWorkItemRepository();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment,container,false);

        final ProgressBar unstartedItems = (ProgressBar) view.findViewById(R.id.unstarted_items);
        ProgressBar startedItems = (ProgressBar) view.findViewById(R.id.started_items);
        ProgressBar doneItems = (ProgressBar) view.findViewById(R.id.done_items);
        ProgressBar myItems = (ProgressBar) view.findViewById(R.id.my_items);

        final TextView unstartedNumber = (TextView) view.findViewById(R.id.tv_unstarted_number);
        unstartedNumber.setText("" + workItemRepository.getWorkItems().size() + "");
        final TextView unstartedTitle = (TextView) view.findViewById(R.id.tv_unstarted);

        unstartedItems.setMax(10);
        unstartedItems.setProgress(4);
        unstartedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(unstartedItems, unstartedNumber, unstartedTitle);
            }
        });
        startedItems.setMax(100);
        startedItems.setProgress(75);
        doneItems.setMax(100);
        doneItems.setProgress(25);
        myItems.setMax(100);
        myItems.setProgress(50);

        return view;
    }

    private void activeChart(ProgressBar bar, TextView number, TextView title){
        bar.setScaleX(1.1F);
        bar.setScaleY(1.1F);
        number.setScaleX(1.1F);
        number.setScaleY(1.1F);
        title.setTextSize(14);
        title.setTextColor(Color.parseColor("#FFA000"));
    }
}

