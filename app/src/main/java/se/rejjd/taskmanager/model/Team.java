package se.rejjd.taskmanager.model;

public final class Team {

    private long id;
    private String teamName;
    private boolean activeTeam;

    public Team(long id, String teamName, boolean activeTeam) {
        this.id = id;
        this.teamName = teamName;
        this.activeTeam = true;
    }

    public long getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean isActiveTeam() {
        return activeTeam;
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
        return (int) (id ^ (id >>> 32));
    }
}
