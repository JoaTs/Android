package se.rejjd.taskmanager.repository;

import java.util.List;

import se.rejjd.taskmanager.model.Team;
import se.rejjd.taskmanager.model.User;

public interface TeamRepository {

    List<Team> getTeams();

    Team getTeam(String id);

    Long addTeam(Team team);

    Team updateTeam(Team team);

    boolean addUserToTeam(User user, Team team);
}