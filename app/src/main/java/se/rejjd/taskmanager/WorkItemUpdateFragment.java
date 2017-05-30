package se.rejjd.taskmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public final class WorkItemUpdateFragment extends Fragment {

    private static final String BUNDLE_WORKITEM_ID = "workitemId";
    private WorkItemRepository workItemRepository;
    private WorkItem workItemSql;
    public static Fragment getInstance(long id){
        Fragment fragment = new WorkItemUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_WORKITEM_ID, id);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkItemRepository workItemRepository = SqlWorkItemRepository.getInstance(getContext());
        long id = getArguments().getLong(BUNDLE_WORKITEM_ID);
        workItemSql = workItemRepository.getWorkItem(String.valueOf(id));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.work_item_update_fragment,container,false);
        final EditText editTextTitle = (EditText) view.findViewById(R.id.ed_update_title);
        editTextTitle.setText(workItemSql.getTitle());

        final EditText editTextDescription = (EditText) view.findViewById(R.id.ed_update_description);
        editTextDescription.setText(workItemSql.getDescription());

        Button updateButton = (Button) view.findViewById(R.id.update_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final WorkItem workItemToUpdate = new WorkItem(workItemSql.getId(),editTextTitle.getText().toString(),editTextDescription.getText().toString(), workItemSql.getUserId());
                workItemToUpdate.setStatus(workItemSql.getStatus());


                WorkItemRepository workItemRepository = new HttpWorkItemRepository();
                workItemRepository.updateWorkItem(workItemToUpdate);
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}
