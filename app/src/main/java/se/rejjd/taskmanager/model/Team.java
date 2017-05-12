package se.rejjd.taskmanager.model;

public class Team {

    private int id;
    private String teamName;
    private boolean activeTeam;

    public Team(int id, String teamName, boolean activeTeam) {
        this.teamName = teamName;
        this.activeTeam = true;
    }

    public int getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setActiveTeam(boolean activeTeam) {
        this.activeTeam = activeTeam;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return id == team.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
