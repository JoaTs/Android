package se.rejjd.taskmanager.model;

public final class User {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String userId;
    private boolean activeUser;
    private long teamId;

    public User(long id, String username, String firstName, String lastName, String userId, long teamId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.activeUser = true;
        this.teamId =teamId;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActiveUser() {
        return activeUser;
    }

    public long getTeamId() {
        return teamId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId='" + userId + '\'' +
                ", activeUser=" + activeUser +
                ", team=" + teamId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}