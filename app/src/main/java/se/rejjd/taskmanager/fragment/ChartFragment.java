package se.rejjd.taskmanager.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;

public class ChartFragment extends Fragment {

    private WorkItemRepository workItemRepository;

    private ChartFragment.CallBacks callBacks;

    public static Fragment newInstance(){
        return new ChartFragment();
    }

    public interface CallBacks{
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
        final ProgressBar startedItems = (ProgressBar) view.findViewById(R.id.started_items);
        final ProgressBar doneItems = (ProgressBar) view.findViewById(R.id.done_items);
        final ProgressBar myItems = (ProgressBar) view.findViewById(R.id.my_items);

        final TextView unstartedNumber = (TextView) view.findViewById(R.id.tv_unstarted_number);
        final TextView startedNumber = (TextView) view.findViewById(R.id.tv_started_number);
        final TextView doneNumber = (TextView) view.findViewById(R.id.tv_done_number);
        final TextView myItemsNumber = (TextView) view.findViewById(R.id.tv_my_items_number);

        unstartedNumber.setText("" + workItemRepository.getWorkItems().size() + "");
        startedNumber.setText("" + workItemRepository.getWorkItems().size() + "");
        doneNumber.setText("" + workItemRepository.getWorkItems().size() + "");
        myItemsNumber.setText("" + workItemRepository.getWorkItems().size() + "");

        final TextView unstartedTitle = (TextView) view.findViewById(R.id.tv_unstarted);
        final TextView startedTitle = (TextView) view.findViewById(R.id.tv_started);
        final TextView doneTitle = (TextView) view.findViewById(R.id.tv_done);
        final TextView myItemsTitle = (TextView) view.findViewById(R.id.tv_my_items);

        unstartedItems.setMax(10);
        unstartedItems.setProgress(4);
        unstartedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(unstartedItems, unstartedNumber, unstartedTitle);
            }
        });

        //TODO set actual progressnumbers
        startedItems.setMax(100);
        startedItems.setProgress(75);
        startedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(startedItems, startedNumber, startedTitle);
            }
        });

        doneItems.setMax(100);
        doneItems.setProgress(25);
        doneItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(doneItems, doneNumber, doneTitle);
            }
        });

        myItems.setMax(100);
        myItems.setProgress(50);
        myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(myItems, myItemsNumber, myItemsTitle);
            }
        });

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

