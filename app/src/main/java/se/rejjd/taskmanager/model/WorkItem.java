package se.rejjd.taskmanager.model;

public final class WorkItem {

    private Long _ID;
    private String title;
    private String description;
    private Status status;

    public WorkItem(Long _ID, String title, String description) {
        this._ID = _ID;
        this.title = title;
        this.description = description;
    }

    public Long get_ID() {
        return _ID;
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

    public void set_ID(Long _ID) {
        this._ID = _ID;
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

    public enum Status {
        DONE, UNSTARTED, STARTED, ARCHIVED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkItem workItem = (WorkItem) o;

        if (_ID != null ? !_ID.equals(workItem._ID) : workItem._ID != null) return false;
        if (title != null ? !title.equals(workItem.title) : workItem.title != null) return false;
        return description != null ? description.equals(workItem.description) : workItem.description == null;

    }

    @Override
    public int hashCode() {
        int result = _ID != null ? _ID.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkItem{" +
                "_ID=" + _ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
