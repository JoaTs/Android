package se.rejjd.taskmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.adapter.WorkItemAdapter;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public final class WorkItemListFragment extends Fragment {

    private WorkItemRepository workItemRepository;
    private WorkItemAdapter workItemAdapter;
    private CallBacks callBacks;
    private List<WorkItem> workItemList;

    public static Fragment newInstance() {
        return new WorkItemListFragment();
    }


    public interface CallBacks{
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
        workItemAdapter = new WorkItemAdapter(workItemRepository.getWorkItems(), new WorkItemAdapter.onCLickResultListener() {
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
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_item_list_fragment, container, false);
        workItemAdapter = new WorkItemAdapter(workItemRepository.getWorkItems(), new WorkItemAdapter.onCLickResultListener() {
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
