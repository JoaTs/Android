package se.rejjd.taskmanager.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.HomeScreenActivity;
import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.model.Chart;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class ChartFragment extends Fragment {

    private WorkItemRepository workItemRepository;
    private UserRepository userRepository;
    private final String unstarted = "UNSTARTED";
    private final String started = "STARTED";
    private final String done = "DONE";
    private User user;
    private ProgressBar unstartedItems;
    private ProgressBar startedItems;
    private ProgressBar doneItems;
    private ProgressBar myItems;
    private List<Chart> progressbars;

    private ChartFragment.CallBacks callBacks;

    public static Fragment newInstance(){
        return new ChartFragment();
    }

    public interface CallBacks{
        void onListItemClicked(int position);
        void onPageChange(int position);
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
        workItemRepository = SqlWorkItemRepository.getInstance(getContext());
        userRepository = SqlUserRepository.getInstance(getContext());
        //TODO hårdkodning
        user = userRepository.getUser("2002");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment,container,false);

        unstartedItems = (ProgressBar) view.findViewById(R.id.unstarted_items);
        startedItems = (ProgressBar) view.findViewById(R.id.started_items);
        doneItems = (ProgressBar) view.findViewById(R.id.done_items);
        myItems = (ProgressBar) view.findViewById(R.id.my_items);

        final TextView unstartedNumber = (TextView) view.findViewById(R.id.tv_unstarted_number);
        final TextView startedNumber = (TextView) view.findViewById(R.id.tv_started_number);
        final TextView doneNumber = (TextView) view.findViewById(R.id.tv_done_number);
        final TextView myItemsNumber = (TextView) view.findViewById(R.id.tv_my_items_number);

        final TextView unstartedTitle = (TextView) view.findViewById(R.id.tv_unstarted);
        final TextView startedTitle = (TextView) view.findViewById(R.id.tv_started);
        final TextView doneTitle = (TextView) view.findViewById(R.id.tv_done);
        final TextView myItemsTitle = (TextView) view.findViewById(R.id.tv_my_items);

        unstartedNumber.setText(String.valueOf(workItemRepository.getWorkItemByStatus(unstarted).size()));
        startedNumber.setText(String.valueOf(workItemRepository.getWorkItemByStatus(started).size()));
        doneNumber.setText(String.valueOf(workItemRepository.getWorkItemByStatus(done).size()));
        myItemsNumber.setText(String.valueOf(workItemRepository.getWorkItemsByUser(String.valueOf(user.getId())).size()));

        setMaxProgress();

        setProgress();

        final Chart chartMyitems = new Chart(myItems,myItemsTitle,myItemsNumber);
        final Chart chartUnstartedItems = new Chart(unstartedItems,unstartedTitle,unstartedNumber);
        final Chart chartDoneItems = new Chart(doneItems,doneTitle,doneNumber);
        final Chart chartStartedItems = new Chart(startedItems,startedTitle,startedNumber);

        progressbars = new ArrayList<>();

        progressbars.add(chartUnstartedItems);
        progressbars.add(chartStartedItems);
        progressbars.add(chartDoneItems);
        progressbars.add(chartMyitems);

        setIsActiveCharts(0);
        activeChart();
        unstartedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onListItemClicked(0);
                setIsActiveCharts(0);
                activeChart();
            }
        });

        //TODO set actual progressnumbers
        startedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onListItemClicked(1);
                setIsActiveCharts(1);
                activeChart();
            }
        });

        doneItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onListItemClicked(2);
                setIsActiveCharts(2);
                activeChart();
                Log.d("", "onClick: " + myItems.isSelected());
            }
        });

        myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onListItemClicked(3);
                setIsActiveCharts(3);
                activeChart();
                Log.d("", "onClick: "+ myItems.isSelected());
            }
        });

        return view;
    }

    private void setMaxProgress() {
        unstartedItems.setMax(workItemRepository.getWorkItems().size());
        startedItems.setMax(workItemRepository.getWorkItems().size());
        doneItems.setMax(workItemRepository.getWorkItems().size());
        myItems.setMax(5);
    }

    private void setProgress() {
        unstartedItems.setProgress(workItemRepository.getWorkItemByStatus(unstarted).size());
        startedItems.setProgress(workItemRepository.getWorkItemByStatus(started).size());
        doneItems.setProgress(workItemRepository.getWorkItemByStatus(done).size());
        myItems.setProgress(workItemRepository.getWorkItemsByUser(String.valueOf(user.getId())).size());
    }

    private boolean isActiveChart(ProgressBar progressBar){
        if(progressBar.isSelected())
        if(progressBar.getScaleX() == 1.1F && progressBar.getScaleY() == 1.1F){
            return true;
        }
        return false;
    }

    public void activeChart(){

        for(Chart chart:progressbars){
            if(chart.getProgressBar().isSelected()){
                chart.getProgressBar().setScaleX(1.1F);
                chart.getProgressBar().setScaleY(1.1F);
                chart.getNumber().setScaleX(1.1F);
                chart.getNumber().setScaleY(1.1F);
                chart.getTitle().setTextSize(14);
                chart.getTitle().setTextColor(getResources().getColor(R.color.primary_orange));
            } else{
                chart.getProgressBar().setScaleX(1.0F);
                chart.getProgressBar().setScaleY(1.0F);
                chart.getNumber().setScaleX(1.0F);
                chart.getNumber().setScaleY(1.0F);
                chart.getTitle().setTextSize(12);
                chart.getTitle().setTextColor(getResources().getColor(R.color.primary_gray));
            }

        }
    }
    public void activeChart(int position){
        Chart chart = progressbars.get(position);

        for (Chart charts : progressbars){
            if(chart.getProgressBar().isSelected()) {
                chart.getProgressBar().setScaleX(1.1F);
                chart.getProgressBar().setScaleY(1.1F);
                chart.getNumber().setScaleX(1.1F);
                chart.getNumber().setScaleY(1.1F);
                chart.getTitle().setTextSize(14);
                chart.getTitle().setTextColor(getResources().getColor(R.color.primary_orange));
            }

        }
    }

    public void setIsActiveCharts(int position){
        Chart chart = progressbars.get(position);
        for (Chart c : progressbars){
            if (c.equals(chart)){
                c.getProgressBar().setSelected(true);
            }else{
                c.getProgressBar().setSelected(false);
            }
        }
//        progressBar.setSelected(true);
//        progressBar2.setSelected(false);
//        progressBar3.setSelected(false);
//        progressBar4.setSelected(false);
    }
}

