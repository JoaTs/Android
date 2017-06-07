package se.rejjd.taskmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.model.Chart;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public class ChartFragment extends Fragment {

    private final static String USER_ID = "userId";
    private final static int MAX_WORKITEMS = 5;
    private final String unstarted = "UNSTARTED";
    private final String started = "STARTED";
    private final String done = "DONE";
    private WorkItemRepository workItemRepository;
    private User user;
    private ProgressBar unstartedItems;
    private ProgressBar startedItems;
    private ProgressBar doneItems;
    private ProgressBar myItems;
    private List<Chart> progressBars;
    private CallBacks callBacks;

    public static Fragment newInstance(String id){
        Fragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    public interface CallBacks{
        void onChartClicked(int position);
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
        UserRepository userRepository = SqlUserRepository.getInstance(getContext());
        user = userRepository.getUser(getArguments().getString(USER_ID));
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

        progressBars = new ArrayList<>();

        progressBars.add(chartUnstartedItems);
        progressBars.add(chartStartedItems);
        progressBars.add(chartDoneItems);
        progressBars.add(chartMyitems);

        setIsActiveCharts(0);
        activeChart();
        unstartedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onChartClicked(0);
                setIsActiveCharts(0);
                activeChart();
            }
        });

        startedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onChartClicked(1);
                setIsActiveCharts(1);
                activeChart();
            }
        });

        doneItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onChartClicked(2);
                setIsActiveCharts(2);
                activeChart();
            }
        });

        myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onChartClicked(3);
                setIsActiveCharts(3);
                activeChart();
            }
        });

        return view;
    }

    private void setMaxProgress() {
        unstartedItems.setMax(workItemRepository.getWorkItems().size());
        startedItems.setMax(workItemRepository.getWorkItems().size());
        doneItems.setMax(workItemRepository.getWorkItems().size());
        myItems.setMax(MAX_WORKITEMS);
    }

    private void setProgress() {
        unstartedItems.setProgress(workItemRepository.getWorkItemByStatus(unstarted).size());
        startedItems.setProgress(workItemRepository.getWorkItemByStatus(started).size());
        doneItems.setProgress(workItemRepository.getWorkItemByStatus(done).size());
        myItems.setProgress(workItemRepository.getWorkItemsByUser(String.valueOf(user.getId())).size());
    }

    public void activeChart(){

        for(Chart chart: progressBars){
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

    public void setIsActiveCharts(int position){
        Chart chart = progressBars.get(position);
        for (Chart c : progressBars){
            if (c.equals(chart)){
                c.getProgressBar().setSelected(true);
            }else{
                c.getProgressBar().setSelected(false);
            }
        }
    }
}