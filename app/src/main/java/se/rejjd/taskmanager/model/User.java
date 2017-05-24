package se.rejjd.taskmanager.model;

public final class User {

    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private String userId;
    private boolean activeUser;
    private long teamId; //TODO Test Johan

    public User(long id, String username, String firstname, String lastname, String userId, boolean activeUser, long teamId) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userId = userId;
        this.activeUser = true;
        this.teamId = teamId;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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
                ", firstaname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
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