package se.rejjd.taskmanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public final class WorkItemDetailFragment extends Fragment {

    private static final String BUNDLE_WORKITEM = "bundleWorkItem";
    private UserRepository userRepository;
    private WorkItem workitem;

    public static Fragment newInstance(long id){
        Fragment fragment = new WorkItemDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_WORKITEM, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkItemRepository workItemRepository = SqlWorkItemRepository.getInstance(getContext());
        userRepository = SqlUserRepository.getInstance(getContext());
        long id = getArguments().getLong(BUNDLE_WORKITEM);
        workitem = workItemRepository.getWorkItem(String.valueOf(id));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.work_item_details_fragment,container,false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_detail_view_title);
        TextView tvStatus = (TextView) view.findViewById(R.id.tv_detail_view_status);
        TextView tvDescription = (TextView) view.findViewById(R.id.tv_detail_view_description);
        TextView tvUser = (TextView) view.findViewById(R.id.tv_detail_view_assignee);
        tvTitle.setText(workitem.getTitle());
        tvStatus.setText(workitem.getStatus());
        tvDescription.setText(workitem.getDescription());
        tvUser.setText(userRepository.getUserById(workitem.getUserId()).getUsername());
        getActivity().setTitle(workitem.getTitle());

        return view;
    }
}
