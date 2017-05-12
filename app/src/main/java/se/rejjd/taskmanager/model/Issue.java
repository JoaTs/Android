package se.rejjd.taskmanager.model;

public class Issue {

    private int id;
    private String description;
    private boolean openIssue;

    public Issue(int id, String description, boolean openIssue) {
        this.id = id;
        this.description = description;
        this.openIssue = true;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOpenIssue(boolean openIssue) {
        this.openIssue = openIssue;
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

        return id == issue.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
