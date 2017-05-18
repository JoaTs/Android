package se.rejjd.taskmanager.model;

public final class WorkItem {

    private long id;
    private String title;
    private String description;
    private Status status;

    public WorkItem(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
                "_ID=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
