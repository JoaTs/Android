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

import se.rejjd.taskmanager.HomeScreenActivity;
import se.rejjd.taskmanager.R;
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

        unstartedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(unstartedItems, unstartedNumber, unstartedTitle);
            }
        });

        //TODO set actual progressnumbers
        startedItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(startedItems, startedNumber, startedTitle);
            }
        });

        doneItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(doneItems, doneNumber, doneTitle);
            }
        });

        myItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeChart(myItems, myItemsNumber, myItemsTitle);
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

    private void activeChart(ProgressBar bar, TextView number, TextView title){
        bar.setScaleX(1.1F);
        bar.setScaleY(1.1F);
        number.setScaleX(1.1F);
        number.setScaleY(1.1F);
        title.setTextSize(14);
        title.setTextColor(Color.parseColor("#FFA000"));
    }
}

