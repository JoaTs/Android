package se.rejjd.taskmanager;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.rejjd.taskmanager.model.WorkItem;

public class WorkItemAdapter extends RecyclerView.Adapter<WorkItemAdapter.WorkItemViewHolder> {

    private final List<WorkItem> workItems;
    private onCLickResultListener onCLickResultListener;
    private onLongClickListener onLongClickListener;

    WorkItemAdapter(List<WorkItem> workItems, onCLickResultListener onCLickResultListener, onLongClickListener onLongClickListener) {
        this.workItems = workItems;
        this.onCLickResultListener = onCLickResultListener;
        this.onLongClickListener = onLongClickListener;
    }

    interface onCLickResultListener {
        void onClickResult(WorkItem workitem);
    }

    interface onLongClickListener {
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
        private final TextView tvDescription;

        WorkItemViewHolder(View itemView) {
            super(itemView);

            this.tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
        }

        void bindView(final WorkItem workItem, final onCLickResultListener onCLickResultListener, final WorkItemAdapter.onLongClickListener onLongClickListener) {
            tvTitle.setText(workItem.getTitle());
            tvDescription.setText(workItem.getDescription());
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
    }
}
