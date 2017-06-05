package se.rejjd.taskmanager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.model.User;
import se.rejjd.taskmanager.model.WorkItem;
import se.rejjd.taskmanager.repository.UserRepository;
import se.rejjd.taskmanager.repository.WorkItemRepository;
import se.rejjd.taskmanager.repository.http.HttpUserRepository;
import se.rejjd.taskmanager.repository.http.HttpWorkItemRepository;
import se.rejjd.taskmanager.repository.sql.SqlUserRepository;
import se.rejjd.taskmanager.repository.sql.SqlWorkItemRepository;

public final class WorkItemUpdateFragment extends Fragment {

    private static final String BUNDLE_WORKITEM_ID = "workitemId";
    private UserRepository userRepository;
    private List<User> users;
    private String[] usernames;
    private int userSpinnerIndexOfOwner;
    private Spinner userSpinner;
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

        userRepository = SqlUserRepository.getInstance(getContext());
        users = userRepository.getUsers();

        usernames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            usernames[i] = users.get(i).getUsername();
            if(users.get(i).getId() == workItemSql.getUserId()){
                userSpinnerIndexOfOwner = i;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.work_item_update_fragment,container,false);

        final EditText editTextTitle = (EditText) view.findViewById(R.id.ed_update_title);
        editTextTitle.setText(workItemSql.getTitle());

        final EditText editTextDescription = (EditText) view.findViewById(R.id.ed_update_description);
        editTextDescription.setText(workItemSql.getDescription());

        final Spinner statusSpinner = (Spinner) view.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.workitem_status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        switch (workItemSql.getStatus()){
            case "UNSTARTED":
                statusSpinner.setSelection(0);
                break;
            case "STARTED":
                statusSpinner.setSelection(1);
                break;
            case "DONE":
                statusSpinner.setSelection(2);
                break;
            case "ARCHIVED":
                statusSpinner.setSelection(3);
                break;
        }

        userSpinner = (Spinner) view.findViewById(R.id.spinner_users);
        ArrayAdapter<String> userAdapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, usernames);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(userAdapter);

        userSpinner.setSelection(userSpinnerIndexOfOwner);

        Button updateButton = (Button) view.findViewById(R.id.update_btn);
        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                User userSelected = getUserFromSpinnerSelectedItem();

                final WorkItem workItemToUpdate = new WorkItem(workItemSql.getId(),editTextTitle.getText().toString(),editTextDescription.getText().toString(), getUserFromSpinnerSelectedItem().getId());
                workItemToUpdate.setStatus(statusSpinner.getSelectedItem().toString().toUpperCase());

                WorkItemRepository httpWorkItemRepository = new HttpWorkItemRepository();
                httpWorkItemRepository.updateWorkItem(workItemToUpdate);
                UserRepository httpUserRepository = new HttpUserRepository();
                httpUserRepository.addUserToWorkItem(getUserFromSpinnerSelectedItem().getUserId(),workItemToUpdate.getId());
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
        return view;
    }

    public User getUserFromSpinnerSelectedItem(){
     return   users.get(userSpinner.getSelectedItemPosition());
    }
}
