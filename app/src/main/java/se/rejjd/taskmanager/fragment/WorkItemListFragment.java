package se.rejjd.taskmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.adapter.WorkItemAdapter;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;
import se.rejjd.taskmanager.service.AppStatus;

public final class WorkItemListFragment extends Fragment {
    private static final String WORKITEM_STATUS = "workitemstatus";
    private static final String USER_ID = "userId";
    private UserRepository userRepository;
    private User user;
    Map<WorkItem, User> users;
    private WorkItemRepository workItemRepository;
    private WorkItemAdapter workItemAdapter;
    private CallBacks callBacks;
    private List<WorkItem> workItemList;
    private List<WorkItem> workitems;

    public static Fragment newInstance(String status) {
        Fragment fragment = new WorkItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WORKITEM_STATUS, status);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstanceWithUserId(String userId) {
        Fragment fragment = new WorkItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance() {
        Fragment fragment = new WorkItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WORKITEM_STATUS, null);
        fragment.setArguments(bundle);
        return fragment;
    }


    public interface CallBacks {
        void onListItemClicked(WorkItem workItem);

        void onListItemLongClicked(WorkItem workItem);
    }

    public void updateAdapter(List<WorkItem> workItemList) {
        this.workItemList = workItemList;
        workItemAdapter.setAdapter(workItemList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBacks = (CallBacks) context;

        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Hosting activity needs callBacks impl");
        }
    }

    public void updateAdapter() {
        //TODO: updateAdapter
        workItemAdapter = new WorkItemAdapter(null, workItemRepository.getWorkItems(), new WorkItemAdapter.onCLickResultListener() {
            @Override
            public void onClickResult(WorkItem workitem) {
                callBacks.onListItemClicked(workitem);
            }
        }, new WorkItemAdapter.onLongClickListener() {
            @Override
            public void onLongClickResult(WorkItem workItem) {
                callBacks.onListItemLongClicked(workItem);
            }
        });
        workItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workItemRepository = SqlWorkItemRepository.getInstance(getActivity());
        userRepository = SqlUserRepository.getInstance(getActivity());
        users = new HashMap<>();
        Bundle bundle = getArguments();
        String status = bundle.getString(WORKITEM_STATUS);
        if (status != null) {
            workitems = workItemRepository.getWorkItemByStatus(status);
        } else if (status == null) {
            String userId = bundle.getString(USER_ID);
            if (userId != null) {
                workitems = workItemRepository.getWorkItemsByUser(userId);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_item_list_fragment, container, false);
        for (WorkItem w : workitems) {
            user = userRepository.getUserById(w.getUserId());
            users.put(w, user);
        }

        workItemAdapter = new WorkItemAdapter(users, workitems, new WorkItemAdapter.onCLickResultListener() {
            @Override
            public void onClickResult(WorkItem workitem) {
                callBacks.onListItemClicked(workitem);
            }
        }, new WorkItemAdapter.onLongClickListener() {
            @Override
            public void onLongClickResult(WorkItem workItem) {
                callBacks.onListItemLongClicked(workItem);
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(workItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
