package se.rejjd.taskmanager.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.R;
import se.rejjd.taskmanager.model.WorkItem;

public class WorkItemAdapter extends RecyclerView.Adapter<WorkItemAdapter.WorkItemViewHolder> {

    private List<WorkItem> workItems;
    private onCLickResultListener onCLickResultListener;
    private onLongClickListener onLongClickListener;

    public WorkItemAdapter(List<WorkItem> workItems, onCLickResultListener onCLickResultListener, onLongClickListener onLongClickListener) {
        this.workItems = workItems;
        this.onCLickResultListener = onCLickResultListener;
        this.onLongClickListener = onLongClickListener;
    }

    public void setAdapter(List<WorkItem> workItemList) {
        this.workItems = workItemList;
        this.notifyDataSetChanged();
    }

    public interface onCLickResultListener {
        void onClickResult(WorkItem workitem);
    }
    
    public interface onLongClickListener {
        void onLongClickResult(WorkItem workItem);
    }

    @Override
    public WorkItemAdapter.WorkItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.work_item_list_view, parent, false);
        return new WorkItemAdapter.WorkItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkItemAdapter.WorkItemViewHolder holder, int position) {
        WorkItem workItem = workItems.get(position);
        holder.bindView(workItem, onCLickResultListener, onLongClickListener);
    }

    @Override
    public int getItemCount() {
        return this.workItems.size();
    }

    public static class WorkItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvStatus;
        private final TextView tvDescription;
        private final TextView tvUser;

        WorkItemViewHolder(View itemView) {
            super(itemView);

            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            this.tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            this.tvUser = (TextView) itemView.findViewById(R.id.tv_user);
        }

        void bindView(final WorkItem workItem, final onCLickResultListener onCLickResultListener, final WorkItemAdapter.onLongClickListener onLongClickListener) {
            tvTitle.setText(workItem.getTitle());
            tvStatus.setText(workItem.getStatus());
            //TODO setText and background on tvStatus to actual user
            setupStatusText(tvStatus);
//            tvStatus.setBackgroundColor(itemView.getResources().getColor(R.color.primary_gray));
            tvDescription.setText(workItem.getDescription());
            tvUser.setText("@Username");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCLickResultListener.onClickResult(workItem);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClickListener.onLongClickResult(workItem);
                    return true;
                }
            });

        }
        private void setupStatusText(TextView status){
            switch (status.getText().toString()){
                case "UNSTARTED" :{
                    status.setBackgroundColor(itemView.getResources().getColor(R.color.primary_gray));
                    break;
                }
                case "STARTED" :{
                    status.setBackgroundColor(itemView.getResources().getColor(R.color.primary_orange));
                    break;
                }
                case "DONE" :{
                    status.setBackgroundColor(itemView.getResources().getColor(R.color.primary_green));
                    break;
                }
            }
        }
    }


}
