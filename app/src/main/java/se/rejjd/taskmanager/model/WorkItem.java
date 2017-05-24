package se.rejjd.taskmanager.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class WorkItem implements Parcelable{

    private long id;
    private String title;
    private String description;
    private Status status;
    public long userLongId; //TODO Test Johan


    public WorkItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    protected WorkItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<WorkItem> CREATOR = new Creator<WorkItem>() {
        @Override
        public WorkItem createFromParcel(Parcel in) {
            return new WorkItem(in);
        }

        @Override
        public WorkItem[] newArray(int size) {
            return new WorkItem[size];
        }
    };

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
    }


    public enum Status {
        DONE, UNSTARTED, STARTED, ARCHIVED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkItem workItem = (WorkItem) o;

        if (id != workItem.id) return false;
        if (title != null ? !title.equals(workItem.title) : workItem.title != null) return false;
        return description != null ? description.equals(workItem.description) : workItem.description == null;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "WorkItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", userLongId='" + userLongId + '\'' +
                '}';
    }
}
