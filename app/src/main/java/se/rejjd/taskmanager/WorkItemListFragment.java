package se.rejjd.taskmanager;

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

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public final class WorkItemListFragment extends Fragment {

    private WorkItemRepository workItemRepository;

    private CallBacks callBacks;

    public static Fragment newInstance(){
        return new WorkItemListFragment();
    }

    interface CallBacks{
        void onListItemClicked(WorkItem workItem);
        void onListItemLongClicked(WorkItem workItem);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callBacks = (CallBacks) context;

        }catch(ClassCastException e){
            throw new IllegalArgumentException("Hosting activity needs callBacks impl");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        workItemRepository = new HttpWorkItemRepository();
        workItemRepository = SqlWorkItemRepository.getInstance(getActivity());
//        for (int i = 0; i <= 10; i++) {
//            workItemRepository.addWorkItem(new WorkItem(i, "test" + i,"test" + i));
//        }
        Log.d("WorkItemListFra row 51 ", "onCreate: " + "" + workItemRepository.getWorkItems().toString());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_item_list_fragment,container,false);
        WorkItemAdapter workItemAdapter = new WorkItemAdapter(workItemRepository.getWorkItems(), new WorkItemAdapter.onCLickResultListener() {
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
