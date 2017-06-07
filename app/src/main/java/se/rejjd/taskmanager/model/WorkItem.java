package se.rejjd.taskmanager.model;

import android.os.Parcel;

public final class WorkItem{

    private long id;
    private String title;
    private String description;
    private String status;
    private long userId;

    public WorkItem(long id, String title, String description, long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    protected WorkItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public long getUserId() {
        return userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkItem workItem = (WorkItem) o;

        return id == workItem.id && (title != null ? title.equals(workItem.title) : workItem.title == null &&
                (description != null ? description.equals(workItem.description) : workItem.description == null));
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
                ", userId='" + userId + '\'' +
                '}';
    }
}