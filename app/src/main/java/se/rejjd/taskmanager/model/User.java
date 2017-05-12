package se.rejjd.taskmanager.model;

public class User {

    private int id;
    private String username;
    private String firstaname;
    private String lastname;
    private String userId;
    private boolean activeUser;

    public User(int id, String username, String firstaname, String lastname, String userId, boolean activeUser) {
        this.id = id;
        this.username = username;
        this.firstaname = firstaname;
        this.lastname = lastname;
        this.userId = userId;
        this.activeUser = true;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstaname() {
        return firstaname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstaname(String firstaname) {
        this.firstaname = firstaname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstaname='" + firstaname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", userId='" + userId + '\'' +
                ", activeUser=" + activeUser +
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
        return id;
    }
}
