package se.rejjd.taskmanager.model;

public final class Issue {

    private long id;
    private String description;
    private boolean openIssue;

    public Issue(long id, String description) {
        this.id = id;
        this.description = description;
        this.openIssue = true;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOpenIssue() {
        return openIssue;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        return id == issue.id && openIssue == issue.openIssue &&
                (description != null ? description.equals(issue.description) : issue.description == null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (openIssue ? 1 : 0);
        return result;
    }
}